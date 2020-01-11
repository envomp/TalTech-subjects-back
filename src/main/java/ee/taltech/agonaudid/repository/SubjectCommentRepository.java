package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.SubjectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectCommentRepository extends JpaRepository<SubjectComment, Long> {

	List<SubjectComment> findBySubjectId(@Param("subject_id") long id);
}
