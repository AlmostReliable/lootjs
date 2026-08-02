package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.ListHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.apache.commons.lang3.mutable.MutableBoolean;

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

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Functions: [");
        info.push();
        for (var entry : this) {
            var key = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(entry.codec());
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

    public boolean replace(Identifier id, LootItemFunction function) {
        MutableBoolean found = new MutableBoolean(false);
        elements.replaceAll(entry -> {
            var entryId = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(entry.codec());
            if (entryId != null && entryId.equals(id)) {
                found.setValue(true);
                return function;
            }

            return entry;
        });

        return found.booleanValue();
    }

    public boolean remove(IdFilter type) {
        return elements.removeIf(element -> {
            var id = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(element.codec());
            if (id == null) return false;
            return type.test(id);
        });
    }

    public boolean contains(Identifier id) {
        return indexOf(id) != -1;
    }

    public int indexOf(Identifier id) {
        for (int i = 0; i < elements.size(); i++) {
            var entryId = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(elements.get(i).codec());
            if (entryId != null && entryId.equals(id)) {
                return i;
            }
        }

        return -1;
    }

    public int lastIndexOf(Identifier id) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            var entryId = BuiltInRegistries.LOOT_FUNCTION_TYPE.getKey(elements.get(i).codec());
            if (entryId != null && entryId.equals(id)) {
                return i;
            }
        }

        return -1;
    }
}
