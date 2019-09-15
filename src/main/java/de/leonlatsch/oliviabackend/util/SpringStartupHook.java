package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.security.AdminManager;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class SpringStartupHook {

    @EventListener(ContextRefreshedEvent.class)
    public void springStartUp() {
        AdminManager.getAdminAccessToken();
    }
}