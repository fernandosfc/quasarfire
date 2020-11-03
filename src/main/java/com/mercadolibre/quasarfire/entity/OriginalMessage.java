package com.mercadolibre.quasarfire.entity;

import java.awt.geom.Point2D;

public class OriginalMessage {
	
	private Point2D.Float position;
	private String message;
	
	public Point2D.Float getPosition() {
		return position;
	}
	public void setPosition(Point2D.Float position) {
		this.position = position;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
