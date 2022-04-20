package com.employee.demo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.employee.demo.model.EmployeeSalary;
import com.employee.demo.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import lombok.SneakyThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "server.baseuri=http://localhost:8888" })
public class EmployeeServiceTest {

	@Autowired
	private ResourceLoader resourceLoader = null;
	
	@Autowired
	private EmployeeServiceImpl empService;
	
	private String empId = "1";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8888).httpsPort(9999)
			.notifier(new ConsoleNotifier(true)).extensions(new ResponseTemplateTransformer(true)));

	@Test
	@SneakyThrows
	public void testCalltoEmployeeService() {
		try {
			wireMockRule.stubFor(get(anyUrl()).willReturn(aResponse().withStatus(200)
					.withHeader("Content-Type", "application/json").withBody(convertJsonToEmployee(resourceLoader.getResource("classpath:EmpResponse.json").getFile()))));
			String salary = empService.getEmployeeSalary("1","/v1/employees/salary/");
			assertTrue("20000".equalsIgnoreCase(salary));
			verify(exactly(1), getRequestedFor(urlPathEqualTo("/v1/employees/salary/1")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = HttpClientErrorException.class)
	public void testCalltoEmployeeServiceWithErrorResponse() {
		try {
			wireMockRule.stubFor(get(anyUrl()).willReturn(aResponse().withStatus(convertJsonToString(resourceLoader.getResource("classpath:404.json").getFile()))
					.withHeader("Content-Type", "application/json")));
			empService.getEmployeeSalary("2","/v1/employees/salary/");
			verify(exactly(1), getRequestedFor(urlPathEqualTo("/v1/employees/salary/2")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String convertJsonToEmployee(File file) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			EmployeeSalary empWithSalary = objectMapper.readValue(file, EmployeeSalary.class);
			return empWithSalary.getSalary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer convertJsonToString(File file) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readValue(file,JsonNode.class);
			return (Integer)jsonNode.get("status").asInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
