package com.example.service;

import com.example.dto.JobCreateDto;
import com.example.dto.JobRequestDto;
import com.example.dto.JobResponseDto;
import org.springframework.data.domain.Page;

public interface JobService {

    Page<JobResponseDto> searchJobs(JobRequestDto request);

    void createJob(JobCreateDto dto);
}