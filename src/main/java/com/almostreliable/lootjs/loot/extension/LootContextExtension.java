package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.util.LootContextUtils;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;

@RemapPrefixForJS("lootjs$")
public interface LootContextExtension {

    static LootContextExtension cast(LootContext context) {
        return (LootContextExtension) context;
    }

    LootContext lootjs$self();

    default boolean lootjs$isType(LootType type) {
        return type == lootjs$getType();
    }

    LootType lootjs$getType();
    default ResourceLocation lootjs$getLootTableId() {
        return LootJSPlatform.INSTANCE.getQueriedLootTableId(lootjs$self());
    }

    default Vec3 lootjs$getPosition() {
        Vec3 pos = lootjs$self().getParamOrNull(LootContextParams.ORIGIN);
        if (pos != null) {
            return pos;
        }

        Entity entity = lootjs$getEntity();
        if (entity != null) {
            return entity.position();
        }

        LootJS.LOG.warn("Loot table {} has no position. This should not happen", lootjs$getLootTableId());
        return Vec3.ZERO;
    }

    @Nullable
    default Entity lootjs$getEntity() {
        return lootjs$self().getParamOrNull(LootContextParams.THIS_ENTITY);
    }

    @Nullable
    default Entity lootjs$getKillerEntity() {
        return lootjs$self().getParamOrNull(LootContextParams.KILLER_ENTITY);
    }

    @Nullable
    default ServerPlayer lootjs$getKillerPlayer() {
        return LootContextUtils.getPlayerOrNull(lootjs$self());
    }

    @Nullable
    default DamageSource lootjs$getDamageSource() {
        return lootjs$self().getParamOrNull(LootContextParams.DAMAGE_SOURCE);
    }

    default ItemStack lootjs$getTool() {
        ItemStack tool = lootjs$self().getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            return tool;
        }

        return ItemStack.EMPTY;
    }

    default boolean lootjs$isExploded() {
        return lootjs$self().hasParam(LootContextParams.EXPLOSION_RADIUS);
    }

    default float lootjs$getExplosionRadius() {
        Float f = lootjs$self().getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        return f != null ? f : 0f;
    }

    @Nullable
    default MinecraftServer lootjs$getServer() {
        return lootjs$self().getLevel().getServer();
    }

    default int lootjs$getLooting() {
        Entity killer = lootjs$self().getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (killer instanceof LivingEntity asLiving) {
            return EnchantmentHelper.getMobLooting(asLiving);
        }

        return 0;
    }

    Map<String, Object> lootjs$getData();
}
