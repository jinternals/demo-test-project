Feature: Check product apis

  Background:
    Given the app has started
    And configure http headers to:
      | Accept       | application/json |
      | Content-Type | application/json |
    And register pojo "com.jinternals.demo.domain.Product" as "Product"

  Scenario: Save Product Information
    Given the client invokes POST "/api/product" with:
    """
    { "name":  "some-product-1", "type": "FOOD" }
    """
    Then api respond with status 201
    Then "$.id" should not be "null"
    Then "$.name" should be "some-product-1"
    Then "$.type" should be "FOOD"

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
