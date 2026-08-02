package com.almostreliable.lootjs;

import com.almostreliable.lootjs.loot.extension.LootTableExtension;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class LootEvents {

    @Nullable
    private static Consumer<WritableRegistry<LootTable>> TABLE_EVENT_LISTENERS;
    @Nullable
    private static Consumer<Map<Identifier, IGlobalLootModifier>> MODIFIER_EVENT_LISTENERS;

    public static void listen(Consumer<WritableRegistry<LootTable>> listener) {
        if (TABLE_EVENT_LISTENERS == null) {
            TABLE_EVENT_LISTENERS = listener;
            return;
        }

        TABLE_EVENT_LISTENERS = TABLE_EVENT_LISTENERS.andThen(listener);
    }

    public static void invoke(WritableRegistry<LootTable> registry) {
        if (TABLE_EVENT_LISTENERS != null) {
            TABLE_EVENT_LISTENERS.accept(registry);
            for (LootTable lootTable : registry) {
                ((LootTableExtension) lootTable).lootjs$recompose();
            }
        }
    }

    public static void listenModifiers(Consumer<Map<Identifier, IGlobalLootModifier>> listener) {
        if (MODIFIER_EVENT_LISTENERS == null) {
            MODIFIER_EVENT_LISTENERS = listener;
            return;
        }

        MODIFIER_EVENT_LISTENERS = MODIFIER_EVENT_LISTENERS.andThen(listener);
    }

    public static void invokeModifiers(Map<Identifier, IGlobalLootModifier> modifiers) {
        if (MODIFIER_EVENT_LISTENERS != null) {
            MODIFIER_EVENT_LISTENERS.accept(modifiers);
        }
    }
}
