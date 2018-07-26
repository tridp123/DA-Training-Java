package com.springjpa.service;


import com.springjpa.model.cassandra.TimeCas;
public interface TimeService {
	
	public Iterable<TimeCas> getAllTimes();

//	public Time addTime(Time time);
//
//	public Time updateTime(Time time);
	
//	public List<Time> getTimeByQueryDslFromJpa(Predicate predicate);
}
