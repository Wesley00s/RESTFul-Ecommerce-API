package org.wesley.ecommerce.application.listener;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.wesley.ecommerce.application.api.v1.controller.dto.event.ProductRatingUpdatedEvent;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;

@Component
@AllArgsConstructor
public class ProductEventListener {

    private final ProductRepository productRepository;

    @Transactional
    @RabbitListener(queues = "product.rating.updated.queue")
    public void onProductRatingUpdated(ProductRatingUpdatedEvent event) {
        productRepository.updateProductRating(
                event.newRating(),
                event.totalReviews(),
                event.productCode()
        );
    }
}