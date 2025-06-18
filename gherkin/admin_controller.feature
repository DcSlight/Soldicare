Feature: Admin API management
  Admin users can manage users, objects, and commands through HTTP endpoints.

  Background:
    Given the admin is authenticated with system ID "ADMIN_SYSTEM" and email "admin@example.com"

  Scenario: Delete all users
    When the admin sends a DELETE request to "/ambient-intelligence/admin/users"
    Then the response status should be 200

  Scenario: Delete all commands
    When the admin sends a DELETE request to "/ambient-intelligence/admin/commands"
    Then the response status should be 200

  Scenario: Delete all objects
    When the admin sends a DELETE request to "/ambient-intelligence/admin/objects"
    Then the response status should be 200

  Scenario: Export all users with pagination
    And the following users exist:
      | email             | role      | username | avatar        |
      | user1@example.com | END_USER  | user1    | avatar1.png   |
      | user2@example.com | OPERATOR  | user2    | avatar2.png   |
    When the admin sends a GET request to "/ambient-intelligence/admin/users" with parameters:
      | size | 10 |
      | page | 0  |
    Then the response status should be 200
    And the response should be a JSON array with 2 users
    And user 1 should have email "user1@example.com" and role "END_USER"
    And user 2 should have email "user2@example.com" and role "OPERATOR"

  Scenario: Export all users with default pagination
    And no users exist
    When the admin sends a GET request to "/ambient-intelligence/admin/users" without pagination parameters
    Then the response status should be 200
    And the response should be a JSON array with 0 users

  Scenario: Export all commands with pagination
    And 1 command exists
    When the admin sends a GET request to "/ambient-intelligence/admin/commands" with parameters:
      | size | 5 |
      | page | 1 |
    Then the response status should be 200
    And the response should be a JSON array with 1 command

  Scenario: Attempt to delete users with unauthorized credentials
    Given the admin is authenticated with system ID "INVALID_SYSTEM" and email "notadmin@example.com"
    When the admin sends a DELETE request to "/ambient-intelligence/admin/users"
    Then the response status should be 200

  Scenario: Attempt to delete users with missing parameters
    When the admin sends a DELETE request to "/ambient-intelligence/admin/users" without parameters
    Then the response status should be a 4xx client error

  Scenario: Export users with invalid pagination parameters
    When the admin sends a GET request to "/ambient-intelligence/admin/users" with parameters:
      | size | -1 |
      | page | -1 |
    Then the response status should be 200
    And the response should be a JSON array (possibly empty)
