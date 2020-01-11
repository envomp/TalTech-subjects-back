package ee.taltech.agonaudid.service;

import ee.taltech.agonaudid.domain.Role;
import ee.taltech.agonaudid.domain.Subject;
import ee.taltech.agonaudid.domain.User;
import ee.taltech.agonaudid.exception.UserNotFoundException;
import ee.taltech.agonaudid.pojo.api.users.UserPostDto;
import ee.taltech.agonaudid.repository.SubjectRepository;
import ee.taltech.agonaudid.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final UserRepository userRepository;
	private final SubjectRepository subjectRepository;

	public UserService(UserRepository userRepository, SubjectRepository subjectRepository) {
		this.userRepository = userRepository;
		this.subjectRepository = subjectRepository;
	}

	public void addSuperUser(UserPostDto user) {
		User user1 = new User(user.getUsername(), user.getPassword());
		user1.setRole(Role.ADMIN);
		User savedUser = userRepository.save(user1);
		LOG.info(savedUser.toString() + " successfully saved into DB as admin");
	}

	public long addUser(UserPostDto user) {
		User savedUser = userRepository.save(new User(user.getUsername(), user.getPassword()));
		LOG.info(savedUser.toString() + " successfully saved into DB");
		return savedUser.getId();
	}

	public User getUser(long id) {
		return userRepository
				.findById(id)
				.map(
						user -> {
							LOG.info("Reading user with id " + id + " from database.");
							return user;
						})
				.orElseThrow(
						() -> {
							LOG.error(String.format("User with id %d was not found.", id));
							return new UserNotFoundException("The user with the id " + id + " couldn't be found in the database.");
						});
	}

	public long getHome(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			LOG.error(String.format("User with username %s was not found.", username));
			throw new UserNotFoundException(String.format("User with username %s was not found.", username));
		}
		return user.get().getId();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// TODO: add handlers for when no user with such ID
	public List<Long> getSubscriptions(long id) {
		return userRepository.getOne(id).getSubjects().stream().map(Subject::getId).collect(Collectors.toList());
	}

	// TODO: add handlers for when no user with such ID or subject with such ID
	@Transactional
	public Long addSubscription(long id, Long subject_id) {
		Subject subject = subjectRepository.getOne(subject_id);
		userRepository.getOne(id).addSubject(subject);
		return subject_id;
	}

	// TODO: add handlers for when no user with such ID or subject with such ID or such subscription
	@Transactional
	public void removeSubscription(long id, Long subject_id) {
		Subject subject = subjectRepository.getOne(subject_id);
		userRepository.getOne(id).removeSubject(subject);
	}

	@Transactional
	public void removeUser(String username) {
		userRepository.deleteByUsername(username);
	}
}
