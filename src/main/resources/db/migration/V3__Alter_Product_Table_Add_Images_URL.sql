ALTER TABLE product
    RENAME COLUMN image_url TO cover_image_url;


CREATE TABLE product_image_urls
(
    product_id BIGINT NOT NULL,
    image_url  VARCHAR(255)
);

ALTER TABLE product_image_urls
    ADD CONSTRAINT fk_product_image_urls_on_product FOREIGN KEY (product_id) REFERENCES product (id);