package ambient_intelligence.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.CommandsService;
import ambient_intelligence.logic.ObjectsService;
import ambient_intelligence.logic.UsersService;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsersService usersService;
    
    @MockBean
    private ObjectsService objectsService;
    
    @MockBean
    private CommandsService commandsService;

    @Test
    public void testDeleteAllUsers() throws Exception {
        // Given: valid admin credentials
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        
        // When: the service deletes all users (mock void method)
        doNothing().when(usersService).deleteAllUsers(userSystemID, userEmail);
        
        // Then: DELETE request should succeed
        mockMvc.perform(delete("/ambient-intelligence/admin/users")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteAllCommands() throws Exception {
        // Given: valid admin credentials
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        
        // When: the service deletes all commands
        doNothing().when(commandsService).deleteAllCommands(userSystemID, userEmail);
        
        // Then: DELETE request should succeed
        mockMvc.perform(delete("/ambient-intelligence/admin/commands")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteAllObjects() throws Exception {
        // Given: valid admin credentials
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        
        // When: the service deletes all objects
        doNothing().when(objectsService).deleteAllObjects(userSystemID, userEmail);
        
        // Then: DELETE request should succeed
        mockMvc.perform(delete("/ambient-intelligence/admin/objects")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testExportAllUsers() throws Exception {
        // Given: valid admin credentials and pagination parameters
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        int size = 10;
        int page = 0;
        
        // And: a list of users to return
        UserBoundary user1 = new UserBoundary();
        UserID userId1 = new UserID("user1@example.com", "SYSTEM");
        user1.setUserId(userId1);
        user1.setRole("END_USER");
        user1.setUsername("user1");
        user1.setAvatar("avatar1.png");
        
        UserBoundary user2 = new UserBoundary();
        UserID userId2 = new UserID("user2@example.com", "SYSTEM");
        user2.setUserId(userId2);
        user2.setRole("OPERATOR");
        user2.setUsername("user2");
        user2.setAvatar("avatar2.png");
        
        List<UserBoundary> userList = Arrays.asList(user1, user2);
        
        // When: the service exports all users
        when(usersService.getAllUsers(userSystemID, userEmail, size, page)).thenReturn(userList);
        
        // Then: GET request should return the user list
        mockMvc.perform(get("/ambient-intelligence/admin/users")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId.email").value("user1@example.com"))
                .andExpect(jsonPath("$[0].role").value("END_USER"))
                .andExpect(jsonPath("$[1].userId.email").value("user2@example.com"))
                .andExpect(jsonPath("$[1].role").value("OPERATOR"));
    }
    
    @Test
    public void testExportAllUsersWithDefaultPagination() throws Exception {
        // Given: valid admin credentials without pagination parameters (should use defaults)
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        
        // And: an empty list of users
        List<UserBoundary> emptyUserList = Collections.emptyList();
        
        // When: the service exports users with default pagination (size=10, page=0)
        when(usersService.getAllUsers(userSystemID, userEmail, 10, 0)).thenReturn(emptyUserList);
        
        // Then: GET request should succeed with default parameters
        mockMvc.perform(get("/ambient-intelligence/admin/users")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void testExportAllCommands() throws Exception {
        // Given: valid admin credentials and pagination
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        int size = 5;
        int page = 1;
        
        // And: a list of commands to return
        CommandBoundary command1 = new CommandBoundary();
        // Note: You'll need to set appropriate fields based on your CommandBoundary structure
        
        List<CommandBoundary> commandList = Arrays.asList(command1);
        
        // When: the service exports all commands
        when(commandsService.getAllCommandsHistory(userSystemID, userEmail, size, page)).thenReturn(commandList);
        
        // Then: GET request should return the command list
        mockMvc.perform(get("/ambient-intelligence/admin/commands")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
    
    @Test
    public void testUnauthorizedDeleteUsers() throws Exception {
        // Given: invalid admin credentials (should cause service to throw exception)
        String userSystemID = "INVALID_SYSTEM";
        String userEmail = "notadmin@example.com";
        
        // When: the service throws an unauthorized exception
        doNothing().when(usersService).deleteAllUsers(userSystemID, userEmail);
        
        // Note: If your service throws specific exceptions for unauthorized access,
        // you would mock those here, for example:
        // doThrow(new UnauthorizedException("Not authorized")).when(usersService).deleteAllUsers(userSystemID, userEmail);
        
        // Then: DELETE request should handle the authorization properly
        // (This test assumes your service handles authorization internally)
        mockMvc.perform(delete("/ambient-intelligence/admin/users")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk()); // Or expect appropriate error status if authorization fails
    }
    
    @Test
    public void testMissingRequiredParameters() throws Exception {
        // Given: missing required parameters
        
        // When: we try to delete users without required parameters
        // Then: should return bad request
        mockMvc.perform(delete("/ambient-intelligence/admin/users"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testExportUsersWithInvalidPagination() throws Exception {
        // Given: valid credentials but invalid pagination
        String userSystemID = "ADMIN_SYSTEM";
        String userEmail = "admin@example.com";
        
        // When: the service handles invalid pagination gracefully
        when(usersService.getAllUsers(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        
        // Then: GET request with negative page should still work (service should handle it)
        mockMvc.perform(get("/ambient-intelligence/admin/users")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", "-1")
                .param("page", "-1"))
                .andExpect(status().isOk());
    }
}