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

    private FilterResult(ArrayList<F> pNotFoundFilters, ArrayList<T> pFoundValues) {
        notFoundFilters = pNotFoundFilters;
        foundValues = pFoundValues;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> FilterResult<F, T> create(F[] pFilters, Function<F, T> pConverter) {
        ArrayList<F> notFoundFilters = new ArrayList<>();
        ArrayList<T> foundValues = new ArrayList<>();

        for (F filter : pFilters) {
            T convertedFilter = pConverter.apply(filter);
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
//
//    @SuppressWarnings("unchecked")
//    public T[] getFoundValuesAsArray() {
//        T[] result = (T[]) Array.newInstance(castClass, foundValues.size());
//        for (int i = 0; i < result.length; i++) {
//            result[i] = foundValues.get(i);
//        }
//        return result;
//    }

    public void yeetIfUnresolvedFilters() {
        if (notFoundFilters.isEmpty()) {
            return;
        }

        String valuesStr = notFoundFilters.stream().map(Objects::toString).collect(Collectors.joining(", "));
        throw new IllegalStateException("Not all filters could be resolved: " + valuesStr);
    }
}
