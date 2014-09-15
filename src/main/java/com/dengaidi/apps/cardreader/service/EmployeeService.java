package com.dengaidi.apps.cardreader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dengaidi.apps.cardreader.entity.Employee;
import com.dengaidi.apps.cardreader.repository.EmployeeRepository;

@Component
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Employee findEmployeeById(int id) {
		return employeeRepository.findEmployeeById(id);
	}
	
	public void addEmployee(Employee employee) {
		employeeRepository.save(employee);
	}

	public EmployeeRepository getEmployeeRepository() {
		return employeeRepository;
	}

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
	
	

}
