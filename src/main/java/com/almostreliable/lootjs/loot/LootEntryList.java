package com.almostreliable.lootjs.loot;

import com.almostreliable.lootjs.core.entry.SimpleLootEntry;
import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.core.filters.ResourceLocationFilter;
import com.almostreliable.lootjs.loot.table.LootAppendHelper;
import com.almostreliable.lootjs.loot.table.LootTransformHelper;
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

public class LootEntryList extends LootObjectList<LootEntry> implements LootTransformHelper,
                                                                        LootAppendHelper {

    private static Collection<LootEntry> mutate(LootPoolEntryContainer... entries) {
        List<LootEntry> mutableEntries = new ArrayList<>(entries.length);
        for (LootPoolEntryContainer entry : entries) {
            mutableEntries.add(LootEntry.ofVanilla(entry));
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
    protected LootEntry wrapTransformed(@Nullable Object o) {
        if (o instanceof LootEntry mle) {
            return mle;
        }

        ItemStack itemStack = ItemStackJS.of(o); // TODO outsource to not have KubeJS calls here
        if (!itemStack.isEmpty()) {
            return LootEntry.of(itemStack);
        }

        return null;
    }

    public LootEntryList(LootEntry... entries) {
        super(Arrays.asList(entries));
    }

    public LootEntryList(LootPoolEntryContainer... entries) {
        super(mutate(entries));
    }

    @Override
    protected boolean entryMatches(LootEntry entry, ResourceLocationFilter filter) {
        var rl = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(entry.getVanillaType());
        return filter.test(rl);
    }


    public LootPoolEntryContainer[] createVanillaArray() {
        LootPoolEntryContainer[] entries = new LootPoolEntryContainer[this.size()];
        for (int i = 0; i < this.size(); i++) {
            entries[i] = this.get(i).getVanillaEntry();
        }

        return entries;
    }

    public void collectDebugInfo(DebugInfo info) {
        if (this.isEmpty()) return;

        info.add("% Entries: [");
        info.push();
        for (LootEntry entry : this) {
            entry.collectDebugInfo(info);
        }
        info.pop();
        info.add("]");
    }

    public void transformEntry(NullableFunction<SimpleLootEntry, Object> onTransform, boolean deepTransform) {
        transform(entry -> {
            if (entry instanceof LootTransformHelper helper && deepTransform) {
                helper.transformEntry(onTransform, true);
                return entry;
            }

            if (entry instanceof SimpleLootEntry le) {
                return onTransform.apply(le);
            }

            return entry;
        });
    }

    public void removeEntry(Predicate<SimpleLootEntry> filter, boolean deepRemove) {
        var it = listIterator();
        while (it.hasNext()) {
            var entry = it.next();
            if (entry instanceof LootTransformHelper helper && deepRemove) {
                helper.removeEntry(filter, true);
                continue;
            }

            if (entry instanceof SimpleLootEntry le && filter.test(le)) {
                it.remove();
            }
        }
    }

    @Override
    public void addEntry(LootEntry entry) {
        add(entry);
    }
}
