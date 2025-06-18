Feature: Object and Object Relationship Management
  As a system user
  I want to manage and organize objects and their relationships
  So that I can interact with them efficiently in the ambient intelligence system

  Background:
    Given the ambient intelligence system is running
    And I am authenticated as a valid user

  # === Object Management ===

  Scenario: Successfully retrieve all objects with pagination
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are 2 objects in the system:
      | objectId | type   |
      | obj1     | DEVICE |
      | obj2     | SENSOR |
    When I request all objects with pagination size 10 and page 0
    Then I should receive a successful response
    And the response should contain 2 objects
    And the first object should have ID "obj1" and type "DEVICE"
    And the second object should have ID "obj2" and type "SENSOR"

  Scenario: Retrieve all objects with default pagination
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are no objects in the system
    When I request all objects without specifying pagination parameters
    Then I should receive a successful response
    And the response should be an empty array
    And the default pagination should be applied (size=10, page=0)

  Scenario: Fail to retrieve objects with missing parameters
    Given I am making a request to get all objects
    When I do not provide required user parameters
    Then I should receive a 4xx client error response

  Scenario: Successfully retrieve a specific object by ID
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists an object with system ID "SYSTEM" and object ID "obj123"
    And the object has type "DEVICE"
    When I request the object with system ID "SYSTEM" and object ID "obj123"
    Then I should receive a successful response
    And the response should contain the object with ID "obj123"
    And the object should have type "DEVICE"

  Scenario: Fail to retrieve non-existent object
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there is no object with system ID "SYSTEM" and object ID "nonexistent"
    When I request the object with system ID "SYSTEM" and object ID "nonexistent"
    Then I should receive a 404 Not Found response

  Scenario: Fail to retrieve object with missing parameters
    Given there exists an object with system ID "SYSTEM" and object ID "obj123"
    When I request the object without providing required user parameters
    Then I should receive a 4xx client error response

  Scenario: Successfully create a new object
    Given I want to create a new object
    And the object has the following properties:
      | property  | value         |
      | objectId  | newobj        |
      | type      | DEVICE        |
      | alias     | Sample DEVICE |
      | status    | ACTIVE        |
    When I submit a POST request to create the object
    Then I should receive a successful response
    And the response should contain the created object
    And the object should have ID "newobj" and type "DEVICE"

  Scenario: Fail to create object with invalid JSON
    Given I want to create a new object
    And I provide invalid JSON data: "{\"type\": \"DEVICE\", \"alias\":}"
    When I submit a POST request with the invalid JSON
    Then I should receive a 4xx client error response

  Scenario: Successfully update an existing object
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists an object with system ID "SYSTEM" and object ID "obj123"
    And I want to update the object with type "UPDATED_DEVICE"
    When I submit a PUT request to update the object
    Then I should receive a successful response

  Scenario: Fail to update object with missing parameters
    Given there exists an object with system ID "SYSTEM" and object ID "obj123"
    And I have valid update data
    When I submit a PUT request without required user parameters
    Then I should receive a 4xx client error response

  # === Relationship Management ===

  Scenario: Successfully bind objects as parent and child
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And there exists a child object with system ID "SYSTEM" and object ID "child123"
    When I bind the child object to the parent object
    Then I should receive a successful response
    And the objects should be bound as parent-child relationship

  Scenario: Fail to bind objects with missing parameters
    Given there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And there exists a child object with system ID "SYSTEM" and object ID "child123"
    When I attempt to bind objects without providing required user parameters
    Then I should receive a 4xx client error response

  Scenario: Fail to bind objects with invalid JSON
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And I provide invalid JSON data: "{\"childId\": {\"objectId\": \"child123\", \"systemID\":}}"
    When I attempt to bind objects with invalid JSON
    Then I should receive a 4xx client error response

  Scenario: Bind objects with null child object ID (current behavior)
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And I want to bind a child with null object ID but valid system ID "SYSTEM"
    When I attempt to bind the objects
    Then I should receive a successful response
    And the system should handle the null object ID gracefully

  Scenario: Bind objects with null child ID should fail (future validation)
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And I want to bind a child with null child ID
    When I attempt to bind the objects
    Then I should receive a 400 Bad Request response
    And the error message should indicate invalid child ID

  Scenario: Bind objects with null child system ID should fail (future validation)
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there exists a parent object with system ID "SYSTEM" and object ID "parent123"
    And I want to bind a child with object ID "child123" but null system ID
    When I attempt to bind the objects
    Then I should receive a 400 Bad Request response
    And the error message should indicate invalid system ID

  # === Endpoint Verification ===

  Scenario Outline: Verify all API endpoints are correctly mapped
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And the system has appropriate test data configured
    When I make a GET request to "<endpoint>"
    Then I should receive a successful response
    And the endpoint should be correctly mapped

    Examples:
      | endpoint                                           |
      | /ambient-intelligence/objects                     |
      | /ambient-intelligence/objects/SYSTEM/obj123       |
      | /ambient-intelligence/objects/search/byAlias/test |
