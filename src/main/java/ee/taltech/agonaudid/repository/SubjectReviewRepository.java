package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.SubjectReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectReviewRepository extends JpaRepository<SubjectReview, Long> {

	List<SubjectReview> findBySubjectId(@Param("subject_id") long id);
}
