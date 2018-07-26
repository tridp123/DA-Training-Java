package com.springjpa.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springjpa.model.cassandra.TimeCas;
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
