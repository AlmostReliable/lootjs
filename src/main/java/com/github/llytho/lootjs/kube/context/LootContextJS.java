package com.github.llytho.lootjs.kube.context;

import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.core.LootContextType;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.player.PlayerJS;
import dev.latvian.kubejs.server.ServerJS;
import dev.latvian.kubejs.util.UtilsJS;
import dev.latvian.kubejs.world.WorldJS;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootContextJS {
    protected final LootContext context;
    private final ILootContextData data;

    public LootContextJS(LootContext pContext) {
        this.context = pContext;
        this.data = pContext.getParamOrNull(Constants.DATA);
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

    public Map<String, Object> getData() {
        return data.getAdditionalData();
    }

    // TODO YEET and move probably to EntityLootContext idk
//    @Nullable
//    public EntityJS getEntity() {
//        if (cachedEntity != null) {
//            return cachedEntity;
//        }
//
//        cachedEntity = getLevel().getEntity(context.getParamOrNull(LootParameters.THIS_ENTITY));
//        return cachedEntity;
//    }

//    public DamageSourceJS getDamageSource() {
//        context.getParamOrNull(LootParameters.TOOL)
//    }

    @Nullable
    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(context.getParamOrNull(LootParameters.THIS_ENTITY));
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

    public List<ItemStack> getVanillaLoot() {
        return data.getGeneratedLoot();
    }

    public void addItem(IngredientJS pIngredient) {
        if (pIngredient.isEmpty()) {
            throw new IllegalArgumentException("Given item can't be empty or air");
        }

        for (ItemStackJS stack : pIngredient.getStacks()) {
            data.getGeneratedLoot().add(stack.getItemStack());
        }
    }

}
