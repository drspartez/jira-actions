package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.CREATE_ISSUE
import com.atlassian.performance.tools.jiraactions.api.CREATE_ISSUE_SUBMIT
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.VIEW_DASHBOARD
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.memories.Project
import com.atlassian.performance.tools.jiraactions.api.memories.ProjectMemory
import com.atlassian.performance.tools.jiraactions.memories.ProjectMemory2Adapter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class CreateIssueAction constructor (
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val projectMemory: Memory2<Project>,
    private val seededRandom: SeededRandom
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    @Deprecated("Use constructor (jira: WebJira, meter: ActionMeter, projectMemory: Memory2<Project>, seededRandom: SeededRandom)")
    constructor (jira: WebJira, meter: ActionMeter, projectMemory: ProjectMemory, seededRandom: SeededRandom) : this(jira, meter, ProjectMemory2Adapter(projectMemory), seededRandom)

    override fun run() {
        val project = projectMemory.recall()
        if (project == null) {
            logger.debug("Skipping Create issue action. I have no knowledge of projects.")
            return
        }
        meter.measure(CREATE_ISSUE) {
            val dashboardPage = meter.measure(VIEW_DASHBOARD) {
                jira.goToDashboard().waitForDashboard()
            }
            val issueCreateDialog = dashboardPage.openIssueCreateDialog()
            val filledForm = issueCreateDialog
                .waitForDialog()
                .selectProject(project.name)
                .selectIssueType(
                    seededRandom.pick(
                        issueCreateDialog.getIssueTypes().filter { it != "Epic" }
                    )!!
                )
                .fill("summary", "This is a simple summary")
            issueCreateDialog.fillRequiredFields()
            meter.measure(CREATE_ISSUE_SUBMIT) { filledForm.submit() }
        }
    }
}
