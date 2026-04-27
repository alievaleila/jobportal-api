package com.example.controller;

import com.example.dto.JobCreateDto;
import com.example.dto.JobRequestDto;
import com.example.dto.JobResponseDto;
import com.example.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponseDto>> searchJobs(@Valid JobRequestDto request) {
        var results = jobService.searchJobs(request);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createJob(@Valid @RequestBody JobCreateDto createDto) {
        jobService.createJob(createDto);
        return ResponseEntity.status(201).build();
    }
}
