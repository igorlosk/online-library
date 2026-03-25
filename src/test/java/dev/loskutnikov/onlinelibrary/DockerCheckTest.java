package dev.loskutnikov.onlinelibrary;


import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class DockerCheckTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15");

    @Test
    void testDockerIsAvailable() {
        assertTrue(container.isRunning());
    }
}
