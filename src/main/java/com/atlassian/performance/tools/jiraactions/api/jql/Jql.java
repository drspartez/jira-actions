package com.atlassian.performance.tools.jiraactions.api.jql;


public interface Jql {

    /**
     * Provides string representation of JQL.
     *
     * @return prepared JQL string
     */
    String query();

    /**
     * Provides JqlSupplier which was used to crete this query
     *
     * @return JqlSupplier
     */
    JqlSupplier supplier();
}
