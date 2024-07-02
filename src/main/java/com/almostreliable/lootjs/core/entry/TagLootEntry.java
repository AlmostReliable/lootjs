package com.almostreliable.lootjs.core.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TagLootEntry extends AbstractSimpleLootEntry<TagEntry> implements SingleLootEntry {

    public TagLootEntry(TagEntry vanillaEntry) {
        super(vanillaEntry);
    }

    public TagLootEntry(TagKey<Item> tag, boolean expand) {
        super(new TagEntry(tag,
                expand,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                EMPTY_CONDITIONS,
                EMPTY_FUNCTIONS));
    }

    public boolean getExpand() {
        return vanillaEntry.expand;
    }

    public void setExpand(boolean expand) {
        vanillaEntry.expand = expand;
    }

    public String getTag() {
        return vanillaEntry.tag.location().toString();
    }

    public void setTag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        vanillaEntry.tag = TagKey.create(Registries.ITEM, ResourceLocation.parse(tag));
    }

    public boolean isTag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return tag.equals(getTag());
    }

    @Override
    public TagLootEntry addCondition(LootItemCondition condition) {
        getConditions().add(condition);
        return this;
    }
}
