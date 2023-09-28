package com.almostreliable.lootjs.loot.table;

import com.almostreliable.lootjs.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.entry.LootAppendHelper;
import com.almostreliable.lootjs.loot.table.entry.LootTransformHelper;
import com.almostreliable.lootjs.loot.table.entry.LootContainer;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import com.almostreliable.lootjs.util.DebugInfo;
import com.almostreliable.lootjs.util.LootObjectList;
import com.almostreliable.lootjs.util.NullableFunction;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class LootEntryList extends LootObjectList<LootContainer> implements LootTransformHelper,
                                                                            LootAppendHelper {

    private static Collection<LootContainer> mutate(LootPoolEntryContainer... entries) {
        List<LootContainer> mutableEntries = new ArrayList<>(entries.length);
        for (LootPoolEntryContainer entry : entries) {
            mutableEntries.add(LootContainer.ofVanilla(entry));
        }

        return mutableEntries;
    }

    public LootEntryList() {
        super();
    }

    public LootEntryList(int initialCapacity) {
        super(initialCapacity);
    }

    @Nullable
    @Override
    protected LootContainer wrapTransformed(@Nullable Object o) {
        if (o instanceof LootContainer mle) {
            return mle;
        }

        ItemStack itemStack = ItemStackJS.of(o); // TODO outsource to not have KubeJS calls here
        if (!itemStack.isEmpty()) {
            return LootEntry.ofItemStack(itemStack);
        }

        return null;
    }

    public LootEntryList(LootContainer... entries) {
        super(Arrays.asList(entries));
    }

    public LootEntryList(LootPoolEntryContainer... entries) {
        super(mutate(entries));
    }

    @Override
    protected boolean entryMatches(LootContainer entry, ResourceLocationFilter filter) {
        var rl = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(entry.getVanillaType());
        return filter.test(rl);
    }


    public LootPoolEntryContainer[] createVanillaArray() {
        LootPoolEntryContainer[] entries = new LootPoolEntryContainer[this.size()];
        for (int i = 0; i < this.size(); i++) {
            entries[i] = this.get(i).saveAndGetOrigin();
        }

        return entries;
    }

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Entries: [");
        info.push();
        for (LootContainer entry : this) {
            entry.collectDebugInfo(info);
        }
        info.pop();
        info.add("]");
    }

    public void transformLoot(NullableFunction<LootEntry, Object> onTransform, boolean deepTransform) {
        transform(entry -> {
            if (entry instanceof LootTransformHelper helper && deepTransform) {
                helper.transformLoot(onTransform, true);
                return entry;
            }

            if (entry instanceof LootEntry sle) {
                return onTransform.apply(sle);
            }

            return entry;
        });
    }

    public void removeLoot(Predicate<LootEntry> filter, boolean deepRemove) {
        var it = listIterator();
        while (it.hasNext()) {
            var entry = it.next();
            if (entry instanceof LootTransformHelper helper && deepRemove) {
                helper.removeLoot(filter, true);
                continue;
            }

            if (entry instanceof LootEntry sle && filter.test(sle)) {
                it.remove();
            }
        }
    }

    @Override
    public void addEntry(LootContainer entry) {
        add(entry);
    }
}
