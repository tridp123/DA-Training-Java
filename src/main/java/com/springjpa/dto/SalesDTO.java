package com.springjpa.dto;


import java.util.UUID;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SalesDTO {

	private UUID productId;
	private UUID locationId;
	private UUID timeId;
	private int dollars;
	private DateTime createdAt;
	private DateTime modifiedAt;

	public SalesDTO() {
	}

	public SalesDTO(UUID productId, UUID locationId, UUID timeId, int dollars, DateTime createdAt,
			DateTime modifiedAt) {
		this.productId = productId;
		this.locationId = locationId;
		this.timeId = timeId;
		this.dollars = dollars;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}

	public int getDollars() {
		return dollars;
	}

	public void setDollars(int dollars) {
		this.dollars = dollars;
	}

	// @JsonIgnore
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	// @JsonIgnore
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	@JsonProperty
	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}

