package com.guvi.inventory.exception;

/**
 * Exception thrown when insufficient inventory is available
 */
public class InsufficientInventoryException extends RuntimeException {
    private final Long productId;
    private final Long requestedQuantity;
    private final Long availableQuantity;

    public InsufficientInventoryException(Long productId, Long requestedQuantity, Long availableQuantity) {
        super(String.format(
            "Insufficient inventory for product ID: %d. Requested: %d, Available: %d",
            productId, requestedQuantity, availableQuantity
        ));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public InsufficientInventoryException(String message) {
        super(message);
        this.productId = null;
        this.requestedQuantity = null;
        this.availableQuantity = null;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getRequestedQuantity() {
        return requestedQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }
}

