package com.almostreliable.lootjs.loot.table.entry;

import com.almostreliable.lootjs.LootJS;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.Arrays;

public interface LootAppendHelper {

    default LootContainer addCustomEntry(JsonObject json) {
        var vanilla = LootJS.FUNCTION_GSON.fromJson(json, LootPoolEntryContainer.class);
        return addAndReturn(LootContainer.ofVanilla(vanilla));
    }

    default LootEntry addItem(ItemStack itemStack) {
        return addAndReturn(LootEntry.of(itemStack));
    }

    default LootEntry addTag(String tag) {
        return addAndReturn(LootEntry.tag(tag, false));
    }

    default LootEntry addTag(String tag, boolean expand) {
        return addAndReturn(LootEntry.tag(tag, expand));
    }

    default LootEntry addReference(ResourceLocation lootTable) {
        return addAndReturn(LootEntry.reference(lootTable));
    }

    default LootEntry addDynamic(ResourceLocation name) {
        return addAndReturn(LootEntry.dynamic(name));
    }

    default LootEntry addEmpty() {
        return addAndReturn(LootEntry.empty());
    }

    default CompositeLootEntry addAlternative(LootContainer... entries) {
        var composite = CompositeLootEntry.alternative();
        if (entries.length != 0) {
            composite.entries(list -> {
                list.addAll(Arrays.asList(entries));
            });
        }

        return addAndReturn(composite);
    }

    default CompositeLootEntry addSequence(LootContainer... entries) {
        var composite = CompositeLootEntry.sequence();
        if (entries.length != 0) {
            composite.entries(list -> {
                list.addAll(Arrays.asList(entries));
            });
        }

        return addAndReturn(composite);
    }

    default CompositeLootEntry addGroup(LootContainer... entries) {
        var composite = CompositeLootEntry.group();
        if (entries.length != 0) {
            composite.entries(list -> {
                list.addAll(Arrays.asList(entries));
            });
        }

        return addAndReturn(composite);
    }

    private <T extends LootContainer> T addAndReturn(T entry) {
        this.addEntry(entry);
        return entry;
    }

    void addEntry(LootContainer entry);
}
