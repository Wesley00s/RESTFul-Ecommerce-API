package org.wesley.ecommerce.application.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PRODUCT_RATING_UPDATED_QUEUE = "product.rating.updated.queue";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue productRatingUpdatedQueue() {
        return new Queue(PRODUCT_RATING_UPDATED_QUEUE);
    }
}