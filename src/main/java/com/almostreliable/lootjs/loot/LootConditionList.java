package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.ListHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class LootConditionList extends ListHolder<LootItemCondition, LootItemCondition>
        implements LootConditionsContainer<LootConditionList>, Predicate<LootContext> {

    public LootConditionList() {
        super();
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

    public LootConditionList(List<LootItemCondition> conditions) {
        super(conditions);
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
            ResourceLocation key = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(entry.getType());
            if (key == null) continue;
            info.add(key.toString());
        }

        info.pop();
        info.add("]");
    }

    @Override
    public boolean test(LootContext context) {
        for (LootItemCondition condition : this) {
            if(!condition.test(context)) {
                return false;
            }
        }

        return true;
    }

    public boolean remove(ResourceLocationFilter type) {
        return elements.removeIf(element -> type.test(BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(element.getType())));
    }

    public boolean contains(LootItemConditionType type) {
        return indexOf(type) != -1;
    }

    public int indexOf(LootItemConditionType type) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(LootItemConditionType type) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }
}
