package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.kube.KubeOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.latvian.mods.kubejs.holder.NamespaceHolderSet;
import dev.latvian.mods.kubejs.holder.RegExHolderSet;
import dev.latvian.mods.kubejs.util.RegExpKJS;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecMixin<E> {

    @Shadow @Final private ResourceKey<? extends Registry<E>> registryKey;

    @Inject(method = "decode", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"), cancellable = true)
    private <T> void lootjs$injectHolderCodec(DynamicOps<T> dynamicOps, T value, CallbackInfoReturnable<DataResult<?>> cir) {
        if (!(dynamicOps instanceof KubeOps kubeOps)) {
            return;
        }

        var namespaceSet = lootjs$tryByNamespace(kubeOps, value);
        if (namespaceSet != null) {
            var result = DataResult.success(Pair.of(namespaceSet, value));
            cir.setReturnValue(result);
            return;
        }

        var regexSet = lootjs$tryByRegex(kubeOps, value);
        if (regexSet != null) {
            var result = DataResult.success(Pair.of(regexSet, value));
            cir.setReturnValue(result);
        }
    }

    @Unique
    @Nullable
    private <T> HolderSet<E> lootjs$tryByNamespace(KubeOps kubeOps, T value) {
        if (!(value instanceof String str && str.startsWith("@"))) {
            return null;
        }

        if (!(kubeOps.getter(this.registryKey).orElse(null) instanceof HolderLookup.RegistryLookup<E> lookup)) {
            return null;
        }

        var namespace = str.substring(1);
        return new NamespaceHolderSet<>(lookup, namespace);
    }

    @Unique
    @Nullable
    private <T> HolderSet<E> lootjs$tryByRegex(KubeOps kubeOps, T value) {
        Pattern pattern = RegExpKJS.wrap(value);
        if (pattern == null) {
            return null;
        }

        if (!(kubeOps.getter(this.registryKey).orElse(null) instanceof HolderLookup.RegistryLookup<E> lookup)) {
            return null;
        }

        return new RegExHolderSet<>(lookup, pattern);
    }
}
