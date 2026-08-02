package com.almostreliable.lootjs.loot.extension;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.core.LootType;
import com.almostreliable.lootjs.util.LootContextUtils;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;

@RemapPrefixForJS("lootjs$")
public interface LootContextExtension {

    static LootContextExtension cast(LootContext context) {
        return (LootContextExtension) context;
    }

    LootContext lootjs$self();

    default Identifier lootjs$getId() {
        return lootjs$self().getQueriedLootTableId();
    }

    default boolean lootjs$isType(LootType type) {
        return type == lootjs$getType();
    }

    LootType lootjs$getType();

    default Vec3 lootjs$getPosition() {
        Vec3 pos = lootjs$self().getOptionalParameter(LootContextParams.ORIGIN);
        if (pos != null) {
            return pos;
        }

        Entity entity = lootjs$getEntity();
        if (entity != null) {
            return entity.position();
        }

        LootJS.LOG.warn("Loot table {} has no position. This should not happen", lootjs$self().getQueriedLootTableId());
        return Vec3.ZERO;
    }

    @Nullable
    default Entity lootjs$getEntity() {
        return lootjs$self().getOptionalParameter(LootContextParams.THIS_ENTITY);
    }

    @Nullable
    default Entity lootjs$getAttackingEntity() {
        return lootjs$self().getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
    }

    @Nullable
    default ServerPlayer lootjs$getKillerPlayer() {
        return LootContextUtils.getPlayerOrNull(lootjs$self());
    }

    @Nullable
    default DamageSource lootjs$getDamageSource() {
        return lootjs$self().getOptionalParameter(LootContextParams.DAMAGE_SOURCE);
    }

    default ItemStack lootjs$getTool() {
        var tool = lootjs$self().getOptionalParameter(LootContextParams.TOOL);
        if (tool instanceof ItemStack itemStack) {
            return itemStack;
        }

        return ItemStack.EMPTY;
    }

    default boolean lootjs$isExploded() {
        return lootjs$self().hasParameter(LootContextParams.EXPLOSION_RADIUS);
    }

    default float lootjs$getExplosionRadius() {
        Float f = lootjs$self().getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
        return f != null ? f : 0f;
    }

    @Nullable
    default MinecraftServer lootjs$getServer() {
        return lootjs$self().getLevel().getServer();
    }

    Map<String, Object> lootjs$getData();
}
