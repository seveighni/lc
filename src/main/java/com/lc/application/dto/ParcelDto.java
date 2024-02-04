package com.lc.application.dto;

import com.lc.application.model.Parcel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParcelDto {
	private long id;

	// TODO
	public ParcelDto(Parcel parcel) {
		this.id = parcel.getId();
	}
}
