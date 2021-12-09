package com.github.llytho.lootjs.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;

public class LootConditionTypes {
    public static final LootConditionType UNUSED = create();

    private static LootConditionType create() {
        new LootConditionType(new UnusedSerializer());
    }

    private static class UnusedSerializer implements ILootSerializer<ILootCondition> {
        @Override
        public void serialize(JsonObject pJson, ILootCondition pValue, JsonSerializationContext pSerializationContext) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ILootCondition deserialize(JsonObject pJson, JsonDeserializationContext pSerializationContext) {
            throw new UnsupportedOperationException();
        }
    }
}
