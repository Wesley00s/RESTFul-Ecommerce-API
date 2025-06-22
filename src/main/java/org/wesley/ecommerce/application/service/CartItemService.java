package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.CartItem;

@Service
public interface CartItemService {

    CartItem findById(Long id);

    CartItem update(Long cartItemId, CartItem cartItem);
}
