package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.loot.condition.WrappedDamageSourceCondition;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class DamageSourcePredicateBuilderJS implements LootItemCondition.Builder {
    private final DamageSourcePredicate.Builder vanillaBuilder = new DamageSourcePredicate.Builder();
    private String[] sourceNames;

    public DamageSourcePredicateBuilderJS anyType(String... names) {
        sourceNames = names;
        return this;
    }

    public DamageSourcePredicateBuilderJS is(ResourceLocation tag) {
        vanillaBuilder.tag(TagPredicate.is(TagKey.create(Registries.DAMAGE_TYPE, tag)));
        return this;
    }

    public DamageSourcePredicateBuilderJS isNot(ResourceLocation tag) {
        vanillaBuilder.tag(TagPredicate.isNot(TagKey.create(Registries.DAMAGE_TYPE, tag)));
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
