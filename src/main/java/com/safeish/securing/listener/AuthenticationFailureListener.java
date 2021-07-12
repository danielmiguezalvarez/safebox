package com.safeish.securing.listener;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	
	@Autowired
	private SafeboxRepository safeboxRepository;
	
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
		String id = e.getAuthentication().getName();
		
		if (id != null)
		{
			Optional<Safebox> safeboxOptional = safeboxRepository.findById(UUID.fromString(id));
			
			if (safeboxOptional.isPresent()){
				
				var safebox = safeboxOptional.get();
				if (safebox.getAttempts() != null && safebox.getAttempts() < AuthenticationSuccessListener.MAX_ATTEMPTS){
					safebox.setAttempts(safebox.getAttempts() + 1);
					safeboxRepository.save(safebox);
				}
			}
		}
		
	}
}