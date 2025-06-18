Feature: User Management
  As a system user
  I want to create, retrieve, and update user accounts
  So that I can manage access to the ambient intelligence system

  Background:
    Given the ambient intelligence system is running

  Scenario: Successfully create a new user
    Given I provide the following new user data:
      | field    | value            |
      | email    | test@example.com |
      | role     | END_USER         |
      | username | testuser         |
      | avatar   | avatar.png       |
    When I submit a POST request to "/ambient-intelligence/users"
    Then I should receive a successful response
    And the response should contain:
      | userId.email | test@example.com |
      | role         | END_USER         |
      | username     | testuser         |
      | avatar       | avatar.png       |

  Scenario: Successfully get user by ID
    Given a user exists with:
      | email    | test@example.com |
      | systemId | SYSTEM           |
      | role     | END_USER         |
      | username | testuser         |
      | avatar   | avatar.png       |
    When I send a GET request to "/ambient-intelligence/users/SYSTEM/test@example.com"
    Then I should receive a successful response
    And the response should contain:
      | userId.email | test@example.com |
      | role         | END_USER         |
      | username     | testuser         |

  Scenario: Successfully update an existing user
    Given I have a user with:
      | email    | test@example.com |
      | systemId | SYSTEM           |
    And I want to update the user with:
      | role     | ADMIN             |
      | username | updateduser       |
      | avatar   | updated_avatar.png |
    When I send a PUT request to "/ambient-intelligence/users/SYSTEM/test@example.com"
    Then I should receive a successful response

  Scenario: Fail to get non-existent user
    Given the user with email "nonexistent@example.com" and system ID "SYSTEM" does not exist
    When I send a GET request to "/ambient-intelligence/users/SYSTEM/nonexistent@example.com"
    Then I should receive a 4xx client error response
