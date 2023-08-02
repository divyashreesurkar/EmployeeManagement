package com.example.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Entity.Employee;
import com.example.Entity.Rating;
import com.example.Exception.ClassNotGetException;
import com.example.Exception.NoSuchElementException;
import com.example.Exception.RecordNotFoundException;
import com.example.Repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	

	public ResponseEntity<Object> saveEmployee(Employee emp) {
		employeeRepository.save(emp);
		 return ResponseEntity.status(HttpStatus.CREATED).body("Employee Record Saved Successfully!");
	}

	public ResponseEntity<Object> updateEmployee(int empId, Employee newEntity) {
		Employee availableEntity = employeeRepository.findById(empId).orElse(null);

		if (availableEntity != null) {
			if (newEntity.getName() != null) {
				availableEntity.setName(newEntity.getName());
			}
			if (newEntity.getEmailId() != null) {
				availableEntity.setEmailId(newEntity.getEmailId());
			}

			employeeRepository.save(availableEntity);
			return ResponseEntity.ok("Employee Record Updated Successfully!");
		} else {
			throw new NoSuchElementException();
		}
	}

	public ResponseEntity<String> deleteEmployee(int empId) {
		Optional<Employee> emp = employeeRepository.findById(empId);
		if (emp.isPresent()) {
			employeeRepository.deleteById(empId);
			return new ResponseEntity<>("Deleted Record Successfully!", HttpStatus.OK);
		} else {
			throw new ClassNotGetException();
		}
	}

	
	public ResponseEntity<Employee> getById(int empId) {
		Optional<Employee> employeeOptional = employeeRepository.findById(empId);
		if (employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();

	        ArrayList<Rating> listOfRatings = restTemplate.getForObject(
	                "http://RATING-SERVICE/ratings/employee/"+employee.getEmpId(),
	                ArrayList.class
	        );

	        employee.setRatings(listOfRatings);
	        return ResponseEntity.ok(employee);
		}else {
			throw new ClassNotGetException();
		}
	}

	public List<Employee> getEmployeeByName(String name) {
		List<Employee> employees = employeeRepository.readByName(name);

		if (employees.isEmpty()) {
			throw new RecordNotFoundException();
		}

		 for (Employee employee : employees) {
		        ArrayList<Rating> listOfRatings = restTemplate.getForObject(
		            "http://RATING-SERVICE/ratings/employee/" + employee.getEmpId(),
		            ArrayList.class
		        );

		        employee.setRatings(listOfRatings);
		    }
		return employees;
		
		
	}
}
