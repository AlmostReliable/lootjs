package com.almostreliable.lootjs.core.entry;

import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.LootConditionsContainer;
import com.almostreliable.lootjs.loot.LootEntryList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public interface LootEntry extends LootConditionsContainer<LootEntry> {

    List<LootItemCondition> EMPTY_CONDITIONS = List.of();
    List<LootItemFunction> EMPTY_FUNCTIONS = List.of();

    static ItemLootEntry of(Item item) {
        return new ItemLootEntry(item, null);
    }

    static ItemLootEntry of(Item item, NumberProvider count) {
        return new ItemLootEntry(item, count);
    }

    static EmptyLootEntry empty() {
        return new EmptyLootEntry();
    }

    static TableReferenceLootEntry reference(Identifier location) {
        return new TableReferenceLootEntry(location);
    }

    static TagLootEntry tag(String tag) {
        return tag(tag, false);
    }

    static TagLootEntry tag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return new TagLootEntry(TagKey.create(Registries.ITEM, Identifier.parse(tag)), expand);
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

    static ItemLootEntry testItem(String name) {
        return (ItemLootEntry) LootEntry.of(Items.PAPER).setName(Component.literal(name));
    }

    static EntityPredicate ep(EntityPredicate ep) {
        return ep;
    }

    static CompositeLootEntry ofIngredient(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            throw new IllegalArgumentException(
                    "[LootEntry.ofIngredient()] Invalid ingredient, returning empty group. Consider using `LootEntry.empty()` if you want to create an empty loot entry.");
        }

        CompositeLootEntry group = LootEntry.group();
        LootEntryList entries = group.getEntries();
        if (ingredient.isCustom()) {
            ingredient.getCustomIngredient().items().forEach(item -> entries.add(new ItemLootEntry(item)));
        } else {
            ingredient.getValues().stream().forEach(item -> entries.add(new ItemLootEntry(item)));
        }

        return group;
    }


    static LootEntry ofVanilla(LootPoolEntryContainer vanillaEntry) {
        return switch (vanillaEntry) {
            case LootItem e -> new ItemLootEntry(e);
            case TagEntry e -> new TagLootEntry(e);
            case EmptyLootItem e -> new EmptyLootEntry(e);
            case NestedLootTable e -> new TableReferenceLootEntry(e);
            case DynamicLoot e -> new DynamicLootEntry(e);
            case CompositeEntryBase c -> new CompositeLootEntry(c);
            default -> new Unknown(vanillaEntry);
        };

    }

    LootPoolEntryContainer getVanillaEntry();

    LootEntry when(Consumer<LootConditionList> callback);

    LootConditionList getConditions();

    default Identifier getType() {
        Identifier key = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.getKey(getVanillaEntry().codec());
        if (key == null) {
            throw new IllegalStateException("Could not find key for loot pool entry " + getVanillaEntry());
        }

        return key;
    }

    default void collectDebugInfo(DebugInfo info) {

    }


    default boolean isItem() {
        return getVanillaEntry() instanceof LootItem;
    }

    default boolean isTag() {
        return getVanillaEntry() instanceof TagEntry;
    }

    default boolean isEmpty() {
        return getVanillaEntry() instanceof EmptyLootItem;
    }

    default boolean isDynamic() {
        return getVanillaEntry() instanceof DynamicLoot;
    }

    default boolean isReference() {
        return getVanillaEntry() instanceof NestedLootTable;
    }

    default boolean isSimple() {
        return !isComposite();
    }

    default boolean isAlternative() {
        return getVanillaEntry() instanceof AlternativesEntry;
    }


    default boolean isSequence() {
        return getVanillaEntry() instanceof SequentialEntry;
    }


    default boolean isGroup() {
        return getVanillaEntry() instanceof EntryGroup;
    }

    default boolean isComposite() {
        return isAlternative() || isSequence() || isGroup();
    }

    default ItemStack createRandomItem(LootContext context) {
        var entries = new ArrayList<LootPoolEntry>();
        getVanillaEntry().expand(context, entries::add);

        var randomEntry = entries.isEmpty() ? null : entries.get(context.getRandom().nextInt(entries.size()));
        if (randomEntry == null) {
            return ItemStack.EMPTY;
        }

        var loot = new ArrayList<ItemStack>();
        randomEntry.createItemStack(loot::add, context);
        if (loot.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return loot.get(context.getRandom().nextInt(loot.size()));
    }

    record Unknown(LootPoolEntryContainer getVanillaEntry) implements LootEntry {

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
        public ItemStack createRandomItem(LootContext context) {
            return ItemStack.EMPTY;
        }

        @Override
        public Unknown addCondition(LootItemCondition condition) {
            // no-op
            return this;
        }
    }
}
