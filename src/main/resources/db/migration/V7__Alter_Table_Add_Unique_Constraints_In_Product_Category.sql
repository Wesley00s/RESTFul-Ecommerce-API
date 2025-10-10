ALTER TABLE product_category
    ADD CONSTRAINT uc_productcategory_description UNIQUE (description);

ALTER TABLE product_category
    ADD CONSTRAINT uc_productcategory_name UNIQUE (name);