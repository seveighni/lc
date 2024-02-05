package com.lc.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateParcelDto {
    private String from;
    private String to;
    private String address;
    private BigDecimal weight;

    private Long rateId;

    private Long officeId;
    private Boolean isPaid;
}
