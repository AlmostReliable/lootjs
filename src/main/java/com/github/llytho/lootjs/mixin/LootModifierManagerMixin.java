package com.github.llytho.lootjs.mixin;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.kube.LootModificationEventJS;
import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.util.ResourceLocation;
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
        new LootModificationEventJS(locations).post(ScriptType.SERVER, "lootjs");
        locations.forEach(originalAction);
    }
}
