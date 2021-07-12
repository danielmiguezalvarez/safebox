package com.safeish.securing.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	SafeboxRepository safeboxRepository;

	
	@Override
	public UserDetails loadUserByUsername(String safeboxId) throws UsernameNotFoundException {
		

		Optional<Safebox> safeboxOpional = safeboxRepository.findById(UUID.fromString(safeboxId));
		if (!safeboxOpional.isPresent())
			throw new UsernameNotFoundException("User" + safeboxId + " was not found");
		
		Safebox safebox = safeboxOpional.get();
		if (safebox.getAttempts() > 2) {
			System.out.println("user blocked");
			return new User(safebox.getName(), safebox.getPassword(), true, true, true, false, Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
		}
		return new User(safebox.getName(), safebox.getPassword(),Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
	}

		
}
