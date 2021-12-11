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
        return new LootConditionType(new UnusedSerializer());
    }

    private static class UnusedSerializer implements ILootSerializer<ILootCondition> {
        @Override
        public void serialize(JsonObject json, ILootCondition value, JsonSerializationContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ILootCondition deserialize(JsonObject serializer, JsonDeserializationContext context) {
            throw new UnsupportedOperationException();
        }
    }
}
