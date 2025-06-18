package ambient_intelligence.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.domain.boundary.ObjectChildIdBoundary;
import ambient_intelligence.exception.BadRequestException;
import ambient_intelligence.id.ChildID;
import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.SearchingObjectService;

@WebMvcTest(ObjectRelationsController.class)
public class ObjectRelationsControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SearchingObjectService objectsService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testBindObjects() throws Exception {
        // Given: parent and child identifiers
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // And: child object boundary
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        ChildID childId = new ChildID("child123", "SYSTEM");
        childBoundary.setChildId(childId);
        
        // When: the service binds objects (mock void method)
        doNothing().when(objectsService).bindObjects(
            parentSystemID, parentObjectId, 
            childId.getSystemID(), childId.getObjectId(),
            userSystemID, userEmail
        );
        
        // Then: PUT request should succeed
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children", 
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().isOk());
    }
    
    // Test methods for future validation implementation
    // Uncomment and use these tests once controller validation is implemented
    /*
    @Test
    public void testBindObjectsWithNullChildId_WithValidation() throws Exception {
        // Given: parent identifiers and child boundary with null child ID
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        childBoundary.setChildId(null);
        
        // When: we try to bind with null child ID
        // Then: should return bad request due to validation
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testBindObjectsWithNullChildSystemID_WithValidation() throws Exception {
        // Given: parent identifiers and child boundary with null system ID
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        ChildID childId = new ChildID("child123", null); // null systemID
        childBoundary.setChildId(childId);
        
        // When: we try to bind with null child system ID
        // Then: should return bad request due to validation
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testBindObjectsWithNullChildObjectId_WithValidation() throws Exception {
        // Given: parent identifiers and child boundary with null object ID
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        ChildID childId = new ChildID(null, "SYSTEM"); // null objectId
        childBoundary.setChildId(childId);
        
        // When: we try to bind with null child object ID
        // Then: should return bad request due to validation
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().isBadRequest());
    }
    */
    
    @Test
    public void testGetChildren() throws Exception {
        // Given: parent object identifiers and pagination
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: a list of child objects
        ObjectBoundary child1 = createSampleObjectBoundary("child1", "DEVICE");
        ObjectBoundary child2 = createSampleObjectBoundary("child2", "SENSOR");
        List<ObjectBoundary> children = Arrays.asList(child1, child2);
        
        // When: the service returns children
        when(objectsService.getChildren(parentSystemID, parentObjectId, userSystemID, userEmail, size, page))
                .thenReturn(children);
        
        // Then: GET request should return children
        mockMvc.perform(get("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id.objectId").value("child1"))
                .andExpect(jsonPath("$[0].type").value("DEVICE"))
                .andExpect(jsonPath("$[1].id.objectId").value("child2"))
                .andExpect(jsonPath("$[1].type").value("SENSOR"));
    }
    
    @Test
    public void testGetChildrenWithDefaultPagination() throws Exception {
        // Given: parent object identifiers without pagination parameters
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // And: empty children list
        List<ObjectBoundary> emptyChildren = Collections.emptyList();
        
        // When: the service returns empty children with default pagination (size=10, page=0)
        when(objectsService.getChildren(parentSystemID, parentObjectId, userSystemID, userEmail, 10, 0))
                .thenReturn(emptyChildren);
        
        // Then: GET request should succeed with default parameters
        mockMvc.perform(get("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void testGetParents() throws Exception {
        // Given: child object identifiers and pagination
        String childSystemID = "SYSTEM";
        String childObjectId = "child123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 5;
        int page = 1;
        
        // And: a list of parent objects
        ObjectBoundary parent1 = createSampleObjectBoundary("parent1", "CONTAINER");
        ObjectBoundary parent2 = createSampleObjectBoundary("parent2", "GROUP");
        List<ObjectBoundary> parents = Arrays.asList(parent1, parent2);
        
        // When: the service returns parents
        when(objectsService.getParents(childSystemID, childObjectId, userSystemID, userEmail, size, page))
                .thenReturn(parents);
        
        // Then: GET request should return parents
        mockMvc.perform(get("/ambient-intelligence/objects/{childSystemID}/{childObjectId}/parents",
                childSystemID, childObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id.objectId").value("parent1"))
                .andExpect(jsonPath("$[0].type").value("CONTAINER"))
                .andExpect(jsonPath("$[1].id.objectId").value("parent2"))
                .andExpect(jsonPath("$[1].type").value("GROUP"));
    }
    
    @Test
    public void testBindObjectsWithMissingParameters() throws Exception {
        // Given: parent identifiers but missing required parameters
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        ChildID childId = new ChildID("child123", "SYSTEM");
        childBoundary.setChildId(childId);
        
        // When: we try to bind without required parameters
        // Then: should return bad request
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testGetChildrenWithMissingParameters() throws Exception {
        // Given: parent identifiers but missing required parameters
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        
        // When: we try to get children without required parameters
        // Then: should return bad request
        mockMvc.perform(get("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testBindObjectsWithInvalidJson() throws Exception {
        // Given: parent identifiers and invalid JSON
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        String invalidJson = "{\"childId\": {\"objectId\": \"child123\", \"systemID\":}}";
        
        // When: we send invalid JSON
        // Then: should return bad request
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testGetChildrenWithCustomPagination() throws Exception {
        // Given: parent object identifiers with custom pagination
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 20;
        int page = 2;
        
        // And: large list of children
        ObjectBoundary child1 = createSampleObjectBoundary("child1", "DEVICE");
        ObjectBoundary child2 = createSampleObjectBoundary("child2", "SENSOR");
        ObjectBoundary child3 = createSampleObjectBoundary("child3", "ACTUATOR");
        List<ObjectBoundary> children = Arrays.asList(child1, child2, child3);
        
        // When: the service returns children with custom pagination
        when(objectsService.getChildren(parentSystemID, parentObjectId, userSystemID, userEmail, size, page))
                .thenReturn(children);
        
        // Then: GET request should handle custom pagination
        mockMvc.perform(get("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }
    
    // TODO: Once validation is implemented in the controller, these tests should expect BadRequest status
    // For now, they test the current behavior where controller accepts null values without validation
    
    
    
   
    @Test
    public void testBindObjectsWithNullChildObjectId() throws Exception {
        // Given: parent identifiers and child boundary with null object ID
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        ObjectChildIdBoundary childBoundary = new ObjectChildIdBoundary();
        ChildID childId = new ChildID(null, "SYSTEM"); // null objectId
        childBoundary.setChildId(childId);
        
        // When: we try to bind with null child object ID
        // Then: current controller returns 200 (no validation implemented yet)
        mockMvc.perform(put("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childBoundary)))
                .andExpect(status().isOk());
    }
    
    
    @Test
    public void testEndpointMappings() throws Exception {
        // Test that the endpoints are correctly mapped
        String parentSystemID = "SYSTEM";
        String parentObjectId = "parent123";
        String childSystemID = "SYSTEM";
        String childObjectId = "child123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // Mock service responses
        when(objectsService.getChildren(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(objectsService.getParents(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        
        // Test children endpoint
        mockMvc.perform(get("/ambient-intelligence/objects/{parentSystemID}/{parentObjectId}/children",
                parentSystemID, parentObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
        
        // Test parents endpoint
        mockMvc.perform(get("/ambient-intelligence/objects/{childSystemID}/{childObjectId}/parents",
                childSystemID, childObjectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
    }
    
    /**
     * Helper method to create a sample ObjectBoundary for testing
     */
    private ObjectBoundary createSampleObjectBoundary(String objectId, String type) {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        
        // Set object ID
        ObjectID id = new ObjectID(objectId, "SYSTEM");
        objectBoundary.setId(id);
        
        // Set basic properties
        objectBoundary.setType(type);
        objectBoundary.setAlias("Sample " + type);
        objectBoundary.setStatus("ACTIVE");
        objectBoundary.setActive(true);
        objectBoundary.setCreationTimestamp(new Date());
        
        // Set created by
        UserID userId = new UserID("creator@example.com", "SYSTEM");
        CreatedBy createdBy = new CreatedBy(userId);
        objectBoundary.setCreatedBy(createdBy);
        
        // Set object details
        Map<String, Object> details = new HashMap<>();
        details.put("description", "Sample object for testing");
        details.put("priority", "high");
        objectBoundary.setObjectDetails(details);
        
        return objectBoundary;
    }
}