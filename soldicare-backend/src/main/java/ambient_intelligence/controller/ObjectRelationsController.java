package ambient_intelligence.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ambient_intelligence.domain.boundary.ObjectChildIdBoundary;
import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.logic.SearchingObjectService;

import java.util.List;

@RestController
@RequestMapping(path = { "/ambient-intelligence/objects" })
public class ObjectRelationsController {

    private final SearchingObjectService objectsService;

    public ObjectRelationsController(SearchingObjectService objectsService) {
        this.objectsService = objectsService;
    }

    // Bind a parent object to a child object
    @PutMapping(
        path = "{parentSystemID}/{parentObjectId}/children",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void bindObjects(
        @PathVariable("parentSystemID") String parentSystemID,
        @PathVariable("parentObjectId") String parentObjectId,
        @RequestParam("userSystemID") String userSystemID,
        @RequestParam("userEmail") String userEmail,
        @RequestBody ObjectChildIdBoundary child
    ) {
        this.objectsService.bindObjects(
            parentSystemID,
            parentObjectId,
            child.getChildId().getSystemID(),
            child.getChildId().getObjectId(),
            userSystemID,
            userEmail
        );
    }

    // Get all children of a parent object with pagination
    @GetMapping(path = "{parentSystemID}/{parentObjectId}/children", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ObjectBoundary> getChildren(
        @PathVariable("parentSystemID") String parentSystemID,
        @PathVariable("parentObjectId") String parentObjectId,
        @RequestParam("userSystemID") String userSystemID,
        @RequestParam("userEmail") String userEmail,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return this.objectsService.getChildren(
            parentSystemID, parentObjectId, userSystemID, userEmail, size, page
        );
    }

    // Get all parents of a child object with pagination
    @GetMapping(path = "{childSystemID}/{childObjectId}/parents", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ObjectBoundary> getParents(
        @PathVariable("childSystemID") String childSystemID,
        @PathVariable("childObjectId") String childObjectId,
        @RequestParam("userSystemID") String userSystemID,
        @RequestParam("userEmail") String userEmail,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return this.objectsService.getParents(
            childSystemID, childObjectId, userSystemID, userEmail, size, page
        );
    }
}
