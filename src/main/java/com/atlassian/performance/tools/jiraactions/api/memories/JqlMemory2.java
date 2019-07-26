package com.atlassian.performance.tools.jiraactions.api.memories;

import com.atlassian.performance.tools.jiraactions.api.jql.Jql;
import com.atlassian.performance.tools.jiraactions.api.page.IssuePage;

public interface JqlMemory2 extends Memory2<Jql> {

    /**
     * Updates memory with JQL(s) that is possible to build with data
     * available in IssuePage
     *
     * @param page jira page instance to analyse
     */
    void observe(IssuePage page);
}
