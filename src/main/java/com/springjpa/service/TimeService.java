package com.springjpa.service;

import java.util.List;
import java.util.UUID;

import com.springjpa.model.cassandra.TimeCas;
import com.springjpa.model.jpa.Time;

public interface TimeService {

	// get
	public List<TimeCas> getAllTimes();

	public List<Time> getAllTimeInJPA();

	public TimeCas findByIdInCas(UUID id);

	public Time findByIdInJPA(UUID id);

	// add
	public TimeCas saveTimeCas(TimeCas timeCas);

	public Time saveTimeJPA(Time time);

	// update
	public TimeCas updateTimeInCas(TimeCas timeCas, int year, int quarter, int month);

	public Time updateTimeInJPA(Time time, int year, int quarter, int month);

	// dalete
	public void deleteAllTimeInCas();

	public void deleteTimeById(UUID id);

	// check location
	public boolean isExistsTime(TimeCas timeCas);
	public boolean isExistsTimeinJPA(Time time);
}
