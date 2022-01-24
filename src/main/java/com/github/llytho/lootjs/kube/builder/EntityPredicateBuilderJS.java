package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.predicate.CustomItemPredicate;
import com.github.llytho.lootjs.predicate.ExtendedEntityFlagsPredicate;
import com.github.llytho.lootjs.predicate.MultiEntityTypePredicate;
import com.github.llytho.lootjs.util.TagOrEntry;
import com.github.llytho.lootjs.util.Utils;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.advancements.critereon.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

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

    public EntityPredicateBuilderJS catType(ResourceLocation type) {
        vanillaBuilder.catType(type);
        return this;
    }

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

    public EntityPredicateBuilderJS matchBlock(String idOrTag, Map<String, String> propertyMap) {
        TagOrEntry<Block> tagOrEntry = Utils.getTagOrEntry(ForgeRegistries.BLOCKS, idOrTag);
        StatePropertiesPredicate.Builder properties = Utils.createProperties(tagOrEntry.getFirst(), propertyMap);
        BlockPredicate.Builder builder = BlockPredicate.Builder.block().setProperties(properties.build());
        if (tagOrEntry.tag != null) {
            builder.of(tagOrEntry.tag);
        }
        if (tagOrEntry.entry != null) {
            builder.of(tagOrEntry.entry);
        }
        blockPredicate = builder.build();
        return this;
    }

    public EntityPredicateBuilderJS matchBlock(String idOrTag) {
        return matchBlock(idOrTag, new HashMap<>());
    }

    public EntityPredicateBuilderJS matchFluid(String idOrTag) {
        TagOrEntry<Fluid> tagOrEntry = Utils.getTagOrEntry(ForgeRegistries.FLUIDS, idOrTag);
        fluidPredicate = new FluidPredicate(tagOrEntry.tag, tagOrEntry.entry, StatePropertiesPredicate.ANY);
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

    public EntityPredicateBuilderJS matchSlot(EquipmentSlot slot, IngredientJS ingredient) {
        if (equipmentPredicateBuilder == null) {
            equipmentPredicateBuilder = new EntityEquipmentPredicate.Builder();
        }

        CustomItemPredicate predicate = new CustomItemPredicate(ingredient.getVanillaPredicate());
        switch (slot) {
            case MAINHAND:
                equipmentPredicateBuilder.mainhand(predicate);
                break;
            case OFFHAND:
                equipmentPredicateBuilder.offhand(predicate);
                break;
            case FEET:
                equipmentPredicateBuilder.feet(predicate);
                break;
            case LEGS:
                equipmentPredicateBuilder.legs(predicate);
                break;
            case CHEST:
                equipmentPredicateBuilder.chest(predicate);
                break;
            case HEAD:
                equipmentPredicateBuilder.head(predicate);
                break;
        }
        return this;
    }

    public EntityPredicateBuilderJS anyType(String... unknowns) {
        List<EntityType<?>> types = new ArrayList<>();
        List<Tag<EntityType<?>>> tags = new ArrayList<>();

        for (String unknown : unknowns) {
            TagOrEntry<EntityType<?>> tagOrEntry = Utils.getTagOrEntry(ForgeRegistries.ENTITIES, unknown);
            if (tagOrEntry.isTag()) {
                tags.add(tagOrEntry.tag);
            } else {
                types.add(tagOrEntry.entry);
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
