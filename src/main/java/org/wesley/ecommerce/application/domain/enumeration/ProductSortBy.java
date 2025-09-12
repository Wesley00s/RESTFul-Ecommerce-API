package org.wesley.ecommerce.application.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductSortBy {
    NAME("name"),
    PRICE("price"),
    RATING("rating"),
    SOLD_COUNT("soldCount");

    private final String propertyName;
}