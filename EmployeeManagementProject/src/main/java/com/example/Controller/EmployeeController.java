package com.example.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.Employee;
import com.example.Service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	

	@PostMapping("/save")
	public ResponseEntity<Object> saveEmployee(@Valid @RequestBody Employee emp) {
		return employeeService.saveEmployee(emp);

	}

	@PutMapping("/update/{empId}")
	public ResponseEntity<Object> updateEmployee(@PathVariable("empId") int empId, @Valid @RequestBody Employee emp) {
		return employeeService.updateEmployee(empId, emp);
	}

	@DeleteMapping("/delete/{empId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("empId") int empId) {
		return employeeService.deleteEmployee(empId);
	}

	
	@GetMapping("/get/{empId}")
	public ResponseEntity<Employee> getById(@PathVariable("empId") int empId) {
		return employeeService.getById(empId);
	}
	

	@GetMapping("/getEmpByName/{name}")
	public List<Employee> getEmployeeByName(@PathVariable("name") String name) {
		return employeeService.getEmployeeByName(name);
	}

}
