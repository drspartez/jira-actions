package com.atlassian.performance.tools.jiraactions.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.jql.Jql
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory2
import com.atlassian.performance.tools.jiraactions.api.page.IssuePage
import com.atlassian.performance.tools.jiraactions.jql.BuiltInJQL
import com.atlassian.performance.tools.jiraactions.jql.JqlContextImpl
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

internal class AdaptiveJqlMemory(random: SeededRandom) : BasicMemory<Jql>(random, mutableListOf()), JqlMemory2 {

    private val logger: Logger = LogManager.getLogger(this::class.java)

    init {
        BuiltInJQL.RESOLVED.get(JqlContextImpl(random))?.let { elementCollection.add(it) }
        BuiltInJQL.GENERIC_WIDE.get(JqlContextImpl(random))?.let { elementCollection.add(it) }
    }

    private val availableJqlSuppliers = mutableSetOf(
        BuiltInJQL.PRIORITIES,
        BuiltInJQL.PROJECT,
        BuiltInJQL.ASSIGNEE,
        BuiltInJQL.REPORTERS,
        BuiltInJQL.PROJECT_ASSIGNEE,
        BuiltInJQL.GIVEN_WORD
    )

    override fun observe(issuePage: IssuePage) {
        val jql = availableJqlSuppliers.asSequence()
            .map { it.get(JqlContextImpl(random, issuePage)) }
            .filter { it != null }
            .firstOrNull()

        jql?.let {
            logger.debug("Rendered a new jql query: <<${it.query()!!}>>")
            elementCollection.add(it)
            availableJqlSuppliers.remove(it.supplier())
        }
    }
}

