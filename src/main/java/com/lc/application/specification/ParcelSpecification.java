package com.lc.application.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.lc.application.model.Parcel;

public class ParcelSpecification {
    public static Specification<Parcel> orderDateInRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            return cb.between(root.get("orderDate"), startDate, endDate);
        };
    }

    public static Specification<Parcel> hasSenderId(Long senderId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("sender").get("id"), senderId);
        };
    }

    public static Specification<Parcel> hasReceiverId(Long receiverId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("receiver").get("id"), receiverId);
        };
    }

    public static Specification<Parcel> hasEmployeeResponsible(String email) {
        return (root, query, cb) -> {
            return cb.equal(root.get("registeredBy").get("user").get("email"), email);
        };
    }

    public static Specification<Parcel> hasSender(String email) {
        return (root, query, cb) -> {
            return cb.equal(root.get("sender").get("user").get("email"), email);
        };
    }

    public static Specification<Parcel> hasReceiver(String email) {
        return (root, query, cb) -> {
            return cb.equal(root.get("receiver").get("user").get("email"), email);
        };
    }
}
