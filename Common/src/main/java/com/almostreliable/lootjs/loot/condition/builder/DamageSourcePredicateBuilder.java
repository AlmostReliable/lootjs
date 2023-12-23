package com.almostreliable.lootjs.loot.condition.builder;

import com.almostreliable.lootjs.loot.condition.WrappedDamageSourceCondition;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class DamageSourcePredicateBuilder implements LootItemCondition.Builder {
    private final DamageSourcePredicate.Builder vanillaBuilder = new DamageSourcePredicate.Builder();

    public DamageSourcePredicateBuilder is(ResourceLocation tag) {
        vanillaBuilder.tag(TagPredicate.is(TagKey.create(Registries.DAMAGE_TYPE, tag)));
        return this;
    }

    public DamageSourcePredicateBuilder isNot(ResourceLocation tag) {
        vanillaBuilder.tag(TagPredicate.isNot(TagKey.create(Registries.DAMAGE_TYPE, tag)));
        return this;
    }

    public DamageSourcePredicateBuilder matchDirectEntity(Consumer<EntityPredicateBuilder> action) {
        EntityPredicateBuilder builder = new EntityPredicateBuilder();
        action.accept(builder);
        vanillaBuilder.direct(builder.build());
        return this;
    }

    public DamageSourcePredicateBuilder matchSourceEntity(Consumer<EntityPredicateBuilder> action) {
        EntityPredicateBuilder builder = new EntityPredicateBuilder();
        action.accept(builder);
        vanillaBuilder.source(builder.build());
        return this;
    }

    @HideFromJS
    public DamageSourceCondition build() {
        return (DamageSourceCondition) DamageSourceCondition.hasDamageSource(vanillaBuilder).build();
    }
}
