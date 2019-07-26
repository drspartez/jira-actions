package com.atlassian.performance.tools.jiraactions.api.action

import com.atlassian.performance.tools.jiraactions.api.LOG_IN
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.memories.User
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.memories.UserMemory2Adapter

class LogInAction(
    private val jira: WebJira,
    private val meter: ActionMeter,
    private val userMemory: Memory2<User>
) : Action {

    @Deprecated("Use constructor(jira: WebJira, meter: ActionMeter, userMemory: Memory2<User>")
    constructor(jira: WebJira, meter: ActionMeter, userMemory: UserMemory) : this(jira, meter, UserMemory2Adapter(userMemory))

    override fun run() {
        val user = userMemory.recall()!!
        meter.measure(LOG_IN) {
            val dashboardPage = jira.goToLogin().logIn(user)
            dashboardPage.waitForDashboard()
        }
    }
}
