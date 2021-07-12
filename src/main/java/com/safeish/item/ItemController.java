package com.safeish.item;

import java.lang.reflect.MalformedParametersException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safeish.item.service.IItemService;
import com.safeish.item.service.impl.exceptions.IdsNotMatchException;

import javassist.NotFoundException;

@RestController
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	IItemService itemServoice;

	@PutMapping(value = "/{id}")
	public ResponseEntity<String> item(@RequestHeader("Authorization") String token, @RequestBody List<String> items, @PathVariable("id") String id) {

		try {
			itemServoice.putItem(token,items,id);
			return new ResponseEntity<>("Content correctly added to the safebox",HttpStatus.OK);
		}catch (MalformedParametersException e) {
			return new ResponseEntity<>("Malformed expected data",HttpStatus.UNPROCESSABLE_ENTITY );	
		} catch (NotFoundException e)		{
			return new ResponseEntity<>("Requested safebox does not exist", HttpStatus.NOT_FOUND);
		} catch (IdsNotMatchException e){
			return new ResponseEntity<>("ID path and id in token not match", HttpStatus.BAD_REQUEST);
		}catch (Exception e) {			
			e.printStackTrace();
			return new ResponseEntity<>("Unexpected API error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@GetMapping(path = "")
	public ResponseEntity<Object> getItems(@RequestHeader("Authorization") String token) {

			try
			{
				return itemServoice.getItems(token);
			} catch (MalformedParametersException e)
			{
				return new ResponseEntity<>("Malformed expected data",HttpStatus.UNPROCESSABLE_ENTITY );	
			} catch (NotFoundException e)
			{
				return new ResponseEntity<>("Requested safebox does not exist", HttpStatus.NOT_FOUND);
			} catch (Exception e) {			
				e.printStackTrace();
				return new ResponseEntity<>("Unexpected API error", HttpStatus.INTERNAL_SERVER_ERROR);
			}


	}


}
