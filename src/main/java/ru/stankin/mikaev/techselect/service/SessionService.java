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
    private UUID sessionId = UUID.randomUUID();

    public UUID getSessionId(){
        return sessionId;
    }
}