package ambient_intelligence.logic;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CommandServiceTestConfig {

    @Bean
    CommandsService commandService() {
        return Mockito.mock(CommandsService.class);
    }
}

