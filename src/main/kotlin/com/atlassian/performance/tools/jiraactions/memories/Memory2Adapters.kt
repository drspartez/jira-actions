package com.atlassian.performance.tools.jiraactions.memories

import com.atlassian.performance.tools.jiraactions.api.jql.Jql
import com.atlassian.performance.tools.jiraactions.api.jql.JqlContext
import com.atlassian.performance.tools.jiraactions.api.jql.JqlSupplier
import com.atlassian.performance.tools.jiraactions.api.memories.Issue
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.IssueMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.JqlMemory2
import com.atlassian.performance.tools.jiraactions.api.memories.Memory2
import com.atlassian.performance.tools.jiraactions.api.memories.Project
import com.atlassian.performance.tools.jiraactions.api.memories.ProjectMemory
import com.atlassian.performance.tools.jiraactions.api.memories.User
import com.atlassian.performance.tools.jiraactions.api.memories.UserMemory
import com.atlassian.performance.tools.jiraactions.api.page.IssuePage
import com.atlassian.performance.tools.jiraactions.jql.JqlImpl
import java.lang.UnsupportedOperationException
import java.util.function.Predicate

@Deprecated("Remove together with ProjectMemory")
internal class ProjectMemory2Adapter(private val delegate: ProjectMemory) : Memory2<Project> {

    override fun recall(): Project? = delegate.recall()

    override fun recall(filter: Predicate<Project>?): Project? {
        return null
    }

    override fun remember(elements: Collection<Project>) {
       delegate.remember(elements)
    }

    override fun remember(element: Project) {
        delegate.remember(listOf(element))
    }

    override fun all(): Collection<Project> {
        throw UnsupportedOperationException("all() is unsupported for legacy memory adapter. Use Memory2<Project>")
    }
}

@Deprecated("Remove together with IssueMemory")
internal class IssueMemory2Adapter(private val delegate: IssueMemory) : Memory2<Issue> {

    override fun recall(): Issue? = delegate.recall()

    override fun recall(filter: Predicate<Issue>): Issue? {
        return delegate.recall { filter.test(it) }
    }

    override fun remember(elements: Collection<Issue>) {
        delegate.remember(elements)
    }

    override fun remember(element: Issue) {
        delegate.remember(listOf(element))
    }

    override fun all(): Collection<Issue> {
        throw UnsupportedOperationException("all() is unsupported for legacy memory adapter. Use Memory2<Project>")
    }
}

@Deprecated("Remove together with UserMemory")
internal class UserMemory2Adapter(private val delegate: UserMemory) : Memory2<User> {

    override fun recall(): User? = delegate.recall()

    override fun recall(filter: Predicate<User>): User? {
        return null
    }

    override fun remember(elements: Collection<User>) {
        delegate.remember(elements)
    }

    override fun remember(element: User) {
        delegate.remember(listOf(element))
    }

    override fun all(): Collection<User> {
        throw UnsupportedOperationException("all() is unsupported for legacy memory adapter. Use Memory2<User>")
    }
}

@Deprecated("Remove together with JqlMemory")
internal class JqlMemory2Adapter(private val delegate: JqlMemory) : JqlMemory2 {

    private class EmptySupplierType(private val name: String) : JqlSupplier {
        override fun get(context: JqlContext?): Jql? = null
        override fun uniqueName(): String  = name
    }

    private companion object {
        val EMPTY_SUPPLIER = EmptySupplierType("")
    }

    override fun all(): MutableCollection<Jql> {
        throw UnsupportedOperationException("all() is unsupported for legacy memory adapter. Use JqlMemory2")
    }

    override fun observe(page: IssuePage) {
        delegate.observe(page)
    }

    override fun recall(): Jql? {
        return delegate.recall()?.let { JqlImpl(it, EMPTY_SUPPLIER) }
    }

    override fun recall(filter: Predicate<Jql>): Jql? {
        val pred: Predicate<String> = Predicate { filter.test(JqlImpl("", EmptySupplierType(it)))  }
        return delegate.recallByTag(pred)?.let { JqlImpl(it, EMPTY_SUPPLIER) }
    }

    override fun remember(elements: Collection<Jql>) {
        delegate.remember(elements.map { it.query() })
    }

    override fun remember(element: Jql) {
        delegate.remember(listOf(element.query()))
    }
}

@Deprecated("Remove together with IssueKeyMemory")
internal class IssueKeyMemory2Adapter(private val delegate: IssueKeyMemory) : Memory2<String> {
    override fun recall(): String?  = delegate.recall()


    override fun recall(filter: Predicate<String>?): String? {
        return null
    }

    override fun remember(elements: Collection<String>) {
        delegate.remember(elements)
    }

    override fun remember(element: String) {
        delegate.remember(listOf(element))
    }

    override fun all(): MutableCollection<String> {
        throw UnsupportedOperationException("all() is unsupported for legacy memory adapter. Memory2<String>")
    }
}
