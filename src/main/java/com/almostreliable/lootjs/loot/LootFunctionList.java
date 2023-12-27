package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.LootObjectList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
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

    @Override
    public LootFunctionList setCount(NumberProvider numberProvider) {
        LootItemFunction sc = LootFunction.setCount(numberProvider);
        replace(LootItemFunctions.SET_COUNT, sc);
        return this;
    }

    public LootFunctionList setNbt(CompoundTag nbt) {
        LootItemFunction sc = LootFunction.setNbt(nbt);
        replace(LootItemFunctions.SET_NBT, sc);
        return this;
    }

    public LootFunctionList setNBT(CompoundTag nbt) {
        return setNbt(nbt);
    }

    public void replace(LootItemFunctionType type, LootItemFunction function) {
        var it = this.listIterator();
        while (it.hasNext()) {
            var entry = it.next();
            if (entry.getType() == type) {
                it.set(function);
                return;
            }
        }
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
