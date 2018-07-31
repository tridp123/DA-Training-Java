package com.springjpa.model.cassandra;


import java.io.Serializable;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table("sales")
public class SalesCas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5427523305381011973L;

	private UUID productId;
	private UUID locationId;
	private UUID timeId;
	private int dollars;
	private DateTime createdAt;
	private DateTime modifiedAt;

	public SalesCas() {
	}

	public SalesCas(UUID productId, UUID locationId, UUID timeId, int dollars, DateTime createdAt,
			DateTime modifiedAt) {
		this.productId = productId;
		this.locationId = locationId;
		this.timeId = timeId;
		this.dollars = dollars;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	@PrimaryKeyColumn(name = "product_id", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
	@CassandraType(type = Name.UUID)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	@PrimaryKeyColumn(name = "time_id", type = PrimaryKeyType.CLUSTERED, ordinal = 3)
	@CassandraType(type = Name.UUID)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}

	@PrimaryKeyColumn(name = "location_id", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
	@CassandraType(type = Name.UUID)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	@Column("dollars")
	public int getDollars() {
		return dollars;
	}

	public void setDollars(int dollars) {
		this.dollars = dollars;
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

}

