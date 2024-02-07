package com.lc.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lc.application.model.Parcel;

public interface ParcelRepository extends JpaRepository<Parcel, Long>, JpaSpecificationExecutor<Parcel> {

}
