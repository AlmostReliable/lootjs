package com.almostreliable.lootjs.kube.builder;

import com.almostreliable.lootjs.filters.ItemFilter;
import com.almostreliable.lootjs.filters.Resolver;
import com.almostreliable.lootjs.predicate.CustomItemPredicate;
import com.almostreliable.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.almostreliable.lootjs.predicate.MultiEntityTypePredicate;
import com.almostreliable.lootjs.util.Utils;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EntityPredicateBuilderJS implements ExtendedEntityFlagsPredicate.IBuilder<EntityPredicate> {
    private final EntityPredicate.Builder vanillaBuilder = EntityPredicate.Builder.entity();
    private final Map<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> effects = new HashMap<>();
    private final ExtendedEntityFlagsPredicate.Builder flagsBuilder = new ExtendedEntityFlagsPredicate.Builder();
    @Nullable
    private EntityEquipmentPredicate.Builder equipmentPredicateBuilder;

    @Nullable
    private FluidPredicate fluidPredicate;
    @Nullable
    private BlockPredicate blockPredicate;

    @Override
    public EntityPredicateBuilderJS isOnFire(boolean flag) {
        flagsBuilder.isOnFire(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isCrouching(boolean flag) {
        flagsBuilder.isCrouching(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isSprinting(boolean flag) {
        flagsBuilder.isSprinting(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isSwimming(boolean flag) {
        flagsBuilder.isSwimming(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isBaby(boolean flag) {
        flagsBuilder.isBaby(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isInWater(boolean flag) {
        flagsBuilder.isInWater(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isUnderWater(boolean flag) {
        flagsBuilder.isUnderWater(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isMonster(boolean flag) {
        flagsBuilder.isMonster(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isCreature(boolean flag) {
        flagsBuilder.isCreature(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isOnGround(boolean flag) {
        flagsBuilder.isOnGround(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isUndeadMob(boolean flag) {
        flagsBuilder.isUndeadMob(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isArthropodMob(boolean flag) {
        flagsBuilder.isArthropodMob(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isIllegarMob(boolean flag) {
        flagsBuilder.isIllegarMob(flag);
        return this;
    }

    @Override
    public EntityPredicateBuilderJS isWaterMob(boolean flag) {
        flagsBuilder.isWaterMob(flag);
        return this;
    }

    public EntityPredicateBuilderJS matchBlock(Resolver resolver, Map<String, String> propertyMap) {
        BlockPredicate.Builder builder = BlockPredicate.Builder.block();
        if (resolver instanceof Resolver.ByEntry byEntry) {
            Block block = byEntry.resolve(BuiltInRegistries.BLOCK);
            StatePropertiesPredicate.Builder properties = Utils.createProperties(block, propertyMap);
            builder.setProperties(properties.build());
            builder.of(block);
        } else if (resolver instanceof Resolver.ByTagKey byTag) {
            TagKey<Block> tagKey = byTag.resolve(Registries.BLOCK);
            builder.of(tagKey);
        }
        blockPredicate = builder.build();
        return this;
    }

    public EntityPredicateBuilderJS matchBlock(Resolver resolver) {
        return matchBlock(resolver, new HashMap<>());
    }

    public EntityPredicateBuilderJS matchFluid(Resolver resolver) {
        if (resolver instanceof Resolver.ByEntry byEntry) {
            Fluid fluid = byEntry.resolve(BuiltInRegistries.FLUID);
            fluidPredicate = new FluidPredicate(null, fluid, StatePropertiesPredicate.ANY);
        } else if (resolver instanceof Resolver.ByTagKey byTag) {
            TagKey<Fluid> tagKey = byTag.resolve(Registries.FLUID);
            fluidPredicate = new FluidPredicate(tagKey, null, StatePropertiesPredicate.ANY);
        }
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(MobEffect effect, int amplifier) {
        MinMaxBounds.Ints bounds = MinMaxBounds.Ints.atLeast(amplifier);
        MobEffectsPredicate.MobEffectInstancePredicate predicate = new MobEffectsPredicate.MobEffectInstancePredicate(
                bounds,
                MinMaxBounds.Ints.ANY,
                null,
                null);
        effects.put(effect, predicate);
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(MobEffect effect) {
        return hasEffect(effect, 0);
    }

    public EntityPredicateBuilderJS nbt(CompoundTag nbt) {
        vanillaBuilder.nbt(new NbtPredicate(nbt));
        return this;
    }

    public EntityPredicateBuilderJS matchMount(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS mountPredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(mountPredicateBuilder);
        vanillaBuilder.vehicle(mountPredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchTargetedEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS vehiclePredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(vehiclePredicateBuilder);
        vanillaBuilder.targetedEntity(vehiclePredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchSlot(EquipmentSlot slot, ItemFilter itemFilter) {
        if (equipmentPredicateBuilder == null) {
            equipmentPredicateBuilder = new EntityEquipmentPredicate.Builder();
        }

        CustomItemPredicate predicate = new CustomItemPredicate(itemFilter);
        switch (slot) {
            case MAINHAND -> equipmentPredicateBuilder.mainhand(predicate);
            case OFFHAND -> equipmentPredicateBuilder.offhand(predicate);
            case FEET -> equipmentPredicateBuilder.feet(predicate);
            case LEGS -> equipmentPredicateBuilder.legs(predicate);
            case CHEST -> equipmentPredicateBuilder.chest(predicate);
            case HEAD -> equipmentPredicateBuilder.head(predicate);
        }
        return this;
    }

    public EntityPredicateBuilderJS anyType(Resolver... resolvers) {
        List<EntityType<?>> types = new ArrayList<>();
        List<TagKey<EntityType<?>>> tags = new ArrayList<>();

        for (Resolver resolver : resolvers) {
            if (resolver instanceof Resolver.ByEntry byEntry) {
                types.add(byEntry.resolve(BuiltInRegistries.ENTITY_TYPE));
            } else if (resolver instanceof Resolver.ByTagKey byTag) {
                tags.add(byTag.resolve(Registries.ENTITY_TYPE));
            }
        }

        vanillaBuilder.entityType(new MultiEntityTypePredicate(tags, types));
        return this;
    }

    public EntityPredicate build() {
        tryBuildFLags();
        tryBuildEffects();
        tryBuildEquipment();
        tryBuildLocation();
        return vanillaBuilder.build();
    }

    private void tryBuildLocation() {
        LocationPredicate.Builder locationBuilder = new LocationPredicate.Builder();
        if (blockPredicate != null) {
            locationBuilder.setBlock(blockPredicate);
        }

        if (fluidPredicate != null) {
            locationBuilder.setFluid(fluidPredicate);
        }
        vanillaBuilder.located(locationBuilder.build());
    }

    private void tryBuildEquipment() {
        if (equipmentPredicateBuilder != null) {
            vanillaBuilder.equipment(equipmentPredicateBuilder.build());
        }
    }


    private void tryBuildEffects() {
        if (!effects.isEmpty()) {
            vanillaBuilder.effects(new MobEffectsPredicate(effects));
        }
    }

    private void tryBuildFLags() {
        vanillaBuilder.flags(flagsBuilder.build());
    }
}
