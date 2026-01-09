package com.company.franchise.franchisemanagementapi.domain.exception;

public class InvalidStockException extends DomainException {

    public InvalidStockException(Integer stock) {
        super("Invalid stock value: " + stock + ". Stock must be non-negative.");
    }

    public InvalidStockException(String message) {
        super(message);
    }
}
