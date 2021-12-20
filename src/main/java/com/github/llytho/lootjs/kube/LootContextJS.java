package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import com.github.llytho.lootjs.util.LootContextUtils;
import dev.latvian.kubejs.entity.DamageSourceJS;
import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.player.PlayerJS;
import dev.latvian.kubejs.server.ServerJS;
import dev.latvian.kubejs.util.ConsoleJS;
import dev.latvian.kubejs.util.UtilsJS;
import dev.latvian.kubejs.world.BlockContainerJS;
import dev.latvian.kubejs.world.WorldJS;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

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

    public Vector3d getPosition() {
        Vector3d vec = context.getParamOrNull(LootParameters.ORIGIN);
        if (vec != null) {
            return vec;
        }

        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (entity != null) {
            return entity.position();
        }

        ConsoleJS.SERVER.warn("No position found. This should not happen");
        return Vector3d.ZERO;
    }

    public BlockPos getBlockPos() {
        Vector3d position = getPosition();
        return new BlockPos(position.x, position.y, position.z);
    }

    @Nullable
    public EntityJS getEntity() {
        return getLevel().getEntity(context.getParamOrNull(LootParameters.THIS_ENTITY));
    }

    @Nullable
    public EntityJS getKillerEntity() {
        return getLevel().getEntity(context.getParamOrNull(LootParameters.KILLER_ENTITY));
    }

    @Nullable
    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(LootContextUtils.getPlayerOrNull(context));
    }

    @Nullable
    public DamageSourceJS getDamageSource() {
        DamageSource damageSource = context.getParamOrNull(LootParameters.DAMAGE_SOURCE);
        if (damageSource == null) {
            return null;
        }

        return new DamageSourceJS(getLevel(), damageSource);
    }

    public ItemStackJS getTool() {
        ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        if (tool == null) {
            return ItemStackJS.EMPTY;
        }

        return new ItemStackJS(tool);
    }

    @Nullable
    public BlockContainerJS getDestroyedBlock() {
        if (cachedBlockContainer == null) {
            BlockState blockStateInContext = context.getParamOrNull(LootParameters.BLOCK_STATE);
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
        return context.hasParam(LootParameters.EXPLOSION_RADIUS);
    }

    public float getExplosionRadius() {
        Float f = context.getParamOrNull(LootParameters.EXPLOSION_RADIUS);
        return f != null ? f : 0f;
    }

    public WorldJS getLevel() {
        return UtilsJS.getWorld(context.getLevel());
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
