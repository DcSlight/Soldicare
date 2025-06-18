Feature: Command API invocation
  Users can invoke commands through the API and receive appropriate results or errors.

  Background:
    Given the following valid command:
      | command         | testCommand |
      | commandId       | cmd123      |
      | objectId        | obj123      |
      | objectSystem    | SYSTEM      |
      | userEmail       | user@example.com |
      | userSystem      | SYSTEM      |
      | priority        | high        |
      | timeout         | 30          |

  Scenario: Invoke command with a single result
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the response should be a JSON array with 1 item
    And the item should contain:
      | status  | Command received |
      | command | testCommand      |

  Scenario: Invoke command with an empty result
    Given the service returns an empty result
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the response should be a JSON array with 0 items

  Scenario: Invoke command with multiple result objects
    Given the service returns the following result objects:
      | id   | type   |
      | obj1 | DEVICE |
      | obj2 | SENSOR |
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the response should be a JSON array with 2 items
    And item 1 should contain:
      | id   | obj1   |
      | type | DEVICE |
    And item 2 should contain:
      | id   | obj2   |
      | type | SENSOR |

  Scenario: Invoke command with null command name
    Given the command name is null
    And the service returns an error message:
      | error | Invalid command |
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the response should be a JSON array with 1 item
    And the item should contain:
      | error | Invalid command |

  Scenario: Invoke command with invalid JSON
    Given the user sends malformed JSON:
      """
      {"command": "test", "invalidField":}
      """
    When the user sends a POST request to "/ambient-intelligence/commands"
    Then the response status should be a 4xx client error

  Scenario: Invoke command without content type header
    Given a valid command
    When the user sends a POST request to "/ambient-intelligence/commands" without a content type header
    Then the response status should be a 4xx client error

  Scenario: Invoke command with complex command attributes
    Given the command has the following complex attributes:
      | temperature       | 25.5                      |
      | coordinates       | [10.0, 20.0]              |
      | metadata.source   | sensor                    |
    And the service returns a result:
      | status | Complex command executed |
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the item should contain:
      | status | Complex command executed |

  Scenario: Endpoint mapping for basic ping command
    Given a command with:
      | command | ping |
    And the service returns:
      | 0 | pong |
    When the user sends a POST request to "/ambient-intelligence/commands" with the command
    Then the response status should be 200
    And the response should be a JSON array with 1 item
    And the first item should be "pong"