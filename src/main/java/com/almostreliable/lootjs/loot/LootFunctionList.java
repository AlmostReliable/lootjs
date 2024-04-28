package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.ListHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class LootFunctionList extends ListHolder<LootItemFunction, LootItemFunction>
        implements LootFunctionsContainer<LootFunctionList>, BiFunction<ItemStack, LootContext, ItemStack> {

    public LootFunctionList() {
        super();
    }

    public LootFunctionList(List<LootItemFunction> functions) {
        super(functions);
    }

    @Override
    public Iterator<LootItemFunction> iterator() {
        return elements.listIterator();
    }

    @Override
    protected LootItemFunction wrap(LootItemFunction entry) {
        return entry;
    }

    @Override
    protected LootItemFunction unwrap(LootItemFunction entry) {
        return entry;
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

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext context) {
        for (var entry : this) {
            itemStack = entry.apply(itemStack, context);
        }

        return itemStack;
    }

    public void replace(LootItemFunctionType type, LootItemFunction function) {
        elements.replaceAll(entry -> entry.getType().equals(type) ? function : entry);
    }

    public boolean remove(ResourceLocationFilter type) {
        return elements.removeIf(element -> type.test(BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(element.getType())));
    }

    public boolean contains(LootItemFunctionType type) {
        return indexOf(type) != -1;
    }

    public int indexOf(LootItemFunctionType type) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(LootItemFunctionType type) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i).getType().equals(type)) {
                return i;
            }
        }

        return -1;
    }
}
