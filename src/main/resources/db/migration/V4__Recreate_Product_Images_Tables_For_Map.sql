DROP TABLE product_image_urls;

CREATE TABLE product_images
(
    product_id      BIGINT       NOT NULL,
    image_public_id VARCHAR(255) NOT NULL,
    image_url       VARCHAR(255),
    PRIMARY KEY (product_id, image_public_id)
);

ALTER TABLE product_images
    ADD CONSTRAINT fk_product_images_on_product FOREIGN KEY (product_id) REFERENCES product (id);