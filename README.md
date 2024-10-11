# RESTFul E-commerce System API
This repository contains the source code for an e-commerce system developed in Java with Spring Boot. The system allows users to view products, place orders, manage order carts and make payments.

## Class Diagram

```mermaid
  classDiagram
    class User {
        +Long id
        +String name
        +String email
        +String password
        +UserType type
        +Address addresses
        +List<Order> orders
        +List<CartItem> cartItems
    }

    class Product {
        +Long id
        +String name
        +String description
        +Numeric price
        +Integer stockQuantity
        +Category category
    }

    class Order {
        +Long id
        +LocalDateTime orderDate
        +OrderStatus status
        +Numeric totalAmount
        +User user
        +List<OrderItem> orderItems
    }

    class OrderItem {
        +Long id
        +Product product
        +Integer quantity
        +Numeric unitPrice
        +Numeric subtotal
        +Order order
    }

    class CartItem {
        +Long id
        +Product product
        +Integr quantity
        +User user
    }

    class Address {
        +Long id
        +String street
        +String city
        +String state
        +String zipCode
        +User user
    }

    class Category {
        +Long id
        +String name
        +String description
        +List<Product> products
    }

    class UserType {
        CUSTOMER
        ADMINISTRATOR
    }

    class OrderStatus {
        AWAITING_PAYMENT
        PAID
        SHIPPED
        DELIVERED
        CANCELED
    }

    User "1" --> "*" Order : has
    User "1" --> "1" Address : has
    User "1" --> "*" CartItem : has
    Product "1" --> "0..*" OrderItem : is part of
    Product "1" --> "1" Category : belongs to
    Order "1" --> "*" OrderItem : contains

```
