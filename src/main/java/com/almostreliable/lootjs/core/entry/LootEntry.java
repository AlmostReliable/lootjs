package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
public interface LootEntry extends LootConditionsContainer<LootEntry> {

    List<LootItemCondition> EMPTY_CONDITIONS = List.of();
    List<LootItemFunction> EMPTY_FUNCTIONS = List.of();

    static ItemLootEntry ofItem(Item item) {
        return new ItemLootEntry(new ItemStack(item));
    }

    static ItemLootEntry of(ItemStack itemStack) {
        return new ItemLootEntry(itemStack);
    }

    static ItemLootEntry of(Item item, NumberProvider count) {
        return new ItemLootEntry(item, count);
    }

    static EmptyLootEntry empty() {
        return new EmptyLootEntry();
    }

    static TableReferenceLootEntry reference(ResourceLocation location) {
        return new TableReferenceLootEntry(location);
    }

    static TagLootEntry tag(String tag) {
        return tag(tag, false);
    }

    static TagLootEntry tag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return new TagLootEntry(TagKey.create(Registries.ITEM, ResourceLocation.parse(tag)), expand);
    }

    static CompositeLootEntry alternative(LootEntry... entries) {
        var el = new LootEntryList(entries);
        var cl = new LootConditionList();
        return new CompositeLootEntry(new AlternativesEntry(el.getElements(), cl.getElements()), el, cl);
    }

    static CompositeLootEntry sequence(LootEntry... entries) {
        var el = new LootEntryList(entries);
        var cl = new LootConditionList();
        return new CompositeLootEntry(new SequentialEntry(el.getElements(), cl.getElements()), el, cl);
    }

    static CompositeLootEntry group(LootEntry... entries) {
        var el = new LootEntryList(entries);
        var cl = new LootConditionList();
        return new CompositeLootEntry(new EntryGroup(el.getElements(), cl.getElements()), el, cl);
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

        if (vanillaEntry instanceof NestedLootTable e) {
            return new TableReferenceLootEntry(e);
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
        return getVanillaType() == LootPoolEntries.LOOT_TABLE;
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

        @Override
        public Unknown addCondition(LootItemCondition condition) {
            // no-op
            return this;
        }
    }
}
