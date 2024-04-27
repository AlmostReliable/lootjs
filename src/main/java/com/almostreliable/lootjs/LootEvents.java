package com.almostreliable.lootjs;

import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LootEvents {

    private static final List<Consumer<WritableRegistry<LootTable>>> TABLE_EVENT_LISTENERS = new ArrayList<>();
    private static final List<Consumer<Map<ResourceLocation, IGlobalLootModifier>>> MODIFIER_EVENT_LISTENERS = new ArrayList<>();

    public static void listen(Consumer<WritableRegistry<LootTable>> listener) {
        TABLE_EVENT_LISTENERS.add(listener);
    }

    public static void invoke(WritableRegistry<LootTable> registry) {
        TABLE_EVENT_LISTENERS.forEach(listener -> listener.accept(registry));
    }

    public static void listenModifiers(Consumer<Map<ResourceLocation, IGlobalLootModifier>> listener) {
        MODIFIER_EVENT_LISTENERS.add(listener);
    }

    public static void invokeModifiers(Map<ResourceLocation, IGlobalLootModifier> modifiers) {
        MODIFIER_EVENT_LISTENERS.forEach(listener -> listener.accept(modifiers));
    }
}
