package ambient_intelligence.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.id.CommandID;
import ambient_intelligence.id.InvokedBy;
import ambient_intelligence.id.TargetObject;
import ambient_intelligence.id.UserID;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.logic.CommandsService;

@WebMvcTest(CommandController.class)
public class CommandControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CommandsService commandsService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInvokeCommand() throws Exception {
        // Given: a command boundary to invoke
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        
        // And: a list of objects to return (command result)
        Map<String, Object> result1 = new HashMap<>();
        result1.put("status", "Command received");
        result1.put("command", "testCommand");
        
        List<Object> commandResults = Arrays.asList(result1);
        
        // When: the service invokes the command
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(commandResults);
        
        // Then: POST request should succeed
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("Command received"))
                .andExpect(jsonPath("$[0].command").value("testCommand"));
    }
    
    @Test
    public void testInvokeCommandWithEmptyResult() throws Exception {
        // Given: a command boundary
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        
        // And: an empty result list
        List<Object> emptyResults = Collections.emptyList();
        
        // When: the service returns empty results
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(emptyResults);
        
        // Then: POST request should succeed with empty array
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void testInvokeCommandWithMultipleResults() throws Exception {
        // Given: a command boundary
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        
        // And: multiple result objects
        Map<String, Object> result1 = new HashMap<>();
        result1.put("id", "obj1");
        result1.put("type", "DEVICE");
        
        Map<String, Object> result2 = new HashMap<>();
        result2.put("id", "obj2");
        result2.put("type", "SENSOR");
        
        List<Object> multipleResults = Arrays.asList(result1, result2);
        
        // When: the service returns multiple results
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(multipleResults);
        
        // Then: POST request should return all results
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("obj1"))
                .andExpect(jsonPath("$[0].type").value("DEVICE"))
                .andExpect(jsonPath("$[1].id").value("obj2"))
                .andExpect(jsonPath("$[1].type").value("SENSOR"));
    }
    
    @Test
    public void testInvokeCommandWithNullCommandName() throws Exception {
        // Given: a command boundary with null command
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        commandBoundary.setCommand(null);
        
        // And: service handles null command gracefully
        List<Object> errorResult = Arrays.asList(
            Collections.singletonMap("error", "Invalid command")
        );
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(errorResult);
        
        // Then: POST request should succeed (service handles validation)
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].error").value("Invalid command"));
    }
    
    @Test
    public void testInvokeCommandWithInvalidJson() throws Exception {
        // Given: invalid JSON
        String invalidJson = "{\"command\": \"test\", \"invalidField\":}";
        
        // When: we send invalid JSON
        // Then: should return bad request
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testInvokeCommandWithMissingContentType() throws Exception {
        // Given: a command boundary
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        
        // When: we send request without content type
        // Then: should return unsupported media type
        mockMvc.perform(post("/ambient-intelligence/commands")
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testInvokeCommandWithComplexCommandAttributes() throws Exception {
        // Given: a command with complex attributes
        CommandBoundary commandBoundary = createSampleCommandBoundary();
        
        Map<String, Object> complexAttributes = new HashMap<>();
        complexAttributes.put("temperature", 25.5);
        complexAttributes.put("coordinates", Arrays.asList(10.0, 20.0));
        complexAttributes.put("metadata", Collections.singletonMap("source", "sensor"));
        commandBoundary.setCommandAttributes(complexAttributes);
        
        // And: service returns success
        List<Object> successResult = Arrays.asList(
            Collections.singletonMap("status", "Complex command executed")
        );
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(successResult);
        
        // Then: POST request should handle complex data
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].status").value("Complex command executed"));
    }
    
    @Test
    public void testEndpointMapping() throws Exception {
        // Given: a minimal command boundary
        CommandBoundary commandBoundary = new CommandBoundary();
        commandBoundary.setCommand("ping");
        
        // And: service returns a simple response
        List<Object> pingResult = Arrays.asList("pong");
        when(commandsService.invokeCommand(any(CommandBoundary.class))).thenReturn(pingResult);
        
        // Then: verify the endpoint is correctly mapped
        mockMvc.perform(post("/ambient-intelligence/commands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandBoundary)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("pong"));
    }
    
    /**
     * Helper method to create a sample CommandBoundary for testing
     */
    private CommandBoundary createSampleCommandBoundary() {
        CommandBoundary commandBoundary = new CommandBoundary();
        
        // Set command ID
        CommandID commandId = new CommandID("cmd123", "SYSTEM");
        commandBoundary.setId(commandId);
        
        // Set command name
        commandBoundary.setCommand("testCommand");
        
        // Set target object
        ObjectID objectId = new ObjectID("obj123", "SYSTEM");
        TargetObject targetObject = new TargetObject(objectId);
        commandBoundary.setTargetObject(targetObject);
        
        // Set invocation timestamp
        commandBoundary.setInvocationTimestamp(new Date());
        
        // Set invoked by
        UserID userId = new UserID("user@example.com", "SYSTEM");
        InvokedBy invokedBy = new InvokedBy(userId);
        commandBoundary.setInvokedBy(invokedBy);
        
        // Set command attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("priority", "high");
        attributes.put("timeout", 30);
        commandBoundary.setCommandAttributes(attributes);
        
        return commandBoundary;
    }
}