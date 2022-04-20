package com.employee.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(Include.NON_NULL)
public class Employee {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String salGrade;
	private String salary;
}
