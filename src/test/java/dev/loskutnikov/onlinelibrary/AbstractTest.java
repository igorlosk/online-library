package dev.loskutnikov.onlinelibrary;

import dev.loskutnikov.onlinelibrary.users.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.context.event.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import tools.jackson.databind.ObjectMapper;

import java.security.SecureRandom;


@AutoConfigureMockMvc
@SpringBootTest
public class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserTestUtils userTestUtils;

    protected final SecureRandom secureRandom = new SecureRandom();

    private static volatile boolean isSharedSetupDone = false;

    public static PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("root");

    static {
        if (!isSharedSetupDone) {
            POSTGRES_CONTAINER.start();
            isSharedSetupDone = true;
        }

    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("test.postgres.port", POSTGRES_CONTAINER::getFirstMappedPort);
    }

    @EventListener
    public void stopContainer(ContextStoppedEvent e) {
        POSTGRES_CONTAINER.stop();
    }

    public int getRandomInt() {
        return secureRandom.nextInt();
    }

    public String getAuthorizationHeader(UserRole role){
        return "Bearer " + userTestUtils.getJwtTokenWithRole(role);
    }
}
