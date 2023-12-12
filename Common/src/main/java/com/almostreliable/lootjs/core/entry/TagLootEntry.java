package com.almostreliable.lootjs.core.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;

public class TagLootEntry extends AbstractSimpleLootEntry<TagEntry> implements LootEntry {

    public TagLootEntry(TagEntry vanillaEntry) {
        super(vanillaEntry);
    }

    public TagLootEntry(TagKey<Item> tag, boolean expand) {
        super(new TagEntry(tag,
                expand,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                LootEntry.EMPTY_CONDITIONS,
                LootEntry.EMPTY_FUNCTIONS));
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
        setTag(tag, false);
    }

    public void setTag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        vanillaEntry.tag = TagKey.create(Registries.ITEM, new ResourceLocation(tag));
    }

    public boolean isTag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return tag.equals(getTag());
    }
}
