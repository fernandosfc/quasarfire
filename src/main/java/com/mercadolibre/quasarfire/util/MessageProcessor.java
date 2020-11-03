package com.mercadolibre.quasarfire.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lemmingapex.trilateration.LinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.mercadolibre.quasarfire.config.ApplicationLogger;
import com.mercadolibre.quasarfire.entity.Satellite;
import com.mercadolibre.quasarfire.entity.StolenMessage;

@Component
public class MessageProcessor {

	@Autowired
	private ApplicationLogger applicationLogger;
	
	/**
	 * Attempts to build the original message and performs several analysis in order to resolve if it is fully completed
	 * @param stolenMessages All the stolen messages captured so far
	 * @param availableInputs The amount of available satellites that should capture and sent a partial message
	 * @return The complete original message if it could be restored, or null otherwise
	 */
	public String[] resolveMessage(List<StolenMessage> stolenMessages, int availableInputs) {
		
		//Build most complete and short message from all messages received
		String[] mostCompleteMessage = this.buildMostCompleteMessage(stolenMessages);
		applicationLogger.LogInfo("MessageProcessor.resolveMessag_mostCompleteMessage", "Message: " + Arrays.toString(mostCompleteMessage));
		
		//Begin validation process in order to resolve if message is ready to be delivered
		String[] readyToBeDelivered = null;
		
		//if message has no gaps...
		if (Arrays.stream(mostCompleteMessage).filter(a -> a.isEmpty()).count() == 0) {
			
			//...it's ready to be delivered without any further control
			readyToBeDelivered = mostCompleteMessage;
			applicationLogger.LogInfo("MessageProcessor.resolveMessage_noGaps");
			
		  //(The other acceptable option, is that all gaps in the message were caused by phase shifting)
		} else if (availableInputs == stolenMessages.size()) { //All expected messages must have been received, so no beginning words could be missing
			
			//Then, it is necessary to get the index of the first word in the message
			int firstWordIndex = IntStream.range(0, mostCompleteMessage.length).filter(i -> !mostCompleteMessage[i].isEmpty()).findFirst().getAsInt();
			
			//if the first word does not occupy the first spot,...
			if (firstWordIndex != 0) {
				
				//...and removing the gaps before it...
				String[] trimmedMessage = Arrays.copyOfRange(mostCompleteMessage, firstWordIndex, mostCompleteMessage.length);
				
				//...leaves the message without any more blanks,...
				if (Arrays.stream(trimmedMessage).filter(a -> a.isEmpty()).count() == 0) {
					
					//...then phase shifting issue can be confirmed and message is ready to be delivered
					readyToBeDelivered = trimmedMessage;
					applicationLogger.LogInfo("MessageProcessor.resolveMessage_phaseShiftedGaps", "FirstWordLocation: " + firstWordIndex);
				}
			}
		}
		
		//Otherwise, the result will be null
		return readyToBeDelivered;
	}
	
	/**
	 * Loops through all stolen messages pieces and attempts to build the complete original message
	 * @param stolenMessages  All the stolen messages captured so far
	 * @return The most complete version of the original message
	 */
	private String[] buildMostCompleteMessage(List<StolenMessage> stolenMessages) {
		//Ground with shortest message
		List<String[]> messagesList = stolenMessages.stream().map(StolenMessage::getMessage).collect(Collectors.toList());
		String[] shortestMessage = Collections.min(messagesList, Comparator.comparing(a -> a.length));
		messagesList.remove(shortestMessage);
		
		//Fill all gaps
		for (int i = 1; i <= shortestMessage.length; i++) {
			if (shortestMessage[shortestMessage.length - i].isEmpty()) {
				for (String[] otherMessage : messagesList) {
					if (!otherMessage[otherMessage.length - i].isEmpty()) {
						shortestMessage[shortestMessage.length - i] = otherMessage[otherMessage.length - i];
						break;
					}
				}
			}
		}
		
		//return most complete message
		return shortestMessage;
	}

	/**
	 * Gathers information from collections provided and performs a trilateration operation in order to establish target location 
	 * @param satellites Information about available satellites including their position
	 * @param stolenMessages Information provided from the satellites including their distance to target
	 * @return Calculated position of target if succeeded, null if information is insufficient
	 */
	public Point2D.Float calculateTargetPosition(List<Satellite> satellites, Iterable<StolenMessage> stolenMessages) {
		
		List<StolenMessage> stolenMessagesList = new ArrayList<StolenMessage>();
		stolenMessages.forEach(stolenMessagesList::add);
		
		if(stolenMessagesList.size() < 3) {
			applicationLogger.LogInfo("MessageProcessor.calculateTargetPosition_notEnoughDistances", "Received: " + stolenMessagesList.size());
			return null;
		}		
		
		double[] distances = new double[stolenMessagesList.size()];
		double[][] positions = new double[stolenMessagesList.size()][2];
		for (StolenMessage p : stolenMessagesList) {
			//Find message satellite position in satellites collection
			int arrayPosition =  IntStream.range(0, satellites.size())
				     .filter(i -> satellites.get(i).getName().equals(p.getName()))
				     .findFirst()
				     .orElse(-1);
			Satellite satellite = satellites.get(arrayPosition);
			
			//Get both satellite informed distance and satellite position
			distances[arrayPosition] = p.getDistance();
			positions[arrayPosition] = new double[] {satellite.getPosition().x, satellite.getPosition().y};
			applicationLogger.LogInfo("MessageProcessor.calculateTargetPosition_satelliteInformation", satellite.getName() + " located [" + satellite.getPosition().x + ", " + satellite.getPosition().y + "] is " + p.getDistance() + " far from target");
		}
		
		TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
		LinearLeastSquaresSolver lSolver = new LinearLeastSquaresSolver(trilaterationFunction);
		double[] linearCalculatedPosition = lSolver.solve().toArray();
		return new Point2D.Float((float)linearCalculatedPosition[0], (float)linearCalculatedPosition[1]);
	}
}
