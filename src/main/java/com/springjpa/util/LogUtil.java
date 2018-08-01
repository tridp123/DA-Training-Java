package com.springjpa.util;

import org.slf4j.Logger;

public class LogUtil {

	public static void trace(Logger logger, String msg) {
		logger.trace(msg);
	}

	public static void debug(Logger logger, String msg) {
		logger.debug(msg);
	}

	public static void info(Logger logger, String msg) {
		logger.info(msg);
	}

	public static void warn(Logger logger, String msg) {
		logger.warn(msg);
	}

	public static void error(Logger logger, String msg) {
		logger.error(msg);
	}
}
