package com.example.repository.specification;


import com.example.entity.Job;
import com.example.enums.JobType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class JobSpecification {

    private JobSpecification() {
    }

    private static Specification<Job> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) return null;

            String tsQuery = keyword.trim().replace(" ", " & ");
            return cb.isTrue(
                    cb.function("to_tsvector", Boolean.class,
                            cb.literal("english"),
                            root.get("title")
                    ).in(
                            cb.function("to_tsquery", Object.class,
                                    cb.literal("english"),
                                    cb.literal(tsQuery)
                            )
                    )
            );
        };
    }

    private static Specification<Job> hasLocation(String location) {
        return (root, query, cb) ->
                StringUtils.hasText(location) ? cb.equal(root.get("location"), location) : null;
    }

    private static Specification<Job> hasJobType(JobType jobType) {
        return (root, query, cb) ->
                jobType == null ? null : cb.equal(root.get("jobType"), jobType);
    }

    public static Specification<Job> build(String keyword, String location, JobType jobType) {
        return Specification.where(hasKeyword(keyword))
                .and(hasLocation(location))
                .and(hasJobType(jobType));
    }
}