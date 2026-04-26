package com.example.service;

import com.example.dto.JobRequestDto;
import com.example.dto.JobResponseDto;
import org.springframework.data.domain.Page;

public interface JobService {

    Page<JobResponseDto> searchJobs(JobRequestDto request);
}