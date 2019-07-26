package com.atlassian.performance.tools.jiraactions.api.jql;

import com.atlassian.performance.tools.jiraactions.jql.BuiltInJQL;
import java.util.Arrays;
import java.util.List;

public interface JqlSupplier {

    Jql get(JqlContext context);

    String uniqueName();

    static List<JqlSupplier> getBuiltInJqlSuppliers() {
        return Arrays.asList(BuiltInJQL.values());
    }
}
