package com.springjpa.service;

import java.util.List;

import com.springjpa.model.cassandra.LocationCas;
import com.springjpa.model.cassandra.TimeCas;
import com.springjpa.model.jpa.Location;
import com.springjpa.model.jpa.Time;

public interface TimeService {

	// get
	public Iterable<TimeCas> getAllTimes();

	public Iterable<Time> getAllTimeInJPA();

	public List<TimeCas> findByYearInCas(int year);

	public Time findByYearInJPA(int year);

	// add
	public TimeCas saveTimeCas(TimeCas timeCas);

	public Time saveTimeJPA(Time time);

	// update
	public TimeCas updateTimeInCas(TimeCas timeCas, int year, int quarter, int month);

	public Time updateTimeInJPA(Time time, int year, int quarter, int month);

	// dalete
	public void deleteAllTimeInCas();

	public void deleteTimeByYear(int year);

	// check location
	public boolean isExistsTime(TimeCas timeCas);
	public boolean isExistsTimeinJPA(Time time);
}
