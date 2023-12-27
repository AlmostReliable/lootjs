package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.LootObjectList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootConditionList extends LootObjectList<LootItemCondition>
        implements LootConditionsContainer<LootConditionList> {

    public LootConditionList() {
        super();
    }

    @Nullable
    @Override
    protected LootItemCondition wrapTransformed(@Nullable Object o) {
        if (o instanceof LootItemCondition lic) {
            return lic;
        }

        return null;
    }

    @Override
    protected boolean entryMatches(LootItemCondition entry, ResourceLocationFilter filter) {
        var rl = BuiltInRegistries.LOOT_CONDITION_TYPE.getKey(entry.getType());
        return filter.test(rl);
    }

    public LootConditionList(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootConditionList addCondition(LootItemCondition condition) {
        this.add(condition);
        return this;
    }

    public LootItemCondition[] createVanillaArray() {
        return this.toArray(new LootItemCondition[0]);
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
}
