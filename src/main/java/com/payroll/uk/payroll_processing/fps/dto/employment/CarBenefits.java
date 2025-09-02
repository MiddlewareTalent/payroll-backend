package com.payroll.uk.payroll_processing.fps.dto.employment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CarBenefits {
    private String carModel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate carFirstRegisteredDate;
    private String carCO2;
    private Integer carZeroEmissionMileage;
    private String carFuelType;
    private BigDecimal carIdentifier;
    private String carAmendmentIndicator;
    private BigDecimal carPrice;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate carAvailableFromDate;
    private BigDecimal carCashEquivalent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate carAvailableTo;
    //Fuel Benefits
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate freeFuelProvidedDate;
    private BigDecimal freeFuelCashEquivalent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate freeFuelWithDrawnDate;

}
