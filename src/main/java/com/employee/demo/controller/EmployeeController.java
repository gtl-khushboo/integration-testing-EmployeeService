package com.employee.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.demo.model.Employee;
import com.employee.demo.service.EmployeeService;

@RestController
@RequestMapping(path = "/v1")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	private String commonURI = "/v1";

	@GetMapping("/welcome")
	public String getWelcomeString() {
		return "Welcome";
	}

	@GetMapping("/employee/{id}")
	public Employee getEmployee(@PathVariable String id) {
		return employeeService.getEmployeeById(id, commonURI + "/employee/");
	}

	@GetMapping("/allemployees")
	public List<Employee> getAllEmployee() {
		return employeeService.getAllEmployees();
	}

	@PostMapping("/employee")
	public void createEmployee(@RequestBody Employee employee) {
		employeeService.createEmployee(employee);
	}

	@GetMapping("/employee/salary/{id}")
	public String getEmployeeSalary(@PathVariable String id) {
		return employeeService.getEmployeeSalary(id, commonURI + "/employee/salary/");
	}

}
