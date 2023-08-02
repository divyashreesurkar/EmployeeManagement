package com.example.Entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Email;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empId;
	@NotEmpty(message = "Insert Name")
	private String name;
	@NotEmpty(message = "Insert Email Id")
	@Email(message = "Email Id is not Valid")
	private String emailId;

	@Transient
	private List<Rating> ratings = new ArrayList<>();

	public Employee() {
		super();
	}

	public Employee(Integer empId, @NotEmpty(message = "Insert Name") String name,
			@NotEmpty(message = "Insert Email Id") @Email(message = "Email Id is not Valid") String emailId
			) {
		super();
		this.empId= empId;
		this.name = name;
		this.emailId = emailId;
		
	}

	

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", name=" + name + ", emailId=" + emailId + ", ratings=" + ratings + "]";
	}

}
