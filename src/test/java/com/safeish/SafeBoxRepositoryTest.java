package com.safeish;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.safeish.safebox.entity.Safebox;
import com.safeish.safebox.repository.SafeboxRepository;

@SpringBootTest
public class SafeBoxRepositoryTest {

    
    @Autowired
    private SafeboxRepository safebosRepository;
    
    @Test
    public void whenFindingSafeboxById_thenCorrect() {
        Safebox c = safebosRepository.save(new Safebox("Safebox", "Pass.1"));
        Optional<Safebox> c2 = safebosRepository.findById(c.getId());
        assertEquals(c2.get(),c);
    }
    
    @Test
    public void whenFindingAllSafebox_thenCorrect() {
    	Safebox s1 = new Safebox("Safebox1", "Pass.1");
    	Safebox s2 = new Safebox("Safebox2", "Pass.2");
    	safebosRepository.save(s1);
    	safebosRepository.save(s2);
    	assertThat(safebosRepository.findAll(), hasItem(s1));
    	assertThat(safebosRepository.findAll(), hasItem(s2));
    }
    
    
    @Test
    public void whenFindByName_thenCorrect() {
    	Safebox safebox1 = safebosRepository.save(new Safebox("Safebox3", "Pass.3"));
    	
        Safebox safebox2 = safebosRepository.findByName(safebox1.getName());
        assertEquals(safebox2,safebox1);
    }

}
