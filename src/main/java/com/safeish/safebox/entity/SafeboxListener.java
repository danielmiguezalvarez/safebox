package com.safeish.safebox.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SafeboxListener {
	
    @PrePersist
    @PreUpdate
    private void beforeAnyUpdate(Safebox safebox) {
		PasswordEncoder codificador = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		safebox.setPassword(codificador.encode(safebox.getPassword()));
    }

	
}
