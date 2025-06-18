package ambient_intelligence.logic;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectServiceTestConfig {

    @Bean
    SearchingObjectService objectsService() {
        return Mockito.mock(SearchingObjectService.class);
    }
}
