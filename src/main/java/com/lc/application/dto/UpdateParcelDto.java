package com.lc.application.dto;

import com.lc.application.model.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateParcelDto {
    private Long id;

    private DeliveryStatus status;

    private Boolean isPaid;
}
