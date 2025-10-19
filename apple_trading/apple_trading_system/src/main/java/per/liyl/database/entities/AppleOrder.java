package per.liyl.database.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "apple_order")
public class AppleOrder {

    /**
     * 订单 ID
     */
    @Id
    private Long orderId;

    /**
     * 订单名称
     */
    @Column(nullable = false, length = 128)
    private String orderName;

    /**
     * 订单数量
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 计量单位
     */
    @Column(nullable = false, length = 8)
    private String unit;

    /**
     * 单价
     */
    @Column(nullable = false)
    private Double unitPrice;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(nullable = false)
    private Date updateTime;

    // 测试用
    public AppleOrder() {
    }

    public AppleOrder(Long orderId) {
        this.orderId = orderId;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
