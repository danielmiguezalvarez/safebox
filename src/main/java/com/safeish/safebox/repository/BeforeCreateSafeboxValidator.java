package com.safeish.safebox.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.safeish.safebox.entity.Safebox;

@Component("beforeCreateSafeboxValidator")
public class BeforeCreateSafeboxValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Safebox.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		var safebox  = (Safebox) target;
		
		PasswordValidator validator = new PasswordValidator(Arrays.asList(				
				new LengthRule(8, 30),				
				new CharacterRule(EnglishCharacterData.UpperCase, 1),				
				new CharacterRule(EnglishCharacterData.LowerCase, 1),				
				new CharacterRule(EnglishCharacterData.Digit, 1),				
				new CharacterRule(EnglishCharacterData.Special, 1),				
				new WhitespaceRule()		
		));
		RuleResult result = validator.validate(new PasswordData(safebox.getPassword()));
		
		if (!result.isValid())
		{			
			List<String> messages = validator.getMessages(result);
			String messageTemplate = messages.stream().collect(Collectors.joining(","));

			errors.rejectValue("id", "INSECPASS", messageTemplate);	
		}
				
	}
	
}
