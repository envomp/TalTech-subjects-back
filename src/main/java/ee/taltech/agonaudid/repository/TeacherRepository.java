package ee.taltech.agonaudid.repository;

import ee.taltech.agonaudid.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	Optional<Teacher> findByName(@Param("name") String name);
}
