package br.com.curso.spring.io.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="addresses")
public class AddressEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, length=40)
	private String street;

	@Column(nullable=false, length=40)
	private String city;

	@Column(nullable=false, length=2)
	private String state;

	@Column(nullable=false, length=2)
	private String country;

	@Column(nullable=false, length=20)
	private String postalCode;
	
	@ManyToOne
	@JoinColumn(name="users_id", referencedColumnName="id")
	private UserEntity user;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}	
}