package com.vmv.agromart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleVisitRequest {

    @NotNull
    private LocalDate visitDate;

    private String visitNotes;
}
