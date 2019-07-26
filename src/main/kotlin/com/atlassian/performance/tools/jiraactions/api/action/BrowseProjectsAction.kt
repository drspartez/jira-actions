package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.BROWSE_PROJECTS
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.memories.Project
import com.atlassian.performance.tools.jiraactions.api.memories.ProjectMemory
import com.atlassian.performance.tools.jiraactions.memories.ProjectMemory2Adapter

class BrowseProjectsAction constructor (
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val projectMemory: Memory2<Project>
) : Action {
    var page = 1

    @Deprecated("Use constructor(jira: WebJira, meter: ActionMeter, issueMemory: Memory2<Project>")
    constructor(jira: WebJira, meter: ActionMeter, projectMemory: ProjectMemory) : this(jira, meter, ProjectMemory2Adapter(projectMemory))

    override fun run() {
        val browseProjectsPage =
            meter.measure(BROWSE_PROJECTS) { jira.goToBrowseProjects(page).waitForProjectList() }

        page = browseProjectsPage.getNextPage() ?: 1
        projectMemory.remember(browseProjectsPage.getProjects())
    }
}
