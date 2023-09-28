package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootContextType;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.loot.table.entry.LootEntry;
import com.almostreliable.lootjs.util.LootContextUtils;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// TODO use Native class
public class LootContextJS {
    protected final LootContext context;
    private final ILootContextData data;
    private BlockContainerJS cachedBlockContainer;

    public LootContextJS(LootContext context) {
        this.context = context;
        this.data = context.getParamOrNull(LootJSParamSets.DATA);
        assert data != null;
    }

    public ResourceLocation getLootTableId() {
        return LootJSPlatform.INSTANCE.getQueriedLootTableId(context);
    }

    public LootContextType getType() {
        return data.getLootContextType();
    }

    public boolean isCanceled() {
        return data.isCanceled();
    }

    public void cancel() {
        data.setCanceled(true);
    }

    public Map<String, Object> getCustomData() {
        return data.getCustomData();
    }

    public Vec3 getPosition() {
        Vec3 vec = context.getParamOrNull(LootContextParams.ORIGIN);
        if (vec != null) {
            return vec;
        }

        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null) {
            return entity.position();
        }

        ConsoleJS.SERVER.warn("No position found. This should not happen");
        return Vec3.ZERO;
    }

    public BlockPos getBlockPos() {
        Vec3 position = getPosition();
        return new BlockPos((int) position.x, (int) position.y, (int) position.z);
    }

    @Nullable
    public Entity getEntity() {
        return context.getParamOrNull(LootContextParams.THIS_ENTITY);
    }

    @Nullable
    public Entity getKillerEntity() {
        return context.getParamOrNull(LootContextParams.KILLER_ENTITY);
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return LootContextUtils.getPlayerOrNull(context);
    }

    @Nullable
    public DamageSource getDamageSource() {
        return context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
    }

    public ItemStack getTool() {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null) {
            return ItemStack.EMPTY;
        }

        return tool;
    }

    @Nullable
    public BlockContainerJS getDestroyedBlock() {
        if (cachedBlockContainer == null) {
            BlockState blockStateInContext = context.getParamOrNull(LootContextParams.BLOCK_STATE);
            if (blockStateInContext == null) return null;
            BlockPos blockPos = getBlockPos();
            cachedBlockContainer = new BlockContainerJS(context.getLevel(), blockPos) {
                @Override
                public BlockState getBlockState() {
                    return blockStateInContext;
                }
            };
        }

        return cachedBlockContainer;
    }

    public boolean isExploded() {
        return context.hasParam(LootContextParams.EXPLOSION_RADIUS);
    }

    public float getExplosionRadius() {
        Float f = context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        return f != null ? f : 0f;
    }

    public ServerLevel getLevel() {
        return context.getLevel();
    }

    @Nullable
    public MinecraftServer getServer() {
        return getLevel().getServer();
    }

    public float getLuck() {
        return context.getLuck();
    }

    public int getLooting() {
        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (killer instanceof LivingEntity asLiving) {
            return EnchantmentHelper.getMobLooting(asLiving);
        }
        return 0;
    }

    public RandomSource getRandom() {
        return context.getRandom();
    }

    public LootContext getVanillaContext() {
        return context;
    }

    public int lootSize() {
        return data.getGeneratedLoot().size();
    }

    public void addLoot(Object unknown) {
        if (unknown instanceof String asStr) {
            ItemStack itemStack = ItemStackJS.of(unknown);
            if (!itemStack.isEmpty()) {
                data.getGeneratedLoot().add(itemStack);
            }

            return;
        }

        LootEntry lootEntry = LootContainerWrapper.ofSingle(unknown);
        lootEntry.saveAndGetOrigin().expand(context, e -> {
            e.createItemStack(itemStack -> data.getGeneratedLoot().add(itemStack), context);
        });
    }

    public void removeLoot(ItemFilter itemFilter) {
        data.getGeneratedLoot().removeIf(itemFilter);
    }

    public List<ItemStack> findLoot(ItemFilter itemFilter) {
        return data
                .getGeneratedLoot()
                .stream()
                .filter(itemFilter)
                .map(ItemStackJS::of)
                .collect(Collectors.toList());
    }

    public List<ItemStack> getLoot() {
        return data.getGeneratedLoot();
    }

    public boolean hasLoot(ItemFilter ingredient) {
        return !findLoot(ingredient).isEmpty();
    }

    public void forEachLoot(Consumer<ItemStack> action) {
        data.getGeneratedLoot().forEach(itemStack -> action.accept(ItemStackJS.of(itemStack)));
    }
}
