package com.safeish.safebox;

import java.lang.reflect.MalformedParametersException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.service.ISafeboxService;
import com.safeish.safebox.service.impl.exceptions.RepeatedNameSafebox;
import com.safeish.safebox.service.impl.exceptions.ValidatePasswordExceptions;

@RestController
@RequestMapping("")
public class SafeboxRegisterController {

	@Autowired
	ISafeboxService safeboxService;
	
	@PostMapping("/safebox")
	public ResponseEntity<String> addSafebox(@RequestBody Safebox safebox) {
		try {

			UUID newSafebox = safeboxService.safeboxRegister(safebox);
			return ResponseEntity.ok(newSafebox.toString());
			
		} catch (RepeatedNameSafebox e) {
			return new ResponseEntity<>("Safebox already exists", HttpStatus.CONFLICT);
		} catch (MalformedParametersException e) {
			return new ResponseEntity<>("Malformed expected data", HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (ValidatePasswordExceptions ex1) {
			return new ResponseEntity<>(ex1.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception ex) {
			return new ResponseEntity<>("Unexpected API error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
