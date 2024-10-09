package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Shopping;


@Service
public interface ShoppingService {
    Shopping create(Shopping shopping);
    Shopping findById(Long id);

}
