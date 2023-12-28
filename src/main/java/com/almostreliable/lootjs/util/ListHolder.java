package com.almostreliable.lootjs.util;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ListHolder<W, T> implements Iterable<W> {

    protected final List<T> elements = new ArrayList<>();

    public ListHolder() {

    }

    public ListHolder(List<T> elements) {
        this.elements.addAll(elements);
    }

    @Override
    public abstract Iterator<W> iterator();

    protected abstract W wrap(T entry);

    protected abstract T unwrap(W entry);

    public List<T> getElements() {
        return elements;
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public boolean add(W entry) {
        T wrappedElement = unwrap(entry);
        return elements.add(wrappedElement);
    }

    public void clear() {
        elements.clear();
    }

    public W get(int index) {
        return wrap(elements.get(index));
    }

    public W set(int index, W element) {
        var replaced = elements.set(index, unwrap(element));
        return wrap(replaced);
    }

    public void add(int index, W element) {
        elements.add(index, unwrap(element));
    }

    public W remove(int index) {
        T removedElement = elements.remove(index);
        return wrap(removedElement);
    }
}
