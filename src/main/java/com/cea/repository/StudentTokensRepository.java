package com.cea.repository;

import com.cea.models.Student;
import com.cea.models.StudentTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentTokensRepository extends JpaRepository<StudentTokens, UUID> {

    List<StudentTokens> findDistinctByTokenAndStudentOrderByExpiresDateDesc(String token, Student student);

}
