package com.demo1.rates1.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private static final boolean initDataSetupDone = false;
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) 
    {
        if (initDataSetupDone)
        {
            initializeTestData();
        }
    }

	 private void initializeTestData() 
	 {
		 
	 }
}


