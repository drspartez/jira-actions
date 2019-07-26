package com.atlassian.performance.tools.jiraactions.api.memories

@Deprecated("Use Memory2<Issue> and MemoryFactory.adaptiveIssueMemory")
interface IssueMemory {
    fun recall(): Issue?
    fun recall(filter: (Issue) -> Boolean): Issue?
    fun remember(issues: Collection<Issue>)
}
