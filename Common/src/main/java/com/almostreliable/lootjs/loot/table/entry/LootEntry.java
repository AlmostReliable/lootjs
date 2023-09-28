package com.almostreliable.lootjs.loot.table.entry;

import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.LootFunctionsContainer;
import com.almostreliable.lootjs.loot.table.LootConditionList;
import com.almostreliable.lootjs.loot.table.LootFunctionList;
import com.almostreliable.lootjs.util.DebugInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class LootEntry implements LootContainer, LootFunctionsContainer<LootEntry> {
    protected LootPoolSingletonContainer origin;
    @Nullable protected LootFunctionList functions;
    @Nullable protected LootConditionList conditions;

    public LootEntry(LootPoolSingletonContainer origin) {
        this.origin = origin;
    }

    public static LootEntry ofItemStack(ItemStack itemStack) {
        itemStack = itemStack.copy();
        return ofItem(itemStack.getItem(),
                itemStack.getCount() > 1 ? ConstantValue.exactly(itemStack.getCount()) : null,
                itemStack.getTag());
    }

    public static LootEntry ofItem(Item item, @Nullable NumberProvider count, @Nullable CompoundTag nbt) {
        if (item == Items.AIR) {
            return empty();
        }

        var functions = new LootFunctionList();

        if (count != null) {
            functions.setCount(count);
        }

        if (nbt != null) {
            functions.addNbt(nbt);
        }


        var origin = new LootItem(item,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                new LootItemCondition[0],
                functions.createVanillaArray());
        return new LootEntry(origin);
    }

    public static LootEntry empty() {
        return new LootEntry(new EmptyLootItem(LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                new LootItemCondition[0],
                new LootItemFunction[0]));
    }

    public static LootEntry ofDynamic(ResourceLocation name) {
        return new LootEntry(new DynamicLoot(name,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                new LootItemCondition[0],
                new LootItemFunction[0]));
    }

    public static LootEntry ofReferece(ResourceLocation lootTable) {
        return new LootEntry(new LootTableReference(lootTable,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                new LootItemCondition[0],
                new LootItemFunction[0]));
    }

    public static LootEntry ofTag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tag));
        return new LootEntry(new TagEntry(tagKey,
                expand,
                LootPoolSingletonContainer.DEFAULT_WEIGHT,
                LootPoolSingletonContainer.DEFAULT_QUALITY,
                new LootItemCondition[0],
                new LootItemFunction[0]));
    }

    public LootPoolSingletonContainer getOrigin() {
        return origin;
    }

    public LootEntry withWeight(int weight) {
        setWeight(weight);
        return this;
    }

    public void setWeight(int weight) {
        getOrigin().weight = Math.max(1, weight);
    }

    public int getWeight() {
        return getOrigin().weight;
    }

    public LootEntry withQuality(int quality) {
        setQuality(quality);
        return this;
    }

    public void setQuality(int quality) {
        getOrigin().quality = Math.max(0, quality);
    }

    public int getQuality() {
        return getOrigin().quality;
    }

    public void setItem(Item item) {
        if (item == Items.AIR) {
            free();
            origin = new EmptyLootItem(getWeight(), getQuality(), getOrigin().conditions, getOrigin().functions);
            return;
        }

        if (getOrigin() instanceof LootItem li) {
            li.item = item;
            return;
        }

        free();
        origin = new LootItem(item, getWeight(), getQuality(), getOrigin().conditions, getOrigin().functions);
    }

    public Item getItem() {
        if (getOrigin() instanceof LootItem li) {
            return li.item;
        }

        return Items.AIR;
    }

    public void setTag(String tag, boolean expand) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tag));
        if (getOrigin() instanceof TagEntry tagEntry) {
            tagEntry.tag = tagKey;
            tagEntry.expand = expand;
            return;
        }

        free();
        origin = new TagEntry(tagKey, expand, getWeight(), getQuality(), getOrigin().conditions, getOrigin().functions);
    }

    public void setTag(String tag) {
        setTag(tag, false);
    }

    @Nullable
    public String getTag() {
        if (getOrigin() instanceof TagEntry tagEntry) {
            return tagEntry.tag.location().toString();
        }

        return null;
    }

    public boolean testTag(String tag) {
        if (tag.startsWith("#")) {
            tag = tag.substring(1);
        }

        return tag.equals(getTag());
    }

    public boolean getExpand() {
        return getOrigin() instanceof TagEntry tagEntry && tagEntry.expand;
    }

    public void setLootTable(ResourceLocation lootTable) {
        if (getOrigin() instanceof LootTableReference ltr) {
            ltr.name = lootTable;
            return;
        }

        free();
        origin = new LootTableReference(lootTable,
                getWeight(),
                getQuality(),
                getOrigin().conditions,
                getOrigin().functions);
    }

    @Nullable
    public ResourceLocation getReference() {
        if (getOrigin() instanceof LootTableReference ltr) {
            return ltr.name;
        }

        return null;
    }

    public void setDynamic(ResourceLocation dynamic) {
        if (getOrigin() instanceof DynamicLoot dl) {
            dl.name = dynamic;
            return;
        }

        free();
        origin = new DynamicLoot(dynamic, getWeight(), getQuality(), getOrigin().conditions, getOrigin().functions);
    }

    @Nullable
    public ResourceLocation getDynamic() {
        if (getOrigin() instanceof DynamicLoot dl) {
            return dl.name;
        }

        return null;
    }

    public boolean isEmpty() {
        return getVanillaType() == LootPoolEntries.EMPTY || getItem() == Items.AIR;
    }

    public boolean isItem() {
        return origin instanceof LootItem;
    }

    public boolean isTag() {
        return origin instanceof TagEntry;
    }

    public boolean isReference() {
        return origin instanceof LootTableReference;
    }

    public boolean isDynamic() {
        return origin instanceof DynamicLoot;
    }

    public LootPoolEntryType getVanillaType() {
        return origin.getType();
    }

    private void free() {
        if (conditions != null) {
            getOrigin().conditions = conditions.createVanillaArray();
            getOrigin().compositeCondition = LootItemConditions.andConditions(getOrigin().conditions);
            conditions = null;
        }

        if (functions != null) {
            getOrigin().functions = functions.createVanillaArray();
            getOrigin().compositeFunction = LootItemFunctions.compose(getOrigin().functions);
            functions = null;
        }
    }

    public LootPoolSingletonContainer saveAndGetOrigin() {
        free();
        return origin;
    }

    public LootEntry modifiers(Consumer<LootFunctionList> callback) {
        if (functions == null) {
            functions = new LootFunctionList(origin.functions);
        }

        callback.accept(functions);
        return this;
    }

    @Override
    public LootEntry when(Consumer<LootConditionList> callback) {
        if (conditions == null) {
            conditions = new LootConditionList(origin.conditions);
        }

        callback.accept(conditions);
        return this;
    }

    @Override
    public boolean test(ItemFilter filter) {
        if (getOrigin() instanceof LootItem li) {
            return filter.test(new ItemStack(li.item));
        }

        if (getOrigin() instanceof TagEntry te && filter instanceof ItemFilter.Tag tf) {
            return tf.tag().equals(te.tag);
        }

        return isEmpty() && filter == ItemFilter.EMPTY;
    }

    @Override
    public void collectDebugInfo(DebugInfo info) {
        Object tmpObject = "";
        if (isItem()) {
            tmpObject = BuiltInRegistries.ITEM.getKey(getItem());
        } else if (isTag()) {
            tmpObject = getTag();
        } else if (isReference()) {
            tmpObject = getReference();
        } else if (isDynamic()) {
            tmpObject = getDynamic();
        }

        if (tmpObject == null) {
            tmpObject = "";
        }

        String valueStr = tmpObject.toString();

        if (!valueStr.isEmpty()) {
            valueStr = " value: \"" + valueStr + "\", ";
        }

        String format = String.format("%s {%s weight: %d, quality: %d }",
                getType(),
                valueStr,
                getWeight(),
                getQuality());
        info.add(format);

        info.push();
        when(conditions -> conditions.collectDebugInfo(info));
        modifiers(functions -> functions.collectDebugInfo(info));
        info.pop();
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public LootEntry addFunction(LootItemFunction lootItemFunction) {
        if (functions == null) {
            functions = new LootFunctionList(origin.functions);
        }

        functions.add(lootItemFunction);
        return this;
    }

    @Nullable
    public ItemStack createItemStack(LootContext context) {
        if (isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (conditions != null) {
            for (LootItemCondition condition : conditions) {
                if (!condition.test(context)) {
                    return null;
                }
            }
        }

        Item item = getItem();
        ItemStack itemStack = new ItemStack(item);
        if (functions != null) {
            for (LootItemFunction function : functions) {
                itemStack = function.apply(itemStack, context);
            }
        }

        return itemStack;
    }
}
