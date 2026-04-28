package com.example.service;

import com.example.dto.JobCreateDto;
import com.example.dto.JobResponseDto;
import com.example.dto.JobSearchRequestDto;
import org.springframework.data.domain.Page;

public interface JobService {

    Page<JobResponseDto> searchJobs(JobSearchRequestDto request);

    JobResponseDto createJob(JobCreateDto dto);
}