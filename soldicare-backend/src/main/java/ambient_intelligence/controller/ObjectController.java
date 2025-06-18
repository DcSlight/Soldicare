package ambient_intelligence.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.logic.SearchingObjectService;

@RestController
@RequestMapping(path = { "/ambient-intelligence/objects" })
public class ObjectController {
	private SearchingObjectService objectsService;

	public ObjectController(SearchingObjectService objectsService) {
		this.objectsService = objectsService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ObjectBoundary> getAllObjects(@RequestParam("userSystemID") String userSystemID,
			@RequestParam("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return objectsService.getAllObjects(userSystemID, userEmail, size, page);
	}

	@GetMapping(path = "/{systemID}/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getById(@PathVariable("objectId") String objectId, @PathVariable("systemID") String systemID,
			@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail) {

		return objectsService.getSpecificObject(systemID, objectId, userSystemID, userEmail)
				.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
						org.springframework.http.HttpStatus.NOT_FOUND, "Object not found or invalid system ID"));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary create(@RequestBody ObjectBoundary object) {
		return objectsService.create(object);
	}

	@PutMapping(path = "/{systemID}/{objectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@PathVariable("systemID") String systemID, @PathVariable("objectId") String objectId,
			@RequestParam("userSystemID") String userSystemID, @RequestParam("userEmail") String userEmail,
			@RequestBody ObjectBoundary update) {
		objectsService.updateObject(systemID, objectId, userSystemID, userEmail, update);
	}

	@GetMapping(path = { "/search/byAlias/{alias}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] searchByExactAlias(@PathVariable("alias") String alias,
			@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return objectsService.searchByExactAlias(userSystemID, userEmail, alias, size, page)
				.toArray(new ObjectBoundary[0]);

	}

	@GetMapping(path = { "/search/byAliasPattern/{pattern}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] searchByAliasPattern(@PathVariable("pattern") String pattern,
			@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.objectsService.searchByAliasPattern(userSystemID, userEmail, pattern, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byType/{type}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] searchByType(@PathVariable("type") String type,
			@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.objectsService.searchByType(userSystemID, userEmail, type, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byStatus/{status}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] searchByStatus(@PathVariable("status") String status,
			@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.objectsService.searchByStatus(userSystemID, userEmail, status, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byTypeAndStatus/{type}/{status}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] searchByTypeAndStatus(@PathVariable("type") String type,
			@PathVariable("status") String status, @RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.objectsService.searchByTypeAndStatus(userSystemID, userEmail, type, status, size, page)
				.toArray(new ObjectBoundary[0]);
	}
}
