package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public interface LootEntry {

    LootItemCondition[] EMPTY_CONDITIONS = new LootItemCondition[0];
    LootItemFunction[] EMPTY_FUNCTIONS = new LootItemFunction[0];
    LootPoolEntryContainer[] EMPTY_ENTRIES = new LootPoolEntryContainer[0];

    static ItemLootEntry of(ItemStack itemStack) {
        return new ItemLootEntry(itemStack);
    }

    static ItemLootEntry of(Item item, NumberProvider count) {
        return new ItemLootEntry(item, count, null);
    }

    static ItemLootEntry of(Item item, NumberProvider count, CompoundTag nbt) {
        return new ItemLootEntry(item, count, nbt);
    }

    static EmptyLootEntry empty() {
        return new EmptyLootEntry();
    }

    static ReferenceLootEntry reference(ResourceLocation location) {
        return new ReferenceLootEntry(location);
    }

    static TagLootEntry tag(String tag) {
        return tag(tag, false);
    }

    static TagLootEntry tag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return new TagLootEntry(TagKey.create(Registries.ITEM, new ResourceLocation(tag)), expand);
    }

    static CompositeLootEntry alternative(LootEntry... entries) {
        var composite = new CompositeLootEntry(new AlternativesEntry(LootEntry.EMPTY_ENTRIES,
                LootEntry.EMPTY_CONDITIONS));
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    static CompositeLootEntry sequence(LootEntry... entries) {
        var composite = new CompositeLootEntry(new SequentialEntry(LootEntry.EMPTY_ENTRIES,
                LootEntry.EMPTY_CONDITIONS));
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    static CompositeLootEntry group(LootEntry... entries) {
        var composite = new CompositeLootEntry(new EntryGroup(LootEntry.EMPTY_ENTRIES, LootEntry.EMPTY_CONDITIONS));
        if (entries.length != 0) {
            composite.getEntries().addAll(List.of(entries));
        }

        return composite;
    }

    static CompositeLootEntry ofIngredient(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            throw new IllegalArgumentException(
                    "[LootEntry.ofIngredient()] Invalid ingredient, returning empty group. Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
        }

        CompositeLootEntry group = LootEntry.group();
        LootEntryList entries = group.getEntries();
        for (ItemStack item : ingredient.getItems()) {
            entries.add(new ItemLootEntry(item));
        }

        return group;
    }


    static LootEntry ofVanilla(LootPoolEntryContainer vanillaEntry) {
        if (vanillaEntry instanceof LootItem e) {
            return new ItemLootEntry(e);
        }

        if (vanillaEntry instanceof TagEntry e) {
            return new TagLootEntry(e);
        }

        if (vanillaEntry instanceof EmptyLootItem e) {
            return new EmptyLootEntry(e);
        }

        if (vanillaEntry instanceof LootTableReference e) {
            return new ReferenceLootEntry(e);
        }

        if (vanillaEntry instanceof DynamicLoot e) {
            return new DynamicLootEntry(e);
        }

        if (vanillaEntry instanceof CompositeEntryBase c) {
            return new CompositeLootEntry(c);
        }

        return new Unknown(vanillaEntry);
    }

    LootPoolEntryType getVanillaType();

    LootPoolEntryContainer getVanillaEntry();

    LootEntry when(Consumer<LootConditionList> callback);

    LootConditionList getConditions();

    default ResourceLocation getType() {
        ResourceLocation key = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(getVanillaType());
        if (key == null) {
            throw new IllegalStateException("Could not find key for loot pool entry type " + getVanillaType());
        }

        return key;
    }

    default void collectDebugInfo(DebugInfo info) {

    }


    default boolean isItem() {
        return getVanillaType() == LootPoolEntries.ITEM;
    }

    default boolean isTag() {
        return getVanillaType() == LootPoolEntries.TAG;
    }

    default boolean isEmpty() {
        return getVanillaType() == LootPoolEntries.EMPTY;
    }

    default boolean isDynamic() {
        return getVanillaType() == LootPoolEntries.DYNAMIC;
    }

    default boolean isReference() {
        return getVanillaType() == LootPoolEntries.REFERENCE;
    }

    default boolean isSimple() {
        return !isComposite();
    }

    default boolean isAlternative() {
        return getVanillaType() == LootPoolEntries.ALTERNATIVES;
    }


    default boolean isSequence() {
        return getVanillaType() == LootPoolEntries.SEQUENCE;
    }


    default boolean isGroup() {
        return getVanillaType() == LootPoolEntries.ALTERNATIVES;
    }

    default boolean isComposite() {
        return isAlternative() || isSequence() || isGroup();
    }

    record Unknown(LootPoolEntryContainer getVanillaEntry) implements LootEntry {

        @Override
        public LootPoolEntryType getVanillaType() {
            return getVanillaEntry().getType();
        }

        @Override
        public LootEntry when(Consumer<LootConditionList> callback) {
            return this;
        }

        @Override
        public LootConditionList getConditions() {
            return new LootConditionList();
        }

        @Override
        public void collectDebugInfo(DebugInfo info) {
            info.add("Unknown entry type: " + getType());
        }
    }
}
