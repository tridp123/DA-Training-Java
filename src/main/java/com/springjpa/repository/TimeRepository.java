package com.springjpa.repository;

import java.util.UUID;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.model.jpa.Time;

@Repository
public interface TimeRepository extends CrudRepository<Time, UUID>,QuerydslPredicateExecutor<Time> {

}
