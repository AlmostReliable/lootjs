package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.loot.condition.WrappedDamageSourceCondition;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class DamageSourcePredicateBuilderJS implements LootItemCondition.Builder {
    private final DamageSourcePredicate.Builder vanillaBuilder = new DamageSourcePredicate.Builder();
    private String[] sourceNames;

    public DamageSourcePredicateBuilderJS anyType(String... names) {
        sourceNames = names;
        return this;
    }

    public DamageSourcePredicateBuilderJS isProjectile(boolean flag) {
        vanillaBuilder.isProjectile(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS isExplosion(boolean flag) {
        vanillaBuilder.isExplosion(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassArmor(boolean flag) {
        vanillaBuilder.bypassesArmor(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassInvulnerability(boolean flag) {
        vanillaBuilder.bypassesInvulnerability(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassMagic(boolean flag) {
        vanillaBuilder.bypassesMagic(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS isFire(boolean flag) {
        vanillaBuilder.isFire(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS isMagic(boolean flag) {
        vanillaBuilder.isMagic(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS isLightning(boolean flag) {
        vanillaBuilder.isLightning(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS matchDirectEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        vanillaBuilder.direct(builder.build());
        return this;
    }

    public DamageSourcePredicateBuilderJS matchSourceEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        vanillaBuilder.source(builder.build());
        return this;
    }

    @HideFromJS
    public WrappedDamageSourceCondition build() {
        return new WrappedDamageSourceCondition(vanillaBuilder.build(), sourceNames);
    }
}
