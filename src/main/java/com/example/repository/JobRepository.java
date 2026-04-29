package com.example.repository;

import com.example.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    @Query(value = """
            SELECT * FROM jobs 
            WHERE (:keyword IS NULL OR 
                   to_tsvector('english', title || ' ' || description) @@ plainto_tsquery('english', :keyword))
            AND (:location IS NULL OR location = :location)
            AND (:jobType IS NULL OR job_type = :jobType)
            """, nativeQuery = true)
    Page<Job> fullTextSearch(@Param("keyword") String keyword,
                             @Param("location") String location,
                             @Param("jobType") String jobType,
                             Pageable pageable);
}
