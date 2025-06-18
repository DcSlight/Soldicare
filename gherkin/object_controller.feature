Feature: Object Management
  As a system user
  I want to manage objects in the ambient intelligence system
  So that I can create, retrieve, update, and search for objects

  Background:
    Given the ambient intelligence system is running
    And I am authenticated as a valid user

  # Retrieve All Objects
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

  # Retrieve Specific Object
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

  # Create New Object
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

  # Update Existing Object
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

  # Search by Exact Alias
  Scenario: Successfully search objects by exact alias
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are objects with alias "TestAlias":
      | objectId | type   |
      | result1  | DEVICE |
      | result2  | SENSOR |
    When I search for objects with exact alias "TestAlias" using pagination size 5 and page 0
    Then I should receive a successful response
    And the response should contain 2 objects
    And all objects should match the exact alias "TestAlias"

  Scenario: Search with no results
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are no objects with alias "NonExistentAlias"
    When I search for objects with exact alias "NonExistentAlias"
    Then I should receive a successful response
    And the response should be an empty array

  # Search by Alias Pattern
  Scenario: Successfully search objects by alias pattern
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are objects matching the pattern "Test*":
      | objectId       | type   |
      | pattern_result | DEVICE |
    When I search for objects with alias pattern "Test*" using pagination size 10 and page 0
    Then I should receive a successful response
    And the response should contain 1 object
    And the object should match the pattern criteria

  # Search by Type
  Scenario: Successfully search objects by type
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are objects of type "DEVICE":
      | objectId | type   |
      | device1  | DEVICE |
      | device2  | DEVICE |
    When I search for objects with type "DEVICE" using pagination size 10 and page 0
    Then I should receive a successful response
    And the response should contain 2 objects
    And all objects should have type "DEVICE"

  # Search by Status
  Scenario: Successfully search objects by status
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are objects with status "ACTIVE":
      | objectId   | type   | status |
      | active_obj | DEVICE | ACTIVE |
    When I search for objects with status "ACTIVE" using pagination size 10 and page 0
    Then I should receive a successful response
    And the response should contain 1 object
    And all objects should have status "ACTIVE"

  # Search by Type and Status
  Scenario: Successfully search objects by type and status combination
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And there are objects matching both type "DEVICE" and status "ACTIVE":
      | objectId      | type   | status |
      | active_device | DEVICE | ACTIVE |
    When I search for objects with type "DEVICE" and status "ACTIVE" using pagination size 10 and page 0
    Then I should receive a successful response
    And the response should contain 1 object
    And the object should have type "DEVICE" and status "ACTIVE"

  # API Endpoint Verification
  Scenario Outline: Verify all API endpoints are correctly mapped
    Given I am a user with system ID "USER_SYSTEM" and email "user@example.com"
    And the system has appropriate test data configured
    When I make a GET request to "<endpoint>"
    Then I should receive a successful response
    And the endpoint should be correctly mapped

    Examples:
      | endpoint                                          |
      | /ambient-intelligence/objects                     |
      | /ambient-intelligence/objects/SYSTEM/obj123       |
      | /ambient-intelligence/objects/search/byAlias/test |
