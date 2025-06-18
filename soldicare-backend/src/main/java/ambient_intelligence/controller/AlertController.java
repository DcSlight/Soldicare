package ambient_intelligence.controller;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.logic.AlertService;

@RestController
@RequestMapping(path = { "/ambient-intelligence/alerts" })
public class AlertController {
	
	private final AlertService alertService;

	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}
	
	
	@GetMapping(path = { "/" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<ObjectBoundary> getAllAlerts(@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.alertService.getAll(userSystemID, userEmail, size, page);
	}

}
