package com.springjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springjpa.util.LogUtil;



@SpringBootApplication
public class SpringJpaPostgreSqlApplication implements CommandLineRunner{

	public static final Logger log = LoggerFactory.getLogger(SpringJpaPostgreSqlApplication.class);
	
	public static void main(String[] args){
		SpringApplication.run(SpringJpaPostgreSqlApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("Run success...!");
	}
}
