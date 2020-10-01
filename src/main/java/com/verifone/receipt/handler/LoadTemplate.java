package com.verifone.receipt.handler;

import com.verifone.receipt.serviceimpl.ReceiptServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class LoadTemplate implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ReceiptServiceImpl receiptService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Retrieve All Templates");
        try {
            receiptService.retrieveAllTemplate();
        } catch (Exception e) {
            log.error("Failed to Retrieve ", e);
        }
    }
}
