package com.safeish.item.service;

import java.lang.reflect.MalformedParametersException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.safeish.item.service.impl.exceptions.IdsNotMatchException;

import javassist.NotFoundException;

public interface IItemService {

	void putItem(String safeboxId, List<String> name, String id) throws NotFoundException, MalformedParametersException, IdsNotMatchException;
	ResponseEntity<Object> getItems(String token) throws NotFoundException, MalformedParametersException;

}
