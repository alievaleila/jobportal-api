package com.example.dto;

import com.example.enums.JobType;

import java.time.LocalDateTime;

public record JobResponseDto(Long id,
                             String title,
                             String location,
                             JobType jobType,
                             String companyName,
                             LocalDateTime createdAt
) {}
