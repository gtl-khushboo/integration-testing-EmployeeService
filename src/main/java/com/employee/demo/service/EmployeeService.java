package com.employee.demo.service;

import java.util.List;

import com.employee.demo.model.Employee;

public interface EmployeeService {

	Employee getEmployeeById(String id,String uri);

	List<Employee> getAllEmployees();

	void createEmployee(Employee employee);

	String getEmployeeSalary(String id, String uri);

}
