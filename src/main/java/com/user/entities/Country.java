package com.user.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="country_tab")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer counrtyId;
	
	private String counrtyName;

	public Integer getCounrtyId() {
		return counrtyId;
	}

	public void setCounrtyId(Integer counrtyId) {
		this.counrtyId = counrtyId;
	}

	public String getCounrtyName() {
		return counrtyName;
	}

	public void setCounrtyName(String counrtyName) {
		this.counrtyName = counrtyName;
	}

	
	
	
	
}
