package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.LootJS;
import com.almostreliable.lootjs.loot.LootConditionList;
import com.almostreliable.lootjs.loot.extension.LootItemFunctionExtension;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(LootItemFunction.class)
public interface LootItemFunctionMixin extends LootItemFunctionExtension {

    @Override
    default LootItemFunction lootjs$when(Consumer<LootConditionList> consumer) {
        LootJS.DEBUG_ACTION.accept(
                "Non conditional loot functions are not supported yet! Added conditions will be ignored!");
        return (LootItemFunction) this;
    }
}
