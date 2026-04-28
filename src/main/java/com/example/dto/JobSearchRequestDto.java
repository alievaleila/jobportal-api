package com.example.dto;

import com.example.enums.JobType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record JobSearchRequestDto(
        String keyword,
        String location,
        JobType jobType,

        @Min(0) Integer page,
        @Min(1) @Max(100) Integer size,
        String sortBy
) {
    public JobSearchRequestDto {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortBy == null) sortBy = "createdAt";
    }
}