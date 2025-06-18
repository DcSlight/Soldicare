package ambient_intelligence.logic;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserServiceTestConfig {
    @Bean
    UsersService usersService() {
        return Mockito.mock(UsersService.class);
    }
}
