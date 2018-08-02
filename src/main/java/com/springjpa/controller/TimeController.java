package com.springjpa.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springjpa.dto.TimeDTO;
import com.springjpa.exception.BadRequestException;
import com.springjpa.exception.NoDataFoundException;
import com.springjpa.model.DBType;
import com.springjpa.model.cassandra.TimeCas;
import com.springjpa.model.jpa.Time;
import com.springjpa.service.TimeService;
import com.springjpa.service.impl.TimeServiceImpl;
import com.springjpa.util.DataTimeUtil;

@RestController
@RequestMapping("/time")
public class TimeController {
	public static final Logger log = LoggerFactory.getLogger(TimeController.class);
	@Autowired
	TimeService timeService = new TimeServiceImpl();

	/* get */
	// -------------------Retrieve All
	// Time--------------------------------------------------------
	@GetMapping(value = "/getalltimecas", headers = "Accept=application/json")
	public ResponseEntity<List<TimeDTO>> getAllTimeCas() {
		List<TimeDTO> list = convertListTimeCas(timeService.getAllTimes());
		return new ResponseEntity<List<TimeDTO>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/getallTimejpa", headers = "Accept=application/json")
	public ResponseEntity<List<TimeDTO>> getAllTimeJPA() {
		List<TimeDTO> list = convertListTimeJPA(timeService.getAllTimeInJPA());
		return new ResponseEntity<List<TimeDTO>>(list, HttpStatus.OK);
	}

	// -------------------Retrieve Single Time by
	// Class--------------------------------------------------------
	@GetMapping(value = "/gettimecas/{timeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeDTO> getTimeCas(@PathVariable("timeId") String id) {
		TimeCas TimeCas = timeService.findByIdInCas(UUID.fromString(id));
		if (TimeCas == null) {
			return new ResponseEntity<TimeDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TimeDTO>(convertToDTO(TimeCas, DBType.CASSANDRA), HttpStatus.OK);
	}

	@GetMapping(value = "/gettimejpa/{timeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeDTO> getTimeJPA(@PathVariable("timeId")String id) {
		TimeDTO TimeDTO = convertToDTO(timeService.findByIdInJPA(UUID.fromString(id)), DBType.JPA);
		if (TimeDTO == null) {
			System.out.println("Time with id " + TimeDTO.getTimeId() + " not found");
			return new ResponseEntity<TimeDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("Time DTO id: " + TimeDTO.getTimeId());
		return new ResponseEntity<TimeDTO>(TimeDTO, HttpStatus.OK);
	}

	// add Time info Cas
	@PostMapping(value = "/addTime/addcas")
	public ResponseEntity<TimeDTO> createTime(@RequestParam int month, @RequestParam int quarter,
			@RequestParam int year, UriComponentsBuilder ucBuilder) {
		TimeCas pro = new TimeCas(UUID.randomUUID(), month, quarter, year, DataTimeUtil.getCurrent(),
				DataTimeUtil.getCurrent());
		if (timeService.isExistsTime(pro)) {
			System.out.println("A Time with id" + pro.getTimeId() + " already exist");
			return new ResponseEntity<TimeDTO>(HttpStatus.CONFLICT);
		}
		TimeCas a = timeService.saveTimeCas(pro);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/Time/gettimeCas/{timeId}").buildAndExpand(a.getTimeId()).toUri());
		return new ResponseEntity<TimeDTO>(convertToDTO(a, DBType.CASSANDRA), headers, HttpStatus.CREATED);
	}

	// add Time info JPA
	@PostMapping(value = "/addTime/addjpa")
	public ResponseEntity<TimeDTO> addTimeInJPA(@RequestParam int month, @RequestParam int quarter,
			@RequestParam int year, UriComponentsBuilder ucBuilder) {
		Time pro = new Time(UUID.randomUUID(), month, quarter, year,
				new Timestamp(DataTimeUtil.getCurrent().getMillis()),
				new Timestamp(DataTimeUtil.getCurrent().getMillis()));

		if (timeService.isExistsTimeinJPA(pro)) {
			System.out.println("A Time with id" + pro.getTimeId() + " already exist");
			return new ResponseEntity<TimeDTO>(HttpStatus.CONFLICT);
		}
		Time a = timeService.saveTimeJPA(pro);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/Time/gettimejpa/{timeId}").buildAndExpand(a.getTimeId()).toUri());
		return new ResponseEntity<TimeDTO>(convertToDTO(a, DBType.JPA), headers, HttpStatus.CREATED);
	}

	// add all Time from CAS into JPA
	@PostMapping(value = "/addallTime")
	public ResponseEntity<List<Time>> saveAllCasIntoJPA() {
		for (TimeDTO pro : convertListTimeCas(timeService.getAllTimes())) {
			timeService.saveTimeJPA(convertToJPAEntity(pro));
		}
		return new ResponseEntity<List<Time>>(timeService.getAllTimeInJPA(), HttpStatus.CREATED);
	}

	// update JPA
	@PutMapping(value = "/updateinjpa", headers = "Accept=application/json")
	public ResponseEntity<TimeDTO> updateTime(@RequestParam String id,@RequestParam int month, @RequestParam int quarter,
			@RequestParam int year, UriComponentsBuilder ucBuilder) {
		Time pro = timeService.findByIdInJPA(UUID.fromString(id));
		if (!timeService.isExistsTimeinJPA(pro)) {
			return new ResponseEntity<TimeDTO>(HttpStatus.NOT_FOUND);
		}
		timeService.updateTimeInJPA(pro, month, quarter, year);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/Time/getTimejpa/{timeId}").buildAndExpand(pro.getTimeId()).toUri());
		return new ResponseEntity<TimeDTO>(convertToDTO(pro, DBType.JPA), headers, HttpStatus.OK);
	}

	// delete all Time in Cas
	@DeleteMapping(value = "deleteall", headers = "Accept=application/json")
	public ResponseEntity<TimeDTO> deleteAllTime() {
		timeService.deleteAllTimeInCas();
		return new ResponseEntity<TimeDTO>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(value = "/delete/{timeId}")
	public ResponseEntity<TimeDTO> deleteTime(@PathVariable("timeId") String id) {

		TimeCas dto = timeService.findByIdInCas(UUID.fromString(id));
		if (dto == null) {
			return new ResponseEntity<TimeDTO>(HttpStatus.NOT_FOUND);
		}

		timeService.deleteTimeById(UUID.fromString(id));
		return new ResponseEntity<TimeDTO>(HttpStatus.NO_CONTENT);
	}

	public TimeDTO convertToDTO(Object obj, DBType type) {
		TimeDTO dto = null;
		if (obj == null) {
			throw new NoDataFoundException("Not found Time");
		}
		if (type == DBType.JPA) {
			Time Time = (Time) obj;
			dto = new TimeDTO(Time.getTimeId(), Time.getMonth(), Time.getQuarter(), Time.getYear(),
					new DateTime(Time.getCreatedAt()), new DateTime(Time.getModifiedAt()));
		} else if (type == DBType.CASSANDRA) {
			TimeCas Time = (TimeCas) obj;
			dto = new TimeDTO(Time.getTimeId(), Time.getMonth(), Time.getQuarter(), Time.getYear(), Time.getCreatedAt(),
					Time.getModifiedAt());
		} else {
			throw new BadRequestException("No type");
		}
		return dto;
	}

	public List<TimeDTO> convertListTimeCas(List<TimeCas> list) {
		List<TimeDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Time");
		}
		for (TimeCas lo : list) {
			TimeDTO dto = convertToDTO(lo, DBType.CASSANDRA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public List<TimeDTO> convertListTimeJPA(List<Time> list) {
		List<TimeDTO> listDTO = new ArrayList<>();
		if (list == null) {
			throw new NoDataFoundException("Not found Time");
		}
		for (Time lo : list) {
			TimeDTO dto = convertToDTO(lo, DBType.JPA);
			dto.getCreatedAt().withZone(DateTimeZone.UTC);
			listDTO.add(dto);
		}
		return listDTO;
	}

	public Time convertToJPAEntity(TimeDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		Time time = new Time(dto.getTimeId(), dto.getMonth(), dto.getQuarter(), dto.getYear(),
				new Timestamp(dto.getCreatedAt().getMillis()), new Timestamp(dto.getModifiedAt().getMillis()));
		return time;
	}

	public TimeCas convertToCassandraEntity(TimeDTO dto) {
		if (dto == null) {
			throw new BadRequestException("Parameters not valid");
		}
		TimeCas time = new TimeCas(dto.getTimeId(), dto.getMonth(), dto.getQuarter(), dto.getYear(), dto.getCreatedAt(),
				dto.getModifiedAt());
		return time;
	}

	@RequestMapping("/initialtime")
	public String process() {
		// sample data
		TimeCas p1 = new TimeCas(UUID.randomUUID(), 5, 2, 2018, DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		TimeCas p2 = new TimeCas(UUID.randomUUID(), 12, 4, 2018, DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());
		TimeCas p3 = new TimeCas(UUID.randomUUID(), 2, 1, 2018, DataTimeUtil.getCurrent(), DataTimeUtil.getCurrent());

		timeService.saveTimeCas(p1);
		timeService.saveTimeCas(p2);
		timeService.saveTimeCas(p3);
		return "Done";
	}
}
