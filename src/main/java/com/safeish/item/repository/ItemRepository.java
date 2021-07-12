package com.safeish.item.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safeish.item.entity.Item;


@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
	
	@Query(value = "select i from Item i where safebox.id = :safeboxId")
	public List<Item> findBySafebox(UUID safeboxId);	
	
}