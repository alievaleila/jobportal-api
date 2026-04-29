package com.example.controller;

import com.example.dto.JobCreateDto;
import com.example.dto.JobResponseDto;
import com.example.dto.JobSearchRequestDto;
import com.example.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponseDto>> searchJobs(
            @Valid @ModelAttribute JobSearchRequestDto request) {
        return ResponseEntity.ok(jobService.searchJobs(request));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobCreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(createDto));
    }


}
