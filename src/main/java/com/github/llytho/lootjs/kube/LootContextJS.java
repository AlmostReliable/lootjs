package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.util.LootContextUtils;
import dev.latvian.mods.kubejs.entity.DamageSourceJS;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.level.world.BlockContainerJS;
import dev.latvian.mods.kubejs.level.world.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.server.ServerJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LootContextJS {
    protected final LootContext context;
    private final ILootContextData data;
    private BlockContainerJS cachedBlockContainer;

    public LootContextJS(LootContext context) {
        this.context = context;
        this.data = context.getParamOrNull(Constants.DATA);
        assert data != null;
    }

    public ResourceLocation getLootTableId() {
        return context.getQueriedLootTableId();
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
        return new BlockPos(position.x, position.y, position.z);
    }

    @Nullable
    public EntityJS getEntity() {
        return getLevel().getEntity(context.getParamOrNull(LootContextParams.THIS_ENTITY));
    }

    @Nullable
    public EntityJS getKillerEntity() {
        return getLevel().getEntity(context.getParamOrNull(LootContextParams.KILLER_ENTITY));
    }

    @Nullable
    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(LootContextUtils.getPlayerOrNull(context));
    }

    @Nullable
    public DamageSourceJS getDamageSource() {
        DamageSource damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if (damageSource == null) {
            return null;
        }

        return new DamageSourceJS(getLevel(), damageSource);
    }

    public ItemStackJS getTool() {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null) {
            return ItemStackJS.EMPTY;
        }

        return new ItemStackJS(tool);
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

    public LevelJS getLevel() {
        return UtilsJS.getLevel(context.getLevel());
    }

    @Nullable
    public ServerJS getServer() {
        return getLevel().getServer();
    }

    public float getLuck() {
        return context.getLuck();
    }

    public int getLooting() {
        return context.getLootingModifier();
    }

    public Random getRandom() {
        return context.getRandom();
    }

    public LootContext getVanillaContext() {
        return context;
    }

    public int lootSize() {
        return data.getGeneratedLoot().size();
    }

    public void addLoot(ItemStackJS itemStack) {
        data.getGeneratedLoot().add(itemStack.getItemStack());
    }

    public void removeLoot(IngredientJS ingredient) {
        data.getGeneratedLoot().removeIf(ingredient::testVanilla);
    }

    public List<ItemStackJS> findLoot(IngredientJS ingredient) {
        return data
                .getGeneratedLoot()
                .stream()
                .map(ItemStackJS::of)
                .filter(ingredient::test)
                .collect(Collectors.toList());
    }

    public boolean hasLoot(IngredientJS ingredient) {
        return !findLoot(ingredient).isEmpty();
    }

    public void forEachLoot(Consumer<ItemStackJS> action) {
        data.getGeneratedLoot().forEach(itemStack -> action.accept(ItemStackJS.of(itemStack)));
    }
}
