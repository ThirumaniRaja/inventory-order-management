package com.guvi.inventory.exception;

/**
 * Exception thrown when a user account is inactive or disabled
 */
public class UserAccountException extends RuntimeException {
    private final Long userId;
    private final String reason;

    public UserAccountException(Long userId, String reason) {
        super(String.format("User account issue for user ID: %d. Reason: %s", userId, reason));
        this.userId = userId;
        this.reason = reason;
    }

    public UserAccountException(String message) {
        super(message);
        this.userId = null;
        this.reason = null;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }
}

