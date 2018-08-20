package com.springjpa.dto;


import java.util.UUID;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationDTO {

	private UUID locationId;
	private String country;
	private String city;
	private DateTime createdAt;
	private DateTime modifiedAt;

	public LocationDTO() {
	}

	public LocationDTO(UUID locationId, String country, String city, DateTime createdAt,
			DateTime modifiedAt) {
		this.locationId = locationId;
		this.country = country;
		this.city = city;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	@JsonProperty
	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "LocationDTO [locationId=" + locationId + ", country=" + country + ", city=" + city + ", createdAt="
				+ createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}

