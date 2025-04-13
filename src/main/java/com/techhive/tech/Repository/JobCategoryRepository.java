package com.techhive.tech.Repository;

import com.techhive.tech.Models.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {

    Optional<JobCategory> findByCategory(String category);
    Optional<JobCategory> findByCategoryId(Integer categoryId);
}
