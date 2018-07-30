package com.springjpa.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.model.cassandra.TimeCas;
import com.springjpa.model.jpa.Time;
import com.springjpa.repository.TimeCasRepository;
import com.springjpa.repository.TimeRepository;
import com.springjpa.service.BaseService;
import com.springjpa.service.TimeService;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TimeCas> findByYearInCas(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time findByYearInJPA(int year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeCas saveTimeCas(TimeCas timeCas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time saveTimeJPA(Time time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeCas updateTimeInCas(TimeCas timeCas, int year, int quarter, int month) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time updateTimeInJPA(Time time, int year, int quarter, int month) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllTimeInCas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTimeByYear(int year) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExistsTime(TimeCas timeCas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExistsTimeinJPA(Time time) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = true)
//	public Time addTime(Time time) {
//		time.setCreatedAt(DateTimeUtil.getCurrent());
//		time.setModifiedAt(DateTimeUtil.getCurrent());
//		return jpaRepository.save(time);
//	}
//
//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	@Transactional(readOnly = false)
//	public Time updateTime(Time time) {
//		if (!jpaRepository.findById(time.getTimeId()).isPresent()) {
//			throw new NoDataFoundException("Time ID '" + time.getTimeId() + "' not found in DB");
//		}
//		time.setModifiedAt(DateTimeUtil.getCurrent());
//		return jpaRepository.save(time);
//	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<Time> getTimeByQueryDslFromJpa(Predicate predicate) {
//		List<Time> list = new ArrayList<>();
//		jpaRepository.findAll(predicate).forEach(list::add);
//		return list;
//	}
}
