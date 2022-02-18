Feature: Check product apis

  Background:
    Given the app has started
    And configure http headers to:
      | Accept       | application/json |
      | Content-Type | application/json |
    And register entity "com.jinternals.demo.domain.Product" as "Product"
    And register event "com.jinternals.demo.domain.events.ProductCreatedEvent" as "ProductCreatedEvent"

  Scenario: Save Product Information
    Given the client invokes POST "/api/product" with:
    """
    { "name":  "some-product-1", "type": "FOOD", "description": "some-description-It" }
    """
    Then api respond with status 201
    And response payload is matching with:
     """
      {
        "id": "${json-unit.regex}[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
        "name": "some-product-1",
        "type": "FOOD",
        "description": "some-description-It"
      }
    """
    And "$.name" should be "some-product-1"
    And "$.type" should be "FOOD"
    And "ProductCreatedEvent" is published on "product" topic with payload:
    """
    {
      "id": "${json-unit.regex}[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
      "name": "some-product-1",
      "type": "FOOD",
      "description": "some-description-It"
    }
    """

  Scenario: Get Product Information by id
    Given the client invokes POST "/api/product" with:
    """
    { "name":  "some-product-x", "type": "FOOD" }
    """
    And save in scope "$.id" as "productId"
    When the client invokes GET "/api/product/{productId}"
    Then api respond with status 200
    Then "$.id" should not be "null"
    Then "$.name" should be "some-product-x"
    Then "$.type" should be "FOOD"
    And "ProductCreatedEvent" is published on "product" topic with payload:
    """
    {
      "id": "${json-unit.regex}[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
      "name": "some-product-x",
      "type": "FOOD",
      "description": null
    }
    """
