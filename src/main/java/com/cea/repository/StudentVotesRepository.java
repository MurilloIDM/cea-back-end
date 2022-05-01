package com.cea.repository;

import com.cea.models.ExclusivePost;
import com.cea.models.PollTopics;
import com.cea.models.Student;
import com.cea.models.StudentVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentVotesRepository extends JpaRepository<StudentVotes, UUID> {

    List<StudentVotes> findAllByExclusivePostAndStudent(ExclusivePost exclusivePost, Student student);
    StudentVotes findByStudentAndPollTopics(Student student, PollTopics pollTopics);

}
