package com.github.llytho.lootjs.kube.builder;

import com.github.llytho.lootjs.predicate.CustomItemPredicate;
import com.github.llytho.lootjs.predicate.MultiEntityTypePredicate;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EntityPredicateBuilderJS {
    private final EntityPredicate.Builder vanillaBuilder = EntityPredicate.Builder.entity();

    @Nullable
    private Boolean isOnFire;
    @Nullable
    private Boolean isCrouching;
    @Nullable
    private Boolean isSprinting;
    @Nullable
    private Boolean isSwimming;
    @Nullable
    private Boolean isBaby;

    @Nullable
    private EntityEquipmentPredicate.Builder equipmentPredicateBuilder;

    private Map<Effect, MobEffectsPredicate.InstancePredicate> effects;

    @Nullable
    private Float minDistanceToPlayer;
    @Nullable
    private Float maxDistanceToPlayer;

    public EntityPredicateBuilderJS catType(ResourceLocation type) {
        vanillaBuilder.catType(type);
        return this;
    }

    public EntityPredicateBuilderJS isOnFire(boolean flag) {
        isOnFire = flag;
        return this;
    }

    public EntityPredicateBuilderJS isCrouching(boolean flag) {
        isCrouching = flag;
        return this;
    }

    public EntityPredicateBuilderJS isSprinting(boolean flag) {
        isSprinting = flag;
        return this;
    }

    public EntityPredicateBuilderJS isSwimming(boolean flag) {
        isSwimming = flag;
        return this;
    }

    public EntityPredicateBuilderJS isBaby(boolean flag) {
        isBaby = flag;
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(Effect effect, int amplifier) {
        MinMaxBounds.IntBound bounds = MinMaxBounds.IntBound.atLeast(amplifier);
        MobEffectsPredicate.InstancePredicate predicate = new MobEffectsPredicate.InstancePredicate(bounds,
                MinMaxBounds.IntBound.ANY,
                null,
                null);
        effects.put(effect, predicate);
        return this;
    }

    public EntityPredicateBuilderJS hasEffect(Effect effect) {
        return hasEffect(effect, 0);
    }

    public EntityPredicateBuilderJS nbt(CompoundNBT nbt) {
        vanillaBuilder.nbt(new NBTPredicate(nbt));
        return this;
    }

    public EntityPredicateBuilderJS matchVehicle(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS vehiclePredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(vehiclePredicateBuilder);
        vanillaBuilder.vehicle(vehiclePredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchTargetedEntity(Consumer<EntityPredicateBuilderJS> action) {
        EntityPredicateBuilderJS vehiclePredicateBuilder = new EntityPredicateBuilderJS();
        action.accept(vehiclePredicateBuilder);
        vanillaBuilder.targetedEntity(vehiclePredicateBuilder.build());
        return this;
    }

    public EntityPredicateBuilderJS matchSlot(EquipmentSlotType slot, IngredientJS ingredient) {
        if (equipmentPredicateBuilder == null) {
            equipmentPredicateBuilder = new EntityEquipmentPredicate.Builder();
        }

        CustomItemPredicate predicate = new CustomItemPredicate(ingredient.getVanillaPredicate());
        switch (slot) {
            case MAINHAND:
                equipmentPredicateBuilder.mainhand = predicate;
                break;
            case OFFHAND:
                equipmentPredicateBuilder.offhand = predicate;
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
        List<ITag<EntityType<?>>> tags = new ArrayList<>();

        for (String unknown : unknowns) {
            if (unknown.startsWith("#")) {
                ITag<EntityType<?>> tag = TagCollectionManager
                        .getInstance()
                        .getEntityTypes()
                        .getTag(new ResourceLocation(unknown.substring(1)));
                if (tag == null) {
                    throw new IllegalArgumentException("Tag " + unknown + " does not exists for entities");
                }
                tags.add(tag);
            } else {
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(unknown));
                if (type == null) {
                    throw new IllegalArgumentException("Entity type " + unknown + " does not exists");
                }
                types.add(type);
            }
        }

        vanillaBuilder.entityType(new MultiEntityTypePredicate(tags, types));
        return this;
    }

    public EntityPredicateBuilderJS minDistanceToPlayer(float distance) {
        minDistanceToPlayer = distance;
        return this;
    }

    public EntityPredicateBuilderJS maxDistanceToPlayer(float distance) {
        maxDistanceToPlayer = distance;
        return this;
    }

    @HideFromJS
    public EntityPredicate build() {
        tryBuildFLags();
        tryBuildEffects();
        tryBuildDistance();
        tryBuildEquipment();
        return vanillaBuilder.build();
    }

    private void tryBuildEquipment() {
        if (equipmentPredicateBuilder != null) {
            vanillaBuilder.equipment(equipmentPredicateBuilder.build());
        }
    }

    private void tryBuildDistance() {
        if (minDistanceToPlayer != null | maxDistanceToPlayer != null) {
            MinMaxBounds.FloatBound distance = new MinMaxBounds.FloatBound(minDistanceToPlayer, maxDistanceToPlayer);
            vanillaBuilder.distance(new DistancePredicate(MinMaxBounds.FloatBound.ANY,
                    MinMaxBounds.FloatBound.ANY,
                    MinMaxBounds.FloatBound.ANY,
                    MinMaxBounds.FloatBound.ANY,
                    distance));
        }
    }

    private void tryBuildEffects() {
        if (!effects.isEmpty()) {
            vanillaBuilder.effects(new MobEffectsPredicate(effects));
        }
    }

    private void tryBuildFLags() {
        if (isOnFire != null || isCrouching != null || isBaby != null || isSprinting != null || isSwimming != null) {
            vanillaBuilder.flags(new EntityFlagsPredicate(isOnFire, isCrouching, isSprinting, isSwimming, isBaby));
        }
    }
}
