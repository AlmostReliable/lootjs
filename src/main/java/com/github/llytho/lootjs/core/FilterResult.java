package com.github.llytho.lootjs.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FilterResult<F, T> {
    private final ArrayList<F> notFoundFilters;
    private final ArrayList<T> foundValues;

    private FilterResult(ArrayList<F> notFoundFilters, ArrayList<T> foundValues) {
        this.notFoundFilters = notFoundFilters;
        this.foundValues = foundValues;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> FilterResult<F, T> create(List<F> filters, Function<F, T> converter) {
        ArrayList<F> notFoundFilters = new ArrayList<>();
        ArrayList<T> foundValues = new ArrayList<>();

        for (F filter : filters) {
            T convertedFilter = converter.apply(filter);
            if (convertedFilter == null) {
                notFoundFilters.add(filter);
            } else {
                foundValues.add(convertedFilter);
            }
        }

        return new FilterResult<F, T>(notFoundFilters, foundValues);
    }

    public List<F> getNotFoundFilters() {
        return Collections.unmodifiableList(notFoundFilters);
    }

    public List<T> getFoundValues() {
        return Collections.unmodifiableList(foundValues);
    }

    public void yeetIfUnresolvedFilters() {
        if (notFoundFilters.isEmpty()) {
            return;
        }

        String valuesStr = notFoundFilters.stream().map(Objects::toString).collect(Collectors.joining(", "));
        throw new IllegalStateException("Not all filters could be resolved: " + valuesStr);
    }
}
