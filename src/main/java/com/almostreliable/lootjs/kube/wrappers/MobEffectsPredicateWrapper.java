package com.almostreliable.lootjs.kube.wrappers;

import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class MobEffectsPredicateWrapper {

    public static MobEffectsPredicate.Builder ofBuilder(@Nullable Object o) {
        if (o instanceof MobEffectsPredicate.Builder b) {
            return b;
        }

        MobEffectsPredicate.Builder effects = MobEffectsPredicate.Builder.effects();
        write(o, effects::and);
        return effects;
    }

    public static MobEffectsPredicate of(@Nullable Object o) {
        if (o instanceof MobEffectsPredicate.Builder b) {
            //noinspection OptionalGetWithoutIsPresent
            return b.build().get();
        }

        if (o instanceof MobEffectsPredicate m) {
            return m;
        }

        Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = new HashMap<>();
        write(o, effectMap::put);
        return new MobEffectsPredicate(effectMap);
    }


    private static void write(@Nullable Object o, BiConsumer<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> onAdd) {
        if (o instanceof List<?> list) {
            for (Object entry : list) {
                writeSingle(entry, onAdd);
            }

            return;
        }

        writeSingle(o, onAdd);
    }

    private static void writeSingle(@Nullable Object o, BiConsumer<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> onAdd) {
        if (o instanceof String str) {
            getEffect(str).ifPresent(h -> onAdd.accept(h, new MobEffectsPredicate.MobEffectInstancePredicate()));
        }

        if (o instanceof Map<?, ?> map) {
            getEffect(map.get("id")).ifPresent(h -> {
                onAdd.accept(h, createPredicate(map));
            });
        }
    }

    private static Optional<? extends Holder<MobEffect>> getEffect(@Nullable Object o) {
        if (!(o instanceof String str)) {
            return Optional.empty();
        }

        return BuiltInRegistries.MOB_EFFECT.getHolder(ResourceKey.create(Registries.MOB_EFFECT,
                new ResourceLocation(str)));
    }

    @Nullable
    private static Boolean tryGetBoolean(@Nullable Object o) {
        if (o instanceof Boolean b) {
            return b;
        }

        return null;
    }

    private static MobEffectsPredicate.MobEffectInstancePredicate createPredicate(Map<?, ?> map) {
        var amplifier = MinMaxBoundsWrapper.ofMinMaxInt(map.get("amplifier"));
        var duration = MinMaxBoundsWrapper.ofMinMaxInt(map.get("duration"));

        var a = tryGetBoolean(map.get("ambient"));
        var v = tryGetBoolean(map.get("visible"));

        return new MobEffectsPredicate.MobEffectInstancePredicate(amplifier,
                duration,
                Optional.ofNullable(a),
                Optional.ofNullable(v));
    }
}
