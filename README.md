
# RESTFul E-commerce System API

This repository contains the source code for an e-commerce system developed in Java with Spring Boot. The system allows users to view products, place orders, manage shopping carts, and make payments.

## Class Diagram

```mermaid
classDiagram
    class User {
        +UUID userId
        +String name
        +String email
        +String password
        +UserType userType
        +LocalDateTime createdAt
        +Address address
        +List<Cart> carts
    }

    class Address {
        +Long id
        +String street
        +String city
        +String state
        +String zip
    }

    class Cart {
        +Long id
        +boolean isActive
        +User user
        +List<Product> products
    }

    class Order {
        +Long id
        +LocalDateTime createdAt
        +OrderStatus status
        +Cart cart
    }

    class Product {
        +Long id
        +String code
        +String name
        +String description
        +BigDecimal price
        +Integer stockQuantity
        +String imageUrl
        +ProductCategory category
    }

    class UserType {
        CUSTOMER
        ADMIN
    }

    class OrderStatus {
        CREATED
        COMPLETED
        CANCELLED
    }

    class ProductCategory {
        SMARTPHONE
        PC
        TABLETS
        TV
        HOME_APPLIANCES
        ACCESSORY
    }

    %% Relationships
    User "1" --> "1" Address : has
    User "1" --> "*" Cart : has
    Cart "1" --> "*" Product : contains
    Cart "1" --> "1" User : belongs to
    Cart "1" --> "1" Order : part of
    Order "1" --> "1" Cart : belongs to
```

## System Flow

### 1. User Authentication and Registration

- **User Registration**: The system allows new users to register by sending a `POST` request to the `/register` endpoint. Registration includes information such as name, email, password, and address. The system returns a confirmation message after successful registration.

- **User Login**: After registration, users can log in with their credentials by sending a `POST` request to `/auth`. The system returns a JWT token that will be used to authenticate the user in subsequent interactions with the API.

### 2. Product Management

- **List Products**: The user can view the list of all products available in the system by sending a `GET` request to `/product/products`. The system returns a list of products with details such as name, price, and category.

- **Product Details**: To view detailed information about a specific product, the user can send a `GET` request to `/product/{id}`.

- **Product Administration**: Administrators have permissions to create new products (`POST /product`), update product information (`PUT /product/update/{id}`), and remove products from the system (`DELETE /product/delete/{id}`).

### 3. Shopping Cart

- **Create Cart**: The system allows the user to create a shopping cart associated with their profile by sending a `POST` request to `/cart`. A cart is created empty and is active until it is disabled or completed.

- **Add Products to Cart**: The user can add products to the existing cart by sending a `POST` request to `/cart/add/{cartId}/{productId}`, where the parameters are the cart ID and the product ID.

- **View Cart**: The user can check the items in their active cart by sending a `GET` request to `/cart/{cartId}`.

- **Disable Cart**: If the user no longer wants to use a cart, they can disable it by sending a `PUT` request to `/cart/disabled/{cartId}`.

### 4. Order Processing

- **Create Order**: When the user wishes to finalize a purchase, they can create an order based on an active cart by sending a `POST` request to `/order`. The system checks the stock quantity of each product, and if available, creates the order with an initial status of `CREATED`.

- **Track Order**: The order can change its status to `COMPLETED` when payment is made or `CANCELLED` if the user opts to cancel the purchase.

### 5. User Management

- **Update User**: The user can update their personal information, such as email or address, by sending a `PUT` request to `/user/update/{id}`.

- **View Users**: Administrators can list all registered users by sending a `GET` request to `/user/users`, or search for a specific user by ID (`GET /user/{id}`) or email (`GET /user/email/{email}`).

### 6. Authorization and Permissions

- The system uses OAuth to protect routes and verify if the user is authenticated before accessing or modifying resources. Only authorized users can view or edit products, carts, and orders.

### 7. API Call Examples

#### User Registration

```json
POST /register
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "strongpassword",
    "street": "123 Main St",
    "city": "Somewhere",
    "state": "CA",
    "zip": "12345"
}
```

#### User Login

```json
POST /auth
{
    "email": "john.doe@example.com",
    "password": "strongpassword"
}
```

#### List Products

```json
GET /product/products
```

#### Create Order

```json
POST /order
{
    "cartId": 1,
    "productId": 2
}
```

### Final Remarks

This system utilizes token-based JWT authentication, ensuring the security of operations. The shopping flow is integrated with product management, carts, and orders, providing an efficient and organized experience for end-users and administrators.
