Feature: Check product apis

  Background:
    Given the app has started
    And configure http headers to:
      | Accept       | application/json |
      | Content-Type | application/json |
    And configure kafka-timeout to 5000
    And register entity "com.jinternals.demo.domain.Product" as "Product"
    And register event "com.jinternals.demo.domain.events.ProductCreatedEvent" as "ProductCreatedEvent"

  Scenario: Save Product Information
    When the client invokes POST "/api/product" with:
    """
    { "name":  "some-product-1", "type": "FOOD", "description": "some-description-1" }
    """
    Then api respond with status 201
    And "$.id" should not be "null"
    And "$.name" should be "some-product-1"
    And "$.type" should be "FOOD"
    And verify "ProductCreatedEvent" is published on "product" topic with content:
    """
    {
      "id": "${json-unit.regex}[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
      "name": "some-product-1",
      "type": "FOOD",
      "description": "some-description-1"
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
    And "$.id" should not be "null"
    And "$.name" should be "some-product-x"
    And "$.type" should be "FOOD"

  Scenario: Get Product Information by id
    Given "Product" with following details exist in db:
    """
    {"id": "demo-id", "name":  "demo-product", "type": "FOOD" }
    """
    When the client invokes GET "/api/product/demo-id"
    Then api respond with status 200
    Then "$.id" should be "demo-id"
    Then "$.name" should be "demo-product"
    Then "$.type" should be "FOOD"

