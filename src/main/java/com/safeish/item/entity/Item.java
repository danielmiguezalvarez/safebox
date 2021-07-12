package com.safeish.item.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.safeish.safebox.entity.Safebox;

@EntityListeners(ItemListener.class)
@Entity
public class Item {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID itemId;
	
    @ManyToOne
    @JoinColumn(name="safebox_id")
    private Safebox safebox;
       
    //read = "pgp_sym_decrypt(content, 'password')", 
	//@ColumnTransformer(write = "pgp_sym_encrypt(?, 'password')")
	private String name;

	public UUID getItemId() {
		return itemId;
	}

	public void setItemId(UUID itemId) {
		this.itemId = itemId;
	}

	public Safebox getSafebox() {
		return safebox;
	}

	public void setSafebox(Safebox safebox) {
		this.safebox = safebox;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((safebox == null) ? 0 : safebox.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (safebox == null) {
			if (other.safebox != null)
				return false;
		} else if (!safebox.equals(other.safebox))
			return false;
		return true;
	}

	
	
}
