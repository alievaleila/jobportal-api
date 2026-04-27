package com.example.dto;

import com.example.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record JobCreateDto (@NotBlank(message = "Job title is required")
                            String title,

                            @NotBlank(message = "Description is required")
                            String description,

                            @NotBlank(message = "Location is required")
                            String location,

                            @NotNull(message = "Job type is required")
                            JobType jobType,

                            @NotBlank(message = "Company name is required")
                            String companyName,

                            @Positive(message = "Salary must be positive")
                            Double salary
) {
}