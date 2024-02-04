package com.lc.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Parcel;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {

	Page<Parcel> findAllBySenderId(PageRequest paging, Long senderId);

	Page<Parcel> findAllByReceiverId(PageRequest paging, Long recieverId);

	Page<Parcel> findAllByRegisteredById(PageRequest paging, Long employeeId);

}
