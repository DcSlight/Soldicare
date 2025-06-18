package ambient_intelligence.initializer;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.UsersService;

@Component("UserInitializer")
@Order(0)
public class UserInitializer implements CommandLineRunner {

    private final UsersService userService;

    public UserInitializer(UsersService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        Stream.of(
                new String[] { "jane", "ADMIN" },
                new String[] { "john", "ADMIN" },
                new String[] { "joanna", "OPERATOR" },
                new String[] { "jack", "END_USER" }
        ).forEach(entry -> {
            String name = entry[0];
            String role = entry[1];

            UserBoundary newUser = new UserBoundary();
            newUser.setRole(role);
            newUser.setUsername(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            newUser.setAvatar(":-)");

            UserID userId = new UserID();
            userId.setEmail(name + "@demo.org");
            newUser.setUserId(userId);

            userService.createUser(newUser);
            System.out.println("Inserted user: " + role + " - " + newUser.getUserId().getEmail());
        });
    }
}
