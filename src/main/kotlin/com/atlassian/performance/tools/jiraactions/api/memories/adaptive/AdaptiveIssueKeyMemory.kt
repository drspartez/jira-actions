package com.atlassian.performance.tools.jiraactions.api.memories.adaptive

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
@Deprecated("Use MemoryFactory.adaptiveIssueKeyMemory")
class AdaptiveIssueKeyMemory(
    private val random: SeededRandom
) : IssueKeyMemory {
    private val issues = mutableSetOf<String>()
    override fun recall(): String? {
        if (issues.isEmpty()) {
            return null
        }
        return random.pick(issues.toList())
    }

    override fun remember(memories: Collection<String>) {
        issues.addAll(memories)
    }
}
