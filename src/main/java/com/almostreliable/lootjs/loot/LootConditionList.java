package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.ListHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class LootConditionList extends ListHolder<LootItemCondition, LootItemCondition>
        implements LootConditionsContainer<LootConditionList>, Predicate<LootContext> {

    public LootConditionList() {
        super();
    }

    public LootConditionList(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public Iterator<LootItemCondition> iterator() {
        return elements.listIterator();
    }

    @Override
    protected LootItemCondition wrap(LootItemCondition entry) {
        return entry;
    }

    @Override
    protected LootItemCondition unwrap(LootItemCondition entry) {
        return entry;
    }

    @Override
    public LootConditionList addCondition(LootItemCondition condition) {
        this.add(condition);
        return this;
    }

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Conditions: [");
        info.push();
        for (var entry : this) {
            Identifier key = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(entry.codec());
            if (key == null) continue;
            info.add(key.toString());
        }

        info.pop();
        info.add("]");
    }

    @Override
    public boolean test(LootContext context) {
        for (LootItemCondition condition : this) {
            if (!condition.test(context)) {
                return false;
            }
        }

        return true;
    }

    public boolean remove(IdFilter type) {
        return elements.removeIf(element -> {
            var id = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(element.codec());
            if (id == null) return false;
            return type.test(id);
        });
    }

    public boolean contains(Identifier type) {
        return indexOf(type) != -1;
    }

    public int indexOf(Identifier type) {
        for (int i = 0; i < elements.size(); i++) {
            var id = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(elements.get(i).codec());
            if (id != null && id.equals(type)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(Identifier type) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            var id = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(elements.get(i).codec());
            if (id != null && id.equals(type)) {
                return i;
            }
        }

        return -1;
    }
}
