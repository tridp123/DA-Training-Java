package com.springjpa.model.jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sales")
public class Sales implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SalesId id;
	private Product product;
	private Location location;
	private Time time;
	private int dollars;
	private Timestamp createdAt;
	private Timestamp modifiedAt;

	public Sales() {
	}

	public Sales(SalesId id, Product product, Location location, Time time, int dollars, Timestamp createdAt,
			Timestamp modifiedAt) {
		this.id = id;
		this.product = product;
		this.location = location;
		this.time = time;
		this.dollars = dollars;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	/*
	 * The EmbeddedId annotation is applied to a persistent field or property of an
	 * entity class or mapped superclass to denote a composite primary key that is
	 * an embeddable class. The embeddable class must be annotated as Embeddable.
	 * 
	 * There must be only one EmbeddedId annotation and no Id annotation when the
	 * EmbeddedId annotation is used.
	 */
	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
			@AttributeOverride(name = "timeId", column = @Column(name = "time_id", nullable = false)),
			@AttributeOverride(name = "locationId", column = @Column(name = "location_id", nullable = false)) })
	public SalesId getId() {
		return id;
	}

	public void setId(SalesId id) {
		this.id = id;
	}

	// many sales have same product id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", insertable = false, updatable = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "time_id", insertable = false, updatable = false)
	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	@Column(name = "dollars", nullable = false)
	public int getDollars() {
		return dollars;
	}

	public void setDollars(int dollars) {
		this.dollars = dollars;
	}

	@Column(name = "created_at", nullable = false)
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "modified_at", nullable = false)
	public Timestamp getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Timestamp modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}
