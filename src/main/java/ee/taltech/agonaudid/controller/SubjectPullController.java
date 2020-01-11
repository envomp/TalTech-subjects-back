package ee.taltech.agonaudid.controller;

import ee.taltech.agonaudid.service.SubjectPullService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class SubjectPullController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final SubjectPullService subjectPullService;

	public SubjectPullController(SubjectPullService subjectPullService) {
		this.subjectPullService = subjectPullService;
	}

	@Transactional
	@EventListener(ApplicationReadyEvent.class)
	public void startupPullSubjects() {
		this.pullSubjects();
	}

	@Transactional
	@Scheduled(cron = "0 0 3 ? * SAT *")
	public void scheduledPullSubjects() {
		this.pullSubjects();
	}

	@Transactional
	void pullSubjects() {
		LOG.info("Starting updating subjects and teachers...");
		subjectPullService.pullSubjects();
		LOG.info("Finished updating subjects and teachers.");
	}
}
