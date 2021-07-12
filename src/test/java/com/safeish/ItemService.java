package com.safeish;

import java.lang.reflect.MalformedParametersException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.safeish.item.entity.Item;
import com.safeish.item.repository.ItemRepository;
import com.safeish.item.service.IItemService;
import com.safeish.item.service.impl.exceptions.IdsNotMatchException;
import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;
import com.safeish.securing.JwtTokenUtil;
import com.safeish.securing.SecurityConstants;

import javassist.NotFoundException;

@SpringBootTest
public class ItemService {
	
	@Autowired
	IItemService itemService;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
    SafeboxRepository safebosRepository;

	
	@Test
	void putAndGetItemTest()  {
		
    	Safebox safebox = safebosRepository.save(new Safebox("safebox","pass3."));
		String token = SecurityConstants.TOKEN_PREFIX+JwtTokenUtil.getInstance().generateToken(safebox.getName());	
		
		try {
			itemService.putItem(token, Arrays.asList("Item 1","Item 2","Item 3"), safebox.getId().toString());
		} catch (MalformedParametersException | NotFoundException | IdsNotMatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Item> items = (List<Item>) itemRepository.findAll();
		
		List<String> a = items.stream().map(Item::getName).collect(Collectors.toList());
		
		Assert.assertTrue(a.contains("Item 1"));
		Assert.assertTrue(a.contains("Item 2"));
	    Assert.assertTrue(a.contains("Item 3"));

	    String itemsString = "";
		try {
			itemsString = itemService.getItems(token).toString();
		} catch (MalformedParametersException | NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertTrue(itemsString.contains("Item 1"));
		Assert.assertTrue(itemsString.contains("Item 1"));
		Assert.assertTrue(itemsString.contains("Item 1"));

		
		
	}
	
	
	ResponseEntity<Object> getItems(String token) throws NotFoundException, MalformedParametersException {
		return null;
	}


	
}
