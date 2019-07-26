package com.atlassian.performance.tools.jiraactions.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.memories.Issue
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
internal class AdaptiveIssueMemory(
    private val issueKeyMemory: Memory2<String>,
    random: SeededRandom
) : BasicMemory<Issue>(random, mutableSetOf()) {

    override fun remember(elements: Collection<Issue>) {
        this.elementCollection.addAll(elements)
        issueKeyMemory.remember(elements.map { it.key })
    }

    override fun remember(element: Issue) {
        elementCollection.add(element)
        issueKeyMemory.remember(element.key)
    }
}
