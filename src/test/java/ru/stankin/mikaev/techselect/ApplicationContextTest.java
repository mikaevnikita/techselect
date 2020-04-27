package ru.stankin.mikaev.techselect;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ApplicationContextTest.
 *
 * @author Nikita_Mikaev
 */
public class ApplicationContextTest extends AbstractIT {

    @Autowired
    private Application application;

    @Test
    public void checkContextStarts() {
        assertNotNull(application);
    }

}