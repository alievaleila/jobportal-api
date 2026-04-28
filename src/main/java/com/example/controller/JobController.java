package com.example.controller;

import com.example.dto.JobCreateDto;
import com.example.dto.JobResponseDto;
import com.example.dto.JobSearchRequestDto;
import com.example.service.JobService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    private Bucket resolveBucket(HttpServletRequest request) {
        String key = request.getRemoteAddr();
        return buckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponseDto>> searchJobs(
            @Valid @ModelAttribute JobSearchRequestDto request,
            HttpServletRequest httpRequest) {
        if (resolveBucket(httpRequest).tryConsume(1)) {
            return ResponseEntity.ok(jobService.searchJobs(request));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/create")
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobCreateDto createDto) {
        JobResponseDto response = jobService.createJob(createDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
