package com.esliceyament.shared.payload;

public class OrderedStockUpdate {
    private Long productCode;
    private Integer quantity;
    private String selectedAttributes;

    public OrderedStockUpdate(Long productCode, Integer quantity, String selectedAttributes) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.selectedAttributes = selectedAttributes;
    }

    public OrderedStockUpdate() {}


    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSelectedAttributes() {
        return selectedAttributes;
    }

    public void setSelectedAttributes(String selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
    }
}
