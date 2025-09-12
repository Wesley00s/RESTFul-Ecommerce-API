ALTER TABLE product
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE product
    ADD is_available BOOLEAN;

ALTER TABLE product
    ADD rating DOUBLE PRECISION DEFAULT 0.0;

ALTER TABLE product
    ADD sold_count INTEGER DEFAULT 0;

ALTER TABLE product
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE product
    ALTER COLUMN is_available SET NOT NULL;

ALTER TABLE product
    ALTER COLUMN rating SET NOT NULL;

ALTER TABLE product
    ALTER COLUMN sold_count SET NOT NULL;