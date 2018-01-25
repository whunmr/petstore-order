package com.thoughtworks.ddd.order.domain.order;

import com.thoughtworks.ddd.order.domain.common.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

import static javax.persistence.EnumType.STRING;

@javax.persistence.Entity(name = "pet_order")
public class Order implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private Customer customer;
    private Shop shop;
    private Pet pet;
    @Enumerated(STRING)
    private OrderStatus orderStatus = OrderStatus.NOT_COMPLETED;

    private String category;

    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now();

    public Order() {
    }

    public Order(Customer customer, Shop shop, Pet pet) {
        this.customer = customer;
        this.shop = shop;
        this.pet = pet;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }

    public Pet getPet() {
        return pet;
    }

    @Override
    public boolean sameIdentityAs(Long otherId) {
        return this.id.equals(otherId);
    }

    public void paid() {
        orderStatus = OrderStatus.PAID;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus status) {
        orderStatus = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean specialCategories() {
        return "Fish".equals(getCategory()) || "Ant".equals(getCategory());
    }

    public boolean inNotAllowToCancelStatus() {
        return getOrderStatus() == OrderStatus.CANCELLED || getOrderStatus() == OrderStatus.CLOSED;
    }

    public boolean notAllowToCancel() {
        //业务政策：蚂蚁，鱼等生命力不强的动物无法取消
        if (specialCategories()) {
            return true;
        }

        //如果订单已经取消 或 已经关闭，则不能继续取消
        return inNotAllowToCancelStatus();
    }
}
