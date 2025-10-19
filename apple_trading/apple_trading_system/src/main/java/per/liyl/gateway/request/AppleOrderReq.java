package per.liyl.gateway.request;

public class AppleOrderReq extends PageRequest{

    /**
     * 订单 ID
     */
    private Long orderId;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 订单数量
     */
    private Integer quantity;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 单价
     */
    private Double unitPrice;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
