package com.safeish.securing.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private SafeboxRepository safeboxRepository;
    
    public static final Integer MAX_ATTEMPTS = 3;
    
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		
    	String name = event.getAuthentication().getName();
    	
    	if(name != null) {
    		Safebox safebox = safeboxRepository.findByName(name);    		
    		if (safebox != null && safebox.getAttempts() != null && safebox.getAttempts() < MAX_ATTEMPTS) {      			
    			safebox.setAttempts(0);
    			safeboxRepository.save(safebox);    			
    		}    		
    	}

	}
}