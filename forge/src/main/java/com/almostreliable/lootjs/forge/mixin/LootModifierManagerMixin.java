package com.almostreliable.lootjs.forge.mixin;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.forge.kube.LootModificationForgeEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.LootModifierManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.function.Consumer;

@Mixin(LootModifierManager.class)
public class LootModifierManagerMixin {

    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;forEach(Ljava/util/function/Consumer;)V", ordinal = 0), remap = false)
    private void lootModifierReload(ArrayList<ResourceLocation> locations, Consumer<ResourceLocation> originalAction) {
        LootModificationsAPI.reload();
        new LootModificationForgeEventJS(locations).post(ScriptType.SERVER, "lootjs");
        locations.forEach(originalAction);
    }
}
