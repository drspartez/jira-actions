package com.atlassian.performance.tools.jiraactions.api.memories

@Deprecated("Use Memory2<T>")
interface Memory<T> {
    fun recall(): T?
    fun remember(memories: Collection<T>)
}
