ALTER TABLE product
    DROP COLUMN category;

ALTER TABLE product
    ADD COLUMN category_id BIGINT REFERENCES product_category (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES product_category (id);