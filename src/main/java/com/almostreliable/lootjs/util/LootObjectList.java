package com.almostreliable.lootjs.util;


import com.almostreliable.lootjs.core.filters.IdFilter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

public abstract class LootObjectList<T> extends ArrayList<T> {

    public LootObjectList() {
        super();
    }

    public LootObjectList(int initialCapacity) {
        super(initialCapacity);
    }

    public LootObjectList(Collection<T> entries) {
        super(entries);
    }

    public void removeById(IdFilter filter) {
        removeIf(entry -> entryMatches(entry, filter));
    }

    public void transform(NullableFunction<T, Object> onTransform) {
        ListIterator<T> it = listIterator();
        while (it.hasNext()) {
            T t = it.next();
            Object o = onTransform.apply(t);
            T transformed = wrapTransformed(o);
            if (transformed == null) {
                it.remove();
                continue;
            }

            if (transformed != t) {
                it.set(transformed);
            }
        }
    }

    @Nullable
    protected abstract T wrapTransformed(@Nullable Object o);

    protected abstract boolean entryMatches(T entry, IdFilter filter);
}
