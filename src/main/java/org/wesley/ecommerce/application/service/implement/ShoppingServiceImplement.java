package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Shopping;
import org.wesley.ecommerce.application.domain.repository.ShoppingRepository;
import org.wesley.ecommerce.application.service.ShoppingService;

import java.util.NoSuchElementException;

/**
 * This class implements the {@link ShoppingService} interface. It provides methods for managing shopping operations.
 *
 * @author Wesley
 * @since 1.0
 */
@Service
public class ShoppingServiceImplement implements ShoppingService {
    private final ShoppingRepository shoppingRepository;

    /**
     * Constructs a new instance of {@link ShoppingServiceImplement} with the given {@link ShoppingRepository}.
     *
     * @param shoppingRepository the repository for managing shopping data
     */
    public ShoppingServiceImplement(ShoppingRepository shoppingRepository) {
        this.shoppingRepository = shoppingRepository;
    }

    /**
     * Creates a new shopping record in the database.
     *
     * @param shopping the shopping object to be created
     * @return the created shopping object
     */
    @Override
    public Shopping create(Shopping shopping) {
        return shoppingRepository.save(shopping);
    }

    /**
     * Retrieves a shopping record from the database by its ID.
     *
     * @param id the ID of the shopping record to be retrieved
     * @return the shopping object with the specified ID
     * @throws NoSuchElementException if no shopping record is found with the given ID
     */
    @Override
    public Shopping findById(Long id) {
        return shoppingRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
