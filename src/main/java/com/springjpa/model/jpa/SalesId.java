package com.springjpa.model.jpa;


import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SalesId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UUID productId;
	private UUID timeId;
	private UUID locationId;

	public SalesId() {
	}

	public SalesId(UUID productId, UUID timeId, UUID locationId) {
		this.productId = productId;
		this.timeId = timeId;
		this.locationId = locationId;
	}

	@Column(name = "product_id", nullable = false)
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	@Column(name = "time_id", nullable = false)
	public UUID getTimeId() {
		return timeId;
	}

	public void setTimeId(UUID timeId) {
		this.timeId = timeId;
	}

	@Column(name = "location_id", nullable = false)
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	
	//compare 2 object SalesID
	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SalesId))
			return false;
		SalesId castOther = (SalesId) other;

		return (this.getProductId() == castOther.getProductId()) && (this.getTimeId() == castOther.getTimeId())
				&& (this.getLocationId() == castOther.getLocationId());
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProductId().hashCode();
		result = 37 * result + this.getTimeId().hashCode();
		result = 37 * result + this.getLocationId().hashCode();
		return result;
	}
}

