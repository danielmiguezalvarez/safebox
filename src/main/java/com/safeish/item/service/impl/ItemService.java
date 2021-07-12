package com.safeish.item.service.impl;

import java.lang.reflect.MalformedParametersException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.safeish.item.entity.Item;
import com.safeish.item.repository.ItemRepository;
import com.safeish.item.service.IItemService;
import com.safeish.item.service.impl.exceptions.IdsNotMatchException;
import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;
import com.safeish.securing.JwtTokenUtil;

import javassist.NotFoundException;

@Service
public class ItemService implements IItemService {
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	SafeboxRepository safeboxRepository;
	
	@Override
	public void putItem(String token, List<String> itemNames, String id) throws MalformedParametersException, NotFoundException, IdsNotMatchException {
		
		String name = JwtTokenUtil.getInstance().getUserNameFromRequestHeader(token);
		if (name == null || name.equals(""))
			throw new MalformedParametersException("Malformed expected data");

		Safebox safebox = safeboxRepository.findByName(name);
		checkParams(itemNames, safebox, id);
		
		addItemsToSafebox(itemNames, safebox);
		
	}
	
	private void addItemsToSafebox(List<String> itemNames, Safebox safebox) {
		itemNames.forEach(itemName -> {
			
			Item item = new Item();
			item.setName(itemName);
			item.setSafebox(safebox);
			itemRepository.save(item);
			
		});
	}
	
	private void checkParams(List<String> itemName, Safebox safebox, String id) throws NotFoundException, MalformedParametersException, IdsNotMatchException {
		if (itemName == null || itemName.isEmpty())
			throw new MalformedParametersException("Malformed expected data");
		if (safebox == null)
			throw new NotFoundException("Requested safebox does not exist");
		if (safebox.getId() == null || safebox.getId().equals(id))
			throw new IdsNotMatchException("Requested safebox does not exist");
		
	}
	
	@Override
	public ResponseEntity<Object> getItems(String token) throws NotFoundException,MalformedParametersException {
		
		String name = JwtTokenUtil.getInstance().getUserNameFromRequestHeader(token);		
		if (name == null)
			throw new MalformedParametersException("Malformed expected data");
		
		Safebox safebox = safeboxRepository.findByName(name);
		if (safebox == null)
			throw new NotFoundException("Requested safebox does not exist");
		
		
		List<Item> items = itemRepository.findBySafebox(safebox.getId());
		String ret = "";
		if (items != null)
			ret = items.stream().map(Item::getName).collect(Collectors.joining("\n-", "-", ""));
		
		return ResponseEntity.ok(ret);
	}
	
}
