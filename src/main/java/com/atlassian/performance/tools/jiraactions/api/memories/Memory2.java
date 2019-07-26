package com.atlassian.performance.tools.jiraactions.api.memories;

import java.util.Collection;
import java.util.function.Predicate;

public interface Memory2<T> {

    /**
     * Retrieves single element from memory.
     *
     * @return element from memory, null if memory is empty
     */
    T recall();

    /**
     * Retrieves single element from memory, first that satisfies predicate
     *
     * @param filter predicate to test elements with
     * @return element from memory, null if memory is empty
     */
    T recall(Predicate<T> filter);

    /**
     * Stores provided elements in emory
     *
     * @param elements elements to be stored in memory
     */
    void remember(Collection<T> elements);

    /**
     * Stores single element in memory
     *
     * @param element instance to store in memory
     */
    void remember(T element);

    /**
     * Returns memory content
     *
     * @return all elements in memory
     */
    Collection<T> all();
}
