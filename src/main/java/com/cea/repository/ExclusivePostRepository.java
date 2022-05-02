package com.cea.repository;

import com.cea.enums.TypeExclusivePost;
import com.cea.models.ExclusivePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExclusivePostRepository extends JpaRepository<ExclusivePost, UUID> {

    Page<ExclusivePost> findAllByFiledFalse(Pageable pageRequest);
    Page<ExclusivePost> findByTitleIgnoreCaseContainingAndTypeEqualsAndFiledFalse(
            String title,
            TypeExclusivePost type,
            Pageable pageRequest);
    Page<ExclusivePost> findByTitleIgnoreCaseContainingAndTypeEqualsAndStatusIsAndFiledFalse(
            String title,
            TypeExclusivePost type,
            Boolean status,
            Pageable pageRequest);
    Page<ExclusivePost> findByTitleIgnoreCaseContainingAndFiledFalse(String title, Pageable pageRequest);
    Page<ExclusivePost> findByTitleIgnoreCaseContainingAndStatusIsAndFiledFalse(
            String title,
            Boolean status,
            Pageable pageRequest);
    Page<ExclusivePost> findByTypeEqualsAndFiledFalse(TypeExclusivePost type, Pageable pageRequest);
    Page<ExclusivePost> findByTypeEqualsAndStatusIsAndFiledFalse(
            TypeExclusivePost type,
            Boolean status,
            Pageable pageRequest);
    Page<ExclusivePost> findAllByStatusTrueAndFiledFalse(Pageable pageRequest);
    Page<ExclusivePost> findAllByStatusIsAndFiledFalse(Boolean status, Pageable pageRequest);

}
