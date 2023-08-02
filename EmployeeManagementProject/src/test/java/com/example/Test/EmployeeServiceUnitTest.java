package com.example.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.Entity.Employee;
import com.example.Exception.ClassNotGetException;
import com.example.Exception.NoSuchElementException;
import com.example.Exception.RecordNotFoundException;
import com.example.Repository.EmployeeRepository;
import com.example.Service.EmployeeService;

@SpringBootTest
public class EmployeeServiceUnitTest {

	@Mock
	EmployeeRepository employeeRepository;

	@InjectMocks
	EmployeeService employeeService;

	@Test
	public void saveEmployeeTest() {
		Employee employee = new Employee(1, "Ram", "ram@gmail.com");
		when(employeeRepository.save(employee)).thenReturn(employee);
		ResponseEntity<Object> response = employeeService.saveEmployee(employee);
		assertEquals(ResponseEntity.ok("Employee Record Saved Successfully!"), response);
	}

	@Test
	public void updateEmployeeTest() {
		Employee existingEmployee = new Employee(1, "Ram", "ram@gmail.com");
		Employee updatedEmployee = new Employee(1, "Shyam", "shyam@gmail.com");
		when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
		when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
		ResponseEntity<Object> response = employeeService.updateEmployee(1, updatedEmployee);
		assertEquals(ResponseEntity.ok("Employee Record Updated Successfully!"), response);
	}

	@Test
	public void UpdateEmployeeWithNonExistingIdTest() {
		Employee updatedEmployee = new Employee(1, "Ram", "ram@gmail.com");
		when(employeeRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> {
			employeeService.updateEmployee(1, updatedEmployee);
		});
	}

	@Test
	public void deleteEmployeeTest() {
		Employee employee = new Employee(1, "Ram", "ram@gmail.com");
		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
		ResponseEntity<String> response = employeeService.deleteEmployee(1);
		assertEquals(new ResponseEntity<>("Deleted Record Successfully!", HttpStatus.OK), response);
	}

	@Test
	public void deleteEmployeeWithNonExistingIdTest() {
		when(employeeRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ClassNotGetException.class, () -> {
        employeeService.deleteEmployee(1);
    });
	}

	@Test
	public void getByIdTest() {

		Employee employee = new Employee(1, "Ram", "ram@gmail.com");
		when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
		ResponseEntity<Employee> response = employeeService.getById(1);
		assertEquals(ResponseEntity.ok(employee), response);
	}

	@Test
	public void getByIdWithNonExistingIdTest() {
		when(employeeRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ClassNotGetException.class, () -> {
        employeeService.getById(1);
    });
}

	@Test
	public void getEmployeeByNameTest() {
		Employee employee1 = new Employee(1, "Ram", "ram@gmail.com");
		Employee employee2 = new Employee(2, "Shyam", "shyam@gmail.com");
		List<Employee> employees = new ArrayList<>();
		employees.add(employee1);
		employees.add(employee2);
		when(employeeRepository.readByName("Ram")).thenReturn(employees);
		List<Employee> resultEmployees = employeeService.getEmployeeByName("Ram");
		assertEquals(employees, resultEmployees);
	}

	@Test
	public void getEmployeeByNameWithNonExistingNameTest() {
		when(employeeRepository.readByName("Ram")).thenReturn(new ArrayList<>());
		assertThrows(RecordNotFoundException.class, () -> {
        employeeService.getEmployeeByName("Ram");
    });
}

}




