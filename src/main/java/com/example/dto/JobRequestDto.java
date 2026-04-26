package com.example.dto;

import com.example.enums.JobType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record JobRequestDto(
        String keyword,
        String location,
        JobType jobType,

        @Min(value = 0, message = "Page index cannot be negative")
        Integer page,

        @Min(value = 1, message = "Size must be at least 1")
        @Max(value = 100, message = "Size cannot exceed 100")
        Integer size
) {
    public JobRequestDto {
        if (page == null) page = 0;
        if (size == null) size = 10;
    }
}
