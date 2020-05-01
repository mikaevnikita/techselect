package ru.stankin.mikaev.techselect.service;

import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * SessionService.
 *
 * @author Nikita_Mikaev
 */
@Service
@VaadinSessionScope
public class SessionService {
    private UUID sessionId;

    public SessionService() {
        startNewSession();
    }

    public UUID getSessionId(){
        return sessionId;
    }

    public void startNewSession() {
        sessionId = UUID.randomUUID();
    }
}