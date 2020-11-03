package com.mercadolibre.quasarfire.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLogger {

	@Autowired
	private MessageSource messageSource;
	
	private Log log = LogFactory.getLog(ApplicationLogger.class);
	private final String ERRORFORMAT = "[AppLog] %s in %s.%s - Runtime info: %s";
	private final String ERRORFORMAT_NORUNTIMEINFO = "[AppLog] %s in %s.%s";
	
	public void LogError(String messageKey) {
		this.LogError(messageKey, null);
	}
	
	public void LogError(String messageKey, String runtimeInfo) {
		if (log.isErrorEnabled()) {
			log.error(getMessage(messageKey, runtimeInfo));
		}
	}
	
	public void LogInfo(String messageKey) {
		this.LogInfo(messageKey, null);
	}
	
	public void LogInfo(String messageKey, String runtimeInfo) {
		if (log.isInfoEnabled()) {
			log.info(getMessage(messageKey, runtimeInfo));
		}
	}
	
	private String getMessage(String messageKey, String runtimeInfo) {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
		String message = messageSource.getMessage(messageKey, null, null);
		
		String result = null;
		if(runtimeInfo == null) {
			result = String.format(this.ERRORFORMAT_NORUNTIMEINFO, message, stackTrace.getClassName(), stackTrace.getMethodName());
		} else {
			result = String.format(this.ERRORFORMAT, message, stackTrace.getClassName(), stackTrace.getMethodName(), runtimeInfo);	
		}
		return result;
	}

	public void LogError(RuntimeException ex) {
		if (log.isErrorEnabled()) {
			log.error(ex);
			log.error(ExceptionUtils.getRootCause(ex));
		}
	}
}
