package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.LootObjectList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootFunctionList extends LootObjectList<LootItemFunction>
        implements LootFunctionsContainer<LootFunctionList> {

    public LootFunctionList() {
        super();
    }

    @Nullable
    @Override
    protected LootItemFunction wrapTransformed(@Nullable Object o) {
        if (o instanceof LootItemFunction lif) {
            return lif;
        }

        return null;
    }

    @Override
    protected boolean entryMatches(LootItemFunction entry, ResourceLocationFilter filter) {
        var rl = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(entry.getType());
        return filter.test(rl);
    }

    public LootFunctionList(LootItemFunction... functions) {
        super(List.of(functions));
    }

    @Override
    public LootFunctionList addFunction(LootItemFunction function) {
        this.add(function);
        return this;
    }

    public LootItemFunction[] createVanillaArray() {
        return this.toArray(new LootItemFunction[0]);
    }

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Functions: [");
        info.push();
        for (var entry : this) {
            ResourceLocation key = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(entry.getType());
            if (key == null) continue;
            info.add(key.toString());
        }

        info.pop();
        info.add("]");
    }
}
