Feature: Check application health check
  Scenario: Validate application health
    Given the app has started
    When the client invokes GET "/actuator/health"
    Then "$.status" should be "UP"
