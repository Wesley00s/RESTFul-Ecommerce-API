CREATE TABLE address
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    street VARCHAR(255),
    city   VARCHAR(255),
    state  VARCHAR(255),
    zip    VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE cart
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id     UUID,
    total_price DOUBLE PRECISION,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    cart_id    BIGINT,
    product_id BIGINT,
    quantity   INTEGER                                 NOT NULL,
    price      DOUBLE PRECISION                        NOT NULL,
    order_id   BIGINT,
    status     SMALLINT,
    CONSTRAINT pk_cartitem PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    order_id   BIGINT,
    product_id BIGINT,
    quantity   INTEGER,
    price      DOUBLE PRECISION,
    status     SMALLINT,
    CONSTRAINT pk_orderitem PRIMARY KEY (id)
);

CREATE TABLE order_shopping
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id    UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    status     SMALLINT,
    CONSTRAINT pk_ordershopping PRIMARY KEY (id)
);

CREATE TABLE product
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    image_url   VARCHAR(255),
    code        VARCHAR(255),
    description VARCHAR(10000),
    stock       INTEGER,
    category    VARCHAR(255),
    price       DOUBLE PRECISION                        NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         UUID         NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255),
    user_type  SMALLINT,
    address_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT uc_cart_user UNIQUE (user_id);

ALTER TABLE product
    ADD CONSTRAINT uc_product_code UNIQUE (code);

ALTER TABLE users
    ADD CONSTRAINT uc_users_address UNIQUE (address_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES order_shopping (id);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES order_shopping (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDERITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_shopping
    ADD CONSTRAINT FK_ORDERSHOPPING_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);