package com.backend.rentalBusiness.module.util;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

    @MappedSuperclass
    @Getter
    @Setter
    public abstract class BaseEntity {

    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(columnDefinition = "char(36)")
    // private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "active")
    private Boolean active = true;

}
