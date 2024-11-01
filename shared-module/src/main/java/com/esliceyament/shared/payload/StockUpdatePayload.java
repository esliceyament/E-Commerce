package com.esliceyament.shared.payload;

public class StockUpdatePayload {

    private Long productCode;
    private Integer totalStock;

    public StockUpdatePayload() {}

    public StockUpdatePayload(Long productCode, Integer totalStock) {
        this.productCode = productCode;
        this.totalStock = totalStock;
    }

    // Getters and Setters
    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }
}
