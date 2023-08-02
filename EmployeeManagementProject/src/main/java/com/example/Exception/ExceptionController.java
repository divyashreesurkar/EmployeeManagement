package com.example.Exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> resp = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			resp.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ClassNotGetException.class)
	public ResponseEntity<Object> exception(ClassNotGetException e) {
		return new ResponseEntity<Object>("Record Not Found For Given Id", HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> exception(RecordNotFoundException e) {
		return new ResponseEntity<Object>("Employee Not Found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = NoSuchElementException.class)
	public ResponseEntity<Object> exception(NoSuchElementException e) {
		return new ResponseEntity<Object>("Can Not Update Record Because Of Invalid Id", HttpStatus.NOT_FOUND);
	}

}
