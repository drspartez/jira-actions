package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.PROJECT_SUMMARY
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.memories.Project
import com.atlassian.performance.tools.jiraactions.api.memories.ProjectMemory
import com.atlassian.performance.tools.jiraactions.memories.ProjectMemory2Adapter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ProjectSummaryAction(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val projectMemory: Memory2<Project>
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    @Deprecated("Use constructor(jira: WebJira, meter: ActionMeter, issueMemory: Memory2<Project>")
    constructor(jira: WebJira, meter: ActionMeter, projectMemory: ProjectMemory) : this(jira, meter, ProjectMemory2Adapter(projectMemory))

    override fun run() {
        val project = projectMemory.recall()
        if (project == null) {
            logger.debug("Skipping Project summary action. I have no knowledge of projects.")
            return
        }
        meter.measure(PROJECT_SUMMARY) {
            jira.goToProjectSummary(project.key).waitForMetadata()
        }
    }
}
