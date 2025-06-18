package ambient_intelligence.logic;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AdminServiceTestConfig {

    @Bean
    UsersService usersService() {
        return Mockito.mock(UsersService.class);
    }

    @Bean
    CommandsService commandsService() {
        return Mockito.mock(CommandsService.class);
    }

    @Bean
    ObjectsService objectsService() {
        return Mockito.mock(ObjectsService.class);
    }
}
