package com.springjpa.service.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	public List<TimeCas> getAllTimes() {
		List<TimeCas> list = new ArrayList<>();
		cassRepository.findAll().forEach(list::add);
		return list;
		
	}

	@Override
	public List<Time> getAllTimeInJPA() {
		List<Time> list = new ArrayList<>();
		jpaRepository.findAll().forEach(list::add);
		return list;
	}

	@Override
	public TimeCas findByIdInCas(UUID id) {
		TimeCas result = null;
		for (TimeCas a : getAllTimes()) {
			if (a.getTimeId().equals(id)) {
				result  =  a;
			}
		}
		return result;
	}

	@Override
	public Time findByIdInJPA(UUID id) {
		for (Time lo : getAllTimeInJPA()) {
			if (lo.getTimeId().equals(id)) {
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
	public void deleteTimeById(UUID id) {
		for (TimeCas pro : getAllTimes()) {
			if (pro.getTimeId().equals(id)) {
				cassRepository.delete(pro);
				break;
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
