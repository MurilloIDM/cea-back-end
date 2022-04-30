package com.cea.repository;

import com.cea.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Student findByEmail(String email);
    Page<Student> findAllByStatusIsAndNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(
            boolean status,
            String name,
            String socialName,
            Pageable pageRequest
    );
    Page<Student> findAllByInactivationSoonTrueAndNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(
            String name,
            String socialName,
            Pageable pageRequest
    );
    Page<Student> findAllByNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(
            String name,
            String socialName,
            Pageable pageRequest
    );
    Page<Student> findAllByStatusIs(boolean status, Pageable pageRequest);
    Page<Student> findAllByInactivationSoonIsTrue(Pageable pageRequest);

}
