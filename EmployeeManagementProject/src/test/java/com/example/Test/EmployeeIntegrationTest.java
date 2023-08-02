package com.example.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.Controller.EmployeeController;
import com.example.Entity.Employee;
import com.example.Exception.ClassNotGetException;
import com.example.Exception.NoSuchElementException;
import com.example.Exception.RecordNotFoundException;
import com.example.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	public void testSaveEmployee() throws Exception {

		Employee emp = new Employee(1, "Ram", "ram@gmail.com");
		when(employeeService.saveEmployee(Mockito.any(Employee.class)))
				.thenReturn(ResponseEntity.ok("Employee Record Saved Successfully!"));

		mockMvc.perform(post("/employee/save").contentType(MediaType.APPLICATION_JSON).content(asJsonString(emp)))
				.andExpect(status().isOk()).andExpect(content().string("Employee Record Saved Successfully!"));

		verify(employeeService, times(1)).saveEmployee(Mockito.any(Employee.class));
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		int employeeId = 1;
		String updateRequestBody = "{\"name\":\"Ram\",\"emailId\":\"ram@gmail.com\"}";
		ResponseEntity<Object> successResponse = ResponseEntity.ok("Employee Record Updated Successfully!");
		Mockito.when(employeeService.updateEmployee(Mockito.anyInt(), Mockito.any(Employee.class)))
				.thenReturn(successResponse);

		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{empId}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(updateRequestBody))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Employee Record Updated Successfully!"));

		Mockito.verify(employeeService).updateEmployee(Mockito.eq(employeeId), Mockito.any(Employee.class));
	}

	@Test
	public void testUpdateEmployeeNoSuchElement() throws Exception {
		int nonExistentEmployeeId = 100;
		Employee updatedEmployee = new Employee();
		updatedEmployee.setName("Ram");
		updatedEmployee.setEmailId("ram@gmail.com");
		Mockito.when(employeeService.updateEmployee(Mockito.eq(nonExistentEmployeeId), Mockito.any(Employee.class)))
				.thenThrow(new NoSuchElementException());

		String requestBody = "{\"name\":\"Ram\",\"emailId\":\"ram@gmail.com\"}";

		mockMvc.perform(MockMvcRequestBuilders.put("/employee/update/{empId}", nonExistentEmployeeId)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		Mockito.verify(employeeService).updateEmployee(Mockito.eq(nonExistentEmployeeId), Mockito.any(Employee.class));
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		int employeeId = 1;

		ResponseEntity<String> successResponse = ResponseEntity.ok("Deleted Record Successfully!");

		Mockito.when(employeeService.deleteEmployee(Mockito.anyInt())).thenReturn(successResponse);

		mockMvc.perform(MockMvcRequestBuilders.delete("/employee/delete/{empId}", employeeId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Deleted Record Successfully!"));

		Mockito.verify(employeeService).deleteEmployee(Mockito.eq(employeeId));
	}

	@Test
	public void testDeleteEmployeeNotFound() throws Exception {
		int nonExistentEmployeeId = 100;

		ResponseEntity<String> errorResponse = new ResponseEntity<>("Record Not Found For Given Id",
				HttpStatus.NOT_FOUND);

		Mockito.when(employeeService.deleteEmployee(Mockito.eq(nonExistentEmployeeId)))
				.thenThrow(new ClassNotGetException());

		mockMvc.perform(MockMvcRequestBuilders.delete("/employee/delete/{empId}", nonExistentEmployeeId))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().string(errorResponse.getBody()));

		Mockito.verify(employeeService).deleteEmployee(Mockito.eq(nonExistentEmployeeId));
	}

	@Test
	public void testGetEmployeeById() throws Exception {
		int employeeId = 1;

		Employee sampleEmployee = new Employee();
		sampleEmployee.setEmpId(employeeId);;
		sampleEmployee.setName("Ram");
		sampleEmployee.setEmailId("ram@gmail.com");

		ResponseEntity<Employee> successResponse = ResponseEntity.ok(sampleEmployee);

		Mockito.when(employeeService.getById(Mockito.anyInt())).thenReturn(successResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/employee/get/{empId}", employeeId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.empId").value(employeeId))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ram"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.emailId").value("ram@gmail.com"));

		Mockito.verify(employeeService).getById(Mockito.eq(employeeId));
	}

	@Test
	public void testGetEmployeeByIdNotFound() throws Exception {
		int nonExistentEmployeeId = 100;

		Mockito.when(employeeService.getById(Mockito.eq(nonExistentEmployeeId))).thenThrow(new ClassNotGetException());

		mockMvc.perform(MockMvcRequestBuilders.get("/employee/get/{empId}", nonExistentEmployeeId))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		Mockito.verify(employeeService).getById(Mockito.eq(nonExistentEmployeeId));
	}

	@Test
	public void testGetEmployeeByName() throws Exception {
		String employeeName = "Ram Patil";

		Employee employee1 = new Employee();
		employee1.setEmpId(1);
		employee1.setName("Ram Patil");
		employee1.setEmailId("ram@gmail.com");

		Employee employee2 = new Employee();
		employee2.setEmpId(2);
		employee2.setName("Ram Patil");
		employee2.setEmailId("ram.patil@gmail.com");

		List<Employee> sampleEmployees = Arrays.asList(employee1, employee2);

		Mockito.when(employeeService.getEmployeeByName(Mockito.anyString())).thenReturn(sampleEmployees);

		mockMvc.perform(MockMvcRequestBuilders.get("/employee/getEmpByName/{name}", employeeName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(sampleEmployees.size()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Ram Patil"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Ram Patil"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].emailId").value("ram@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].emailId").value("ram.patil@gmail.com"));

		Mockito.verify(employeeService).getEmployeeByName(Mockito.eq(employeeName));
	}

	@Test
	public void testGetEmployeeByNameNotFound() throws Exception {
		String nonExistentEmployeeName = "Nonexistent Employee";

		Mockito.when(employeeService.getEmployeeByName(Mockito.eq(nonExistentEmployeeName)))
				.thenThrow(new RecordNotFoundException());

		mockMvc.perform(MockMvcRequestBuilders.get("/employee/getEmpByName/{name}", nonExistentEmployeeName))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		Mockito.verify(employeeService).getEmployeeByName(Mockito.eq(nonExistentEmployeeName));
	}

	private String asJsonString(Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
