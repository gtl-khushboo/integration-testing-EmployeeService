package com.employee.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.employee.demo.model.Employee;
import com.employee.demo.model.EmployeeSalary;
import com.employee.demo.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	private String empServiceURI="/v1/employee/salary/";
	
	private String empSalaryServiceURL = "http://localhost:8888";

	@Override
	public Employee getEmployeeById(String id,String uri) {
		Optional<Employee> optionalEmp = employeeRepository.findById(id);
		ResponseEntity<String> response = restTemplate.exchange(empSalaryServiceURL+uri+id,HttpMethod.GET,null, String.class);
		String salary = response.getBody();
		if (optionalEmp.isPresent()) {
			Employee employee = optionalEmp.get();
			employee.setSalary(salary);
			return employee;
		} else {
			return null;
		}
	}
	
	
	@Override
	public String getEmployeeSalary(String id,String uri) {
		ResponseEntity<String> response = restTemplate.exchange(empSalaryServiceURL+uri+id,HttpMethod.GET,null, String.class);
		return response.getBody();
	}

	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> emps = employeeRepository.findAll();
		List<EmployeeSalary> empSalary = restTemplate.getForObject(empSalaryServiceURL+empServiceURI, List.class);
		return employeeRepository.findAll();
	}

	@Override
	public void createEmployee(Employee employee) {
		employeeRepository.save(employee);
	}

}
