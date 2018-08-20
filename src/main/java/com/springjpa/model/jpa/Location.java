package com.springjpa.model.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "location")
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UUID locationId;
	private String country;
	private String city;
	private Timestamp created_at;
	private Timestamp modified_at;

	
	public Location() {
	}
	
	public Location(UUID location_id, String country, String city, Timestamp created_at, Timestamp modified_at) {
		super();
		this.locationId = location_id;
		this.country = country;
		this.city = city;
		this.created_at = created_at;
		this.modified_at = modified_at;
	}

	@Id
	@GeneratedValue
	@Column(name = "location_id", table = "location", unique = true, nullable = false)
	public UUID getLocation_id() {
		return locationId;
	}

	public void setLocation_id(UUID location_id) {
		this.locationId = location_id;
	}

	@Column(name = "country", nullable = false)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "city", nullable = false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "created_at", nullable = false)
	public Timestamp getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.created_at = createdAt;
	}

	@Column(name = "modified_at", nullable = false)
	public Timestamp getModifiedAt() {
		return modified_at;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modified_at = modifiedAt;
	}

	@Override
	public String toString() {
		return "Location [location_id=" + locationId + ", country=" + country + ", city=" + city + ", created_at="
				+ created_at + ", modified_at=" + modified_at + "]";
	}

}
