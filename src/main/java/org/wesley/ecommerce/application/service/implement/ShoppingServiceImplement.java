package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Shopping;
import org.wesley.ecommerce.application.domain.repository.ShoppingRepository;
import org.wesley.ecommerce.application.service.ShoppingService;

import java.util.NoSuchElementException;

@Service
public class ShoppingServiceImplement implements ShoppingService {
    private final ShoppingRepository shoppingRepository;

    public ShoppingServiceImplement(ShoppingRepository shoppingRepository) {
        this.shoppingRepository = shoppingRepository;
    }

    @Override
    public Shopping create(Shopping shopping) {
        return shoppingRepository.save(shopping);
    }

    @Override
    public Shopping findById(Long id) {
        return shoppingRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
