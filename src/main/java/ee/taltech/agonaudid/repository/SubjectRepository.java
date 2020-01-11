package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Optional<Subject> findByCode(@Param("code") String code);

	@Query(value = "SELECT id FROM subject", nativeQuery = true)
	List<Long> findAllOnlyIds();
}
