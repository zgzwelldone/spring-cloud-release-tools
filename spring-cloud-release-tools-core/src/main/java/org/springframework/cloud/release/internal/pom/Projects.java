package org.springframework.cloud.release.internal.pom;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstraction over collection of projects
 *
 * @author Marcin Grzejszczak
 */
public class Projects extends HashSet<ProjectVersion> {

	public Projects(Set<ProjectVersion> versions) {
		addAll(versions);
	}

	@SuppressWarnings("unchecked")
	public Projects(ProjectVersion... versions) {
		addAll(new HashSet<>(Arrays.asList(versions)));
	}

	public void remove(String projectName) {
		ProjectVersion projectVersion = forName(projectName);
		remove(projectVersion);
	}

	public ProjectVersion forFile(File projectRoot) {
		final ProjectVersion thisProject = new ProjectVersion(projectRoot);
		return this.stream().filter(projectVersion -> projectVersion.projectName.equals(thisProject.projectName))
				.findFirst()
				.orElseThrow(() -> exception(thisProject.projectName));
	}

	private static IllegalStateException exception(String projectName) {
		return new IllegalStateException("Project with name [" + projectName + "] is not present. "
				+ "Either put it in the Spring Cloud Release project or set it via the [--releaser.fixed-versions[" + projectName + "]=1.0.0.RELEASE] property");
	}

	public ProjectVersion forName(String projectName) {
		return this.stream().filter(projectVersion -> projectVersion.projectName.equals(projectName))
				.findFirst()
				.orElseThrow(() -> exception(projectName));
	}

	public boolean containsSnapshots() {
		return this.stream().anyMatch(ProjectVersion::isSnapshot);
	}
}
