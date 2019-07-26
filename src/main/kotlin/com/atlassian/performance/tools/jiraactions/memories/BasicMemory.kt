package com.atlassian.performance.tools.jiraactions.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import java.util.function.Predicate

internal open class BasicMemory<T>(
    protected val random: SeededRandom,
    protected val elementCollection: MutableCollection<T>) : Memory2<T> {

    override fun recall(): T? {
        if (elementCollection.isEmpty()) {
            return null
        }
        return random.pick(elementCollection.toList())
    }

    override fun recall(filter: Predicate<T>): T? = random.pick(elementCollection.filter { filter.test(it) }.toList())

    override fun remember(elements: Collection<T>) {
        elementCollection.addAll(elements)
    }

    override fun remember(element: T) {
        elementCollection.add(element)
    }

    override fun all(): Collection<T> = elementCollection
}
