package com.atlassian.performance.tools.jiraactions.api.jql;

import com.atlassian.performance.tools.jiraactions.api.SeededRandom;
import com.atlassian.performance.tools.jiraactions.api.page.IssuePage;

public interface JqlContext {
    SeededRandom seededRandom();
    IssuePage issuePage();
}
