package com.safeish;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safeish.item.entity.Item;
import com.safeish.item.repository.ItemRepository;
import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;

@SpringBootTest
public class ItemRepositoryTest {
	
	@Autowired
	ItemRepository itemRepository;
	
    @Autowired
    private  SafeboxRepository safebosRepository;
    
    
    
    @Test
    public void whenSaveItemById_thenCorrect() {
    	Safebox safebox = safebosRepository.save(new Safebox("safebox","pass3."));

		Item item = new Item();
		item.setName("i1");
		item.setSafebox(safebox);
		Item i2 = itemRepository.save(item);
    	
    	assertEquals(item.getItemId(),i2.getItemId());
    }
    
    @Test
    public void whenFindingAllItem_thenCorrect() {
    	Safebox safebox = safebosRepository.save(new Safebox("safebox2","pass3."));
		
    	Item item = new Item();
		item.setName("i1");
		item.setSafebox(safebox);

		Item item2 = new Item();
		item2.setName("i2");
		item2.setSafebox(safebox);
		
		itemRepository.save(item);
		itemRepository.save(item2);
		
		Item i1 = itemRepository.findById(item.getItemId()).get();
		Item i2 = itemRepository.findById(item2.getItemId()).get();

    	assertThat(itemRepository.findAll(), hasItem(i1));
    	assertThat(itemRepository.findAll(), hasItem(i2));
    }

	
}
