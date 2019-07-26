package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory2
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.observation.SearchJqlObservation
import com.atlassian.performance.tools.jiraactions.api.page.IssueNavigatorPage
import com.atlassian.performance.tools.jiraactions.jql.BuiltInJQL
import com.atlassian.performance.tools.jiraactions.api.SEARCH_JQL_CHANGELOG
import com.atlassian.performance.tools.jiraactions.memories.IssueKeyMemory2Adapter
import com.atlassian.performance.tools.jiraactions.memories.JqlMemory2Adapter
import java.util.function.Predicate
import javax.json.JsonObject

class SearchJqlChangelogAction(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val jqlMemory: JqlMemory2,
    private val issueKeyMemory: Memory2<String>
) : Action {


    @Deprecated("Use constructor(jira: WebJira, meter: ActionMeter, jqlMemory: JqlMemory2, issueKeyMemory: Memory2<String>)")
    constructor(jira: WebJira, meter: ActionMeter, jqlMemory: JqlMemory, issueKeyMemory: IssueKeyMemory) : this(jira, meter, JqlMemory2Adapter(jqlMemory), IssueKeyMemory2Adapter(issueKeyMemory))


    override fun run() {
        val jqlQuery = jqlMemory.recall(Predicate { it.supplier().uniqueName() == BuiltInJQL.REPORTERS.name })!!.query()
        val issueNavigatorPage = meter.measure(
            key = SEARCH_JQL_CHANGELOG,
            action = { jira.goToIssueNavigator(jqlQuery).waitForIssueNavigator() },
            observation = this::observe
        )
        issueKeyMemory.remember(issueNavigatorPage.getIssueKeys())
    }

    private fun observe(
        page: IssueNavigatorPage
    ): JsonObject {
        val issueKeys = page.getIssueKeys()
        issueKeyMemory.remember(issueKeys)
        return SearchJqlObservation(page.jql, issueKeys.size, page.getTotalResults()).serialize()
    }
}
