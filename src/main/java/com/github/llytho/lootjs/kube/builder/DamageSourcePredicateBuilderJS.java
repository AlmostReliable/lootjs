package com.github.llytho.lootjs.kube.builder;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.criterion.DamageSourcePredicate;

import java.util.function.Consumer;

public class DamageSourcePredicateBuilderJS {
    private final DamageSourcePredicate.Builder vanillaBuilder = new DamageSourcePredicate.Builder();

    public DamageSourcePredicateBuilderJS isProjectile(boolean flag) {
        vanillaBuilder.isProjectile = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS isExplosion(boolean flag) {
        vanillaBuilder.isExplosion = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassArmor(boolean flag) {
        vanillaBuilder.bypassesArmor = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassInvulnerability(boolean flag) {
        vanillaBuilder.bypassesInvulnerability = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS doesBypassMagic(boolean flag) {
        vanillaBuilder.bypassesMagic = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS isFire(boolean flag) {
        vanillaBuilder.isFire = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS isMagic(boolean flag) {
        vanillaBuilder.isMagic = flag;
        return this;
    }

    public DamageSourcePredicateBuilderJS isLightning(boolean flag) {
        vanillaBuilder.isLightning(flag);
        return this;
    }

    public DamageSourcePredicateBuilderJS matchDirectEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        vanillaBuilder.directEntity = builder.build();
        return this;
    }

    public DamageSourcePredicateBuilderJS matchSourceEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS builder = new EntityPredicateBuilderJS();
        action.accept(builder);
        vanillaBuilder.sourceEntity = builder.build();
        return this;
    }

    @HideFromJS
    public DamageSourcePredicate build() {
        return vanillaBuilder.build();
    }
}