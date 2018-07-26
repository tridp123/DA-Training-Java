package com.springjpa.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

public class CassandraDateTimeConverter implements Converter<Date, DateTime> {

	@Override
	public DateTime convert(Date arg0) {
		return new DateTime(arg0);
	}

}
