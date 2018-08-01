package com.springjpa.service.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.cassandra.ProductCas;
import com.springjpa.model.cassandra.TimeCas;
import com.springjpa.model.jpa.Product;
import com.springjpa.model.jpa.Time;
import com.springjpa.repository.TimeCasRepository;
import com.springjpa.repository.TimeRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.TimeService;
import com.springjpa.util.DataTimeUtil;

@Service
public class TimeServiceImpl extends BaseService implements TimeService {

	@Autowired
	public TimeCasRepository cassRepository;

	@Autowired
	public TimeRepository jpaRepository;

	@Override
	@Transactional(readOnly = true)
	public Iterable<TimeCas> getAllTimes() {
		return cassRepository.findAll();
	}

	@Override
	public Iterable<Time> getAllTimeInJPA() {
		return jpaRepository.findAll();
	}

	@Override
	public List<TimeCas> findByYearInCas(int year) {
		List<TimeCas> result = new ArrayList<>();
		for (TimeCas lo : getAllTimes()) {
			if (lo.getYear()==(year)) {
				result.add(lo);
			}
		}
		return result;
	}

	@Override
	public Time findByYearInJPA(int year) {
		for (Time lo : getAllTimeInJPA()) {
			if (lo.getYear()==(year)) {
				return lo;
			}
		}
		return null;
	}

	@Override
	public TimeCas saveTimeCas(TimeCas timeCas) {
		if (isExistsTime(timeCas)) {
			return new TimeCas();
		}
		return cassRepository.save(timeCas);
	}

	@Override
	public Time saveTimeJPA(Time time) {
		if (isExistsTimeinJPA(time)) {
			return new Time();
		}
		return jpaRepository.save(time);
	}

	@Override
	public TimeCas updateTimeInCas(TimeCas timeCas, int year, int quarter, int month) {
		if ((cassRepository.findById(timeCas.getTimeId()) == null)) {
			throw new NoDataFoundException("Product ID '" + timeCas.getTimeId() + "' not found in DB");
		}
		timeCas.setMonth(month);
		timeCas.setQuarter(quarter);
		timeCas.setYear(year);
		timeCas.setModifiedAt(DataTimeUtil.getCurrent());
		return cassRepository.save(timeCas);
	}

	@Override
	public Time updateTimeInJPA(Time time, int year, int quarter, int month) {
		if ((jpaRepository.findById(time.getTimeId()) == null)) {
			throw new NoDataFoundException("Product ID '" + time.getTimeId() + "' not found in DB");
		}
		time.setMonth(month);
		time.setQuarter(quarter);
		time.setYear(year);
		time.setModifiedAt(new Timestamp(DataTimeUtil.getCurrent().getMillis()));
		return jpaRepository.save(time);
	}

	@Override
	public void deleteAllTimeInCas() {
		cassRepository.deleteAll();
	}

	@Override
	public void deleteTimeByYear(int year) {
		for (TimeCas pro : getAllTimes()) {
			if (pro.getYear()==(year)) {
				cassRepository.delete(pro);
			}
		}
	}

	@Override
	public boolean isExistsTime(TimeCas timeCas) {
		boolean result = false;
		for (TimeCas lo : getAllTimes()) {
			if (lo.getMonth()==timeCas.getMonth()&& lo.getQuarter()==timeCas.getQuarter()
					&& lo.getYear()==timeCas.getYear()) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean isExistsTimeinJPA(Time time) {
		boolean result = false;
		for (Time lo : getAllTimeInJPA()) {
			if (lo.getMonth()==time.getMonth()&& lo.getQuarter()==time.getQuarter()
					&& lo.getYear()==time.getYear()) {
				result = true;
				break;
			}
		}
		return result;
	}

}
