package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.VIEW_ISSUE
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Issue
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.IssueMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory2
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.memories.IssueKeyMemory2Adapter
import com.atlassian.performance.tools.jiraactions.memories.IssueMemory2Adapter
import com.atlassian.performance.tools.jiraactions.memories.JqlMemory2Adapter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.json.Json

class ViewIssueAction (
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val issueKeyMemory: Memory2<String>,
    private val issueMemory: Memory2<Issue>,
    private val jqlMemory: JqlMemory2
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    @Deprecated("Use constructor(jira: WebJira, meter: ActionMeter, issueKeyMemory: Memory2<String>, issueMemory: Memory2<Issue>, jqlMemory: JqlMemory2)")
    constructor(jira: WebJira, meter: ActionMeter, issueKeyMemory: IssueKeyMemory, issueMemory: IssueMemory, jqlMemory: JqlMemory) :
        this(jira, meter, IssueKeyMemory2Adapter(issueKeyMemory), IssueMemory2Adapter(issueMemory), JqlMemory2Adapter(jqlMemory))

    override fun run() {
        val issueKey = issueKeyMemory.recall()
        if (issueKey == null) {
            logger.debug("Skipping View Issue action. I have no knowledge of issue keys.")
            return
        }
        val issuePage = meter.measure(
            key = VIEW_ISSUE,
            action = { jira.goToIssue(issueKey).waitForSummary() },
            observation = { page -> Json.createObjectBuilder()
                .add("issueKey", issueKey)
                .add("issueId", page.getIssueId())
                .build()
            }
        )
        val issue = Issue(
            key = issueKey,
            editable = issuePage.isEditable(),
            id = issuePage.getIssueId(),
            type = issuePage.getIssueType()
        )
        issueMemory.remember(setOf(issue))
        jqlMemory.observe(issuePage)
    }
}
