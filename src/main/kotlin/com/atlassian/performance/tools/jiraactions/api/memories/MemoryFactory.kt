package com.atlassian.performance.tools.jiraactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.memories.AdaptiveIssueMemory
import com.atlassian.performance.tools.jiraactions.memories.AdaptiveJqlMemory
import com.atlassian.performance.tools.jiraactions.memories.BasicMemory

class MemoryFactory {
    companion object {
        fun adaptiveIssueKeyMemory(random: SeededRandom): Memory2<String> = BasicMemory(random, mutableSetOf())
        fun adaptiveJqlMemory(random: SeededRandom): JqlMemory2 = AdaptiveJqlMemory(random)
        fun adaptiveIssueMemory(issueKeyMemory: Memory2<String>, random: SeededRandom): Memory2<Issue> = AdaptiveIssueMemory(issueKeyMemory, random)
        fun adaptiveProjectMemory(random: SeededRandom): Memory2<Project> = BasicMemory(random, mutableSetOf())
        fun adaptiveUserMemory(random: SeededRandom): Memory2<User> = BasicMemory(random, mutableSetOf())
    }
}
