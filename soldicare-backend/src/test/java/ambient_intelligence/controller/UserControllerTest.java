package ambient_intelligence.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ambient_intelligence.domain.boundary.NewUserBoundary;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.UsersService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsersService usersService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateNewUser() throws Exception {
        // Given: a new user boundary
        NewUserBoundary newUser = new NewUserBoundary();
        newUser.setEmail("test@example.com");
        newUser.setRole("END_USER");
        newUser.setUsername("testuser");
        newUser.setAvatar("avatar.png");
        
        // And: a created user boundary to return
        UserBoundary createdUser = new UserBoundary();
        UserID userId = new UserID("test@example.com", "SYSTEM");
        createdUser.setUserId(userId);
        createdUser.setRole("END_USER");
        createdUser.setUsername("testuser");
        createdUser.setAvatar("avatar.png");
        
        // When: the service creates a user (match the exact method signature)
        when(usersService.createUser(any(UserBoundary.class))).thenReturn(createdUser);
        
        // Then: POST request should succeed
        mockMvc.perform(post("/ambient-intelligence/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("END_USER"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.avatar").value("avatar.png"));
    }
    
    @Test
    public void testGetUserById() throws Exception {
        // Given: an existing user
        UserBoundary existingUser = new UserBoundary();
        UserID userId = new UserID("test@example.com", "SYSTEM");
        existingUser.setUserId(userId);
        existingUser.setRole("END_USER");
        existingUser.setUsername("testuser");
        existingUser.setAvatar("avatar.png");
        
        // When: the service finds a user (using the correct method from controller)
        when(usersService.login("SYSTEM", "test@example.com")).thenReturn(java.util.Optional.of(existingUser));
        
        // Then: GET request should return the user
        mockMvc.perform(get("/ambient-intelligence/users/SYSTEM/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("END_USER"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    
    
    
    
    @Test
    public void testUpdateUser() throws Exception {
        // Given: an updated user boundary
        UserBoundary updatedUser = new UserBoundary();
        UserID userId = new UserID("test@example.com", "SYSTEM");
        updatedUser.setUserId(userId);
        updatedUser.setRole("ADMIN");
        updatedUser.setUsername("updateduser");
        updatedUser.setAvatar("updated_avatar.png");
        
        // When: we update the user
        // Then: PUT request should succeed
        mockMvc.perform(put("/ambient-intelligence/users/SYSTEM/test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetNonExistentUser() throws Exception {
        // Given: a non-existent user
        when(usersService.login(anyString(), anyString())).thenReturn(java.util.Optional.empty());
        
        // When: we try to get the user
        // Then: it should return not found
        mockMvc.perform(get("/ambient-intelligence/users/SYSTEM/nonexistent@example.com"))
                .andExpect(status().is4xxClientError());
    }
}