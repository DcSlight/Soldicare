package ambient_intelligence.controller;

import static org.mockito.ArgumentMatchers.any;
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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.SearchingObjectService;

@WebMvcTest(ObjectController.class)
public class ObjectControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SearchingObjectService objectsService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllObjects() throws Exception {
        // Given: user parameters and pagination
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: a list of objects
        ObjectBoundary object1 = createSampleObjectBoundary("obj1", "DEVICE");
        ObjectBoundary object2 = createSampleObjectBoundary("obj2", "SENSOR");
        List<ObjectBoundary> objects = Arrays.asList(object1, object2);
        
        // When: the service returns objects
        when(objectsService.getAllObjects(userSystemID, userEmail, size, page))
                .thenReturn(objects);
        
        // Then: GET request should return all objects
        mockMvc.perform(get("/ambient-intelligence/objects")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id.objectId").value("obj1"))
                .andExpect(jsonPath("$[0].type").value("DEVICE"))
                .andExpect(jsonPath("$[1].id.objectId").value("obj2"))
                .andExpect(jsonPath("$[1].type").value("SENSOR"));
    }
    
    @Test
    public void testGetAllObjectsWithDefaultPagination() throws Exception {
        // Given: user parameters without pagination
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // And: empty objects list
        List<ObjectBoundary> emptyObjects = Collections.emptyList();
        
        // When: the service returns empty objects with default pagination (size=10, page=0)
        when(objectsService.getAllObjects(userSystemID, userEmail, 10, 0))
                .thenReturn(emptyObjects);
        
        // Then: GET request should succeed with default parameters
        mockMvc.perform(get("/ambient-intelligence/objects")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void testGetById() throws Exception {
        // Given: object identifiers and user parameters
        String systemID = "SYSTEM";
        String objectId = "obj123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // And: an object
        ObjectBoundary object = createSampleObjectBoundary(objectId, "DEVICE");
        
        // When: the service returns the object
        when(objectsService.getSpecificObject(systemID, objectId, userSystemID, userEmail))
                .thenReturn(Optional.of(object));
        
        // Then: GET request should return the object
        mockMvc.perform(get("/ambient-intelligence/objects/{systemID}/{objectId}",
                systemID, objectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id.objectId").value(objectId))
                .andExpect(jsonPath("$.type").value("DEVICE"));
    }
    
    @Test
    public void testGetByIdNotFound() throws Exception {
        // Given: object identifiers and user parameters
        String systemID = "SYSTEM";
        String objectId = "nonexistent";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // When: the service returns empty optional
        when(objectsService.getSpecificObject(systemID, objectId, userSystemID, userEmail))
                .thenReturn(Optional.empty());
        
        // Then: GET request should return 404
        mockMvc.perform(get("/ambient-intelligence/objects/{systemID}/{objectId}",
                systemID, objectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreate() throws Exception {
        // Given: an object to create
        ObjectBoundary objectToCreate = createSampleObjectBoundary("newobj", "DEVICE");
        ObjectBoundary createdObject = createSampleObjectBoundary("newobj", "DEVICE");
        
        // When: the service creates the object
        when(objectsService.create(any(ObjectBoundary.class)))
                .thenReturn(createdObject);
        
        // Then: POST request should create and return the object
        mockMvc.perform(post("/ambient-intelligence/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(objectToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id.objectId").value("newobj"))
                .andExpect(jsonPath("$.type").value("DEVICE"));
    }
    
    @Test
    public void testUpdate() throws Exception {
        // Given: object identifiers, user parameters, and update data
        String systemID = "SYSTEM";
        String objectId = "obj123";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        ObjectBoundary updateData = createSampleObjectBoundary(objectId, "UPDATED_DEVICE");
        
        // When: the service updates the object (void method)
        doNothing().when(objectsService)
                .updateObject(systemID, objectId, userSystemID, userEmail, updateData);
        
        // Then: PUT request should succeed
        mockMvc.perform(put("/ambient-intelligence/objects/{systemID}/{objectId}",
                systemID, objectId)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchByExactAlias() throws Exception {
        // Given: search parameters
        String alias = "TestAlias";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 5;
        int page = 0;
        
        // And: search results
        ObjectBoundary result1 = createSampleObjectBoundary("result1", "DEVICE");
        ObjectBoundary result2 = createSampleObjectBoundary("result2", "SENSOR");
        List<ObjectBoundary> results = Arrays.asList(result1, result2);
        
        // When: the service returns search results
        when(objectsService.searchByExactAlias(userSystemID, userEmail, alias, size, page))
                .thenReturn(results);
        
        // Then: GET request should return search results as array
        mockMvc.perform(get("/ambient-intelligence/objects/search/byAlias/{alias}", alias)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    public void testSearchByAliasPattern() throws Exception {
        // Given: search parameters
        String pattern = "Test*";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: search results
        ObjectBoundary result = createSampleObjectBoundary("pattern_result", "DEVICE");
        List<ObjectBoundary> results = Arrays.asList(result);
        
        // When: the service returns search results
        when(objectsService.searchByAliasPattern(userSystemID, userEmail, pattern, size, page))
                .thenReturn(results);
        
        // Then: GET request should return search results
        mockMvc.perform(get("/ambient-intelligence/objects/search/byAliasPattern/{pattern}", pattern)
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
    public void testSearchByType() throws Exception {
        // Given: search parameters
        String type = "DEVICE";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: search results
        ObjectBoundary result1 = createSampleObjectBoundary("device1", "DEVICE");
        ObjectBoundary result2 = createSampleObjectBoundary("device2", "DEVICE");
        List<ObjectBoundary> results = Arrays.asList(result1, result2);
        
        // When: the service returns search results
        when(objectsService.searchByType(userSystemID, userEmail, type, size, page))
                .thenReturn(results);
        
        // Then: GET request should return search results
        mockMvc.perform(get("/ambient-intelligence/objects/search/byType/{type}", type)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    public void testSearchByStatus() throws Exception {
        // Given: search parameters
        String status = "ACTIVE";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: search results
        ObjectBoundary result = createSampleObjectBoundary("active_obj", "DEVICE");
        List<ObjectBoundary> results = Arrays.asList(result);
        
        // When: the service returns search results
        when(objectsService.searchByStatus(userSystemID, userEmail, status, size, page))
                .thenReturn(results);
        
        // Then: GET request should return search results
        mockMvc.perform(get("/ambient-intelligence/objects/search/byStatus/{status}", status)
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
    public void testSearchByTypeAndStatus() throws Exception {
        // Given: search parameters
        String type = "DEVICE";
        String status = "ACTIVE";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        int size = 10;
        int page = 0;
        
        // And: search results
        ObjectBoundary result = createSampleObjectBoundary("active_device", "DEVICE");
        List<ObjectBoundary> results = Arrays.asList(result);
        
        // When: the service returns search results
        when(objectsService.searchByTypeAndStatus(userSystemID, userEmail, type, status, size, page))
                .thenReturn(results);
        
        // Then: GET request should return search results
        mockMvc.perform(get("/ambient-intelligence/objects/search/byTypeAndStatus/{type}/{status}", 
                type, status)
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
    public void testGetAllObjectsWithMissingParameters() throws Exception {
        // Given: missing required parameters
        
        // When: we try to get all objects without required parameters
        // Then: should return bad request
        mockMvc.perform(get("/ambient-intelligence/objects"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testGetByIdWithMissingParameters() throws Exception {
        // Given: object identifiers but missing required parameters
        String systemID = "SYSTEM";
        String objectId = "obj123";
        
        // When: we try to get object without required parameters
        // Then: should return bad request
        mockMvc.perform(get("/ambient-intelligence/objects/{systemID}/{objectId}",
                systemID, objectId))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testCreateWithInvalidJson() throws Exception {
        // Given: invalid JSON
        String invalidJson = "{\"type\": \"DEVICE\", \"alias\":}";
        
        // When: we send invalid JSON
        // Then: should return bad request
        mockMvc.perform(post("/ambient-intelligence/objects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testUpdateWithMissingParameters() throws Exception {
        // Given: object identifiers but missing required parameters
        String systemID = "SYSTEM";
        String objectId = "obj123";
        ObjectBoundary updateData = createSampleObjectBoundary(objectId, "DEVICE");
        
        // When: we try to update without required parameters
        // Then: should return bad request
        mockMvc.perform(put("/ambient-intelligence/objects/{systemID}/{objectId}",
                systemID, objectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().is4xxClientError());
    }
    
   
    
    
    
    @Test
    public void testSearchWithEmptyResults() throws Exception {
        // Given: search parameters
        String alias = "NonExistentAlias";
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // And: empty search results
        List<ObjectBoundary> emptyResults = Collections.emptyList();
        
        // When: the service returns empty results
        when(objectsService.searchByExactAlias(userSystemID, userEmail, alias, 10, 0))
                .thenReturn(emptyResults);
        
        // Then: GET request should return empty array
        mockMvc.perform(get("/ambient-intelligence/objects/search/byAlias/{alias}", alias)
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    public void testEndpointMappings() throws Exception {
        // Given: mock service responses
        when(objectsService.getAllObjects(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        when(objectsService.getSpecificObject(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(createSampleObjectBoundary("test", "DEVICE")));
        when(objectsService.searchByExactAlias(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        
        // Test that all endpoints are correctly mapped
        String userSystemID = "USER_SYSTEM";
        String userEmail = "user@example.com";
        
        // Test GET all objects
        mockMvc.perform(get("/ambient-intelligence/objects")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
        
        // Test GET specific object
        mockMvc.perform(get("/ambient-intelligence/objects/SYSTEM/obj123")
                .param("userSystemID", userSystemID)
                .param("userEmail", userEmail))
                .andExpect(status().isOk());
        
        // Test search by alias
        mockMvc.perform(get("/ambient-intelligence/objects/search/byAlias/test")
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