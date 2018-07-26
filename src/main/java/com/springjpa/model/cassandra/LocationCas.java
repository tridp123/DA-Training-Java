package com.springjpa.model.cassandra;


import java.io.Serializable;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table("location")
public class LocationCas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5842680488047414610L;
	
	private UUID locationId;
	private String country;
	private String city;
	private DateTime createdAt;
	private DateTime modifiedAt;

	public LocationCas() {
	}

	public LocationCas(UUID locationId, String country, String city, DateTime createdAt,
			DateTime modifiedAt) {
		this.locationId = locationId;
		this.country = country;
		this.city = city;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	@PrimaryKeyColumn(name = "location_id", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
	@CassandraType(type = Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column("country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column("city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column("created_at")
	public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Column("modified_at")
	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "LocationCas [locationId=" + locationId + ", country=" + country + ", city=" + city + ", createdAt="
				+ createdAt + ", modifiedAt=" + modifiedAt + "]";
	}

}

