package com.almostreliable.lootjs.predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;

import javax.annotation.Nullable;

public class ExtendedEntityFlagsPredicate {
    // TODO implement into vanilla via mixin or remove it. Idk yet
    @Nullable
    protected final Boolean isInWater;
    @Nullable
    protected final Boolean isUnderWater;
    @Nullable
    protected final Boolean isMonster;
    @Nullable
    protected final Boolean isCreature;
    @Nullable
    protected final Boolean isOnGround;
    @Nullable
    protected final Boolean isUndeadMob;
    @Nullable
    protected final Boolean isArthropodMob;
    @Nullable
    protected final Boolean isIllegarMob;
    @Nullable
    protected final Boolean isWaterMob;

    public ExtendedEntityFlagsPredicate(@Nullable Boolean isOnFire, @Nullable Boolean isCrouching, @Nullable Boolean isSprinting, @Nullable Boolean isSwimming, @Nullable Boolean isBaby, @Nullable Boolean isInWater, @Nullable Boolean isUnderWater, @Nullable Boolean isMonster, @Nullable Boolean isCreature, @Nullable Boolean isOnGround, @Nullable Boolean isUndeadMob, @Nullable Boolean isArthropodMob, @Nullable Boolean isIllegarMob, @Nullable Boolean isWaterMob) {
        this.isInWater = isInWater;
        this.isUnderWater = isUnderWater;
        this.isMonster = isMonster;
        this.isCreature = isCreature;
        this.isOnGround = isOnGround;
        this.isUndeadMob = isUndeadMob;
        this.isArthropodMob = isArthropodMob;
        this.isIllegarMob = isIllegarMob;
        this.isWaterMob = isWaterMob;
    }

    public boolean matches(Entity entity) {
        return matchesInWater(entity) && matchesUnderWater(entity) && matchesMonster(entity) &&
               matchesCreature(entity) && matchesOnGround(entity) && matchesMobTypes(entity);
    }

    private boolean matchesMobTypes(Entity entity) {
        return matchesMobType(isUndeadMob, entity, MobType.UNDEAD) &&
               matchesMobType(isArthropodMob, entity, MobType.ARTHROPOD) &&
               matchesMobType(isIllegarMob, entity, MobType.ILLAGER) &&
               matchesMobType(isWaterMob, entity, MobType.WATER);
    }

    private boolean matchesMobType(@Nullable Boolean flag, Entity entity, MobType mobType) {
        if (flag == null) {
            return true;
        }

        if (entity instanceof LivingEntity) {
            return flag == (((LivingEntity) entity).getMobType() == mobType);
        }

        return false;
    }

    private boolean matchesOnGround(Entity entity) {
        return isOnGround == null || isOnGround == entity.onGround();
    }

    private boolean matchesCreature(Entity entity) {
        return isCreature == null || (entity.getType().getCategory() == MobCategory.CREATURE) == isCreature;
    }

    private boolean matchesMonster(Entity entity) {
        return isMonster == null || (entity.getType().getCategory() == MobCategory.MONSTER) == isMonster;
    }

    private boolean matchesUnderWater(Entity entity) {
        return isUnderWater == null || isUnderWater == entity.isUnderWater();
    }

    private boolean matchesInWater(Entity entity) {
        return isInWater == null || isInWater == entity.isInWater();
    }


    public JsonElement serializeToJson() {
//        JsonObject jsonobject = jsonElement.getAsJsonObject();
////        this.addOptionalBoolean(jsonobject, "isInWater", this.isInWater);
////        this.addOptionalBoolean(jsonobject, "isUnderWater", this.isUnderWater);
////        this.addOptionalBoolean(jsonobject, "isMonster", this.isMonster);
////        this.addOptionalBoolean(jsonobject, "isCreature", this.isCreature);
////        this.addOptionalBoolean(jsonobject, "isOnGround", this.isOnGround);
////        this.addOptionalBoolean(jsonobject, "isUndeadMob", this.isUndeadMob);
////        this.addOptionalBoolean(jsonobject, "isArthropodMob", this.isArthropodMob);
////        this.addOptionalBoolean(jsonobject, "isIllegarMob", this.isIllegarMob);
////        this.addOptionalBoolean(jsonobject, "isWaterMob", this.isWaterMob);
////        return jsonobject;
        return new JsonObject();
    }

    private void addOptionalBoolean(JsonObject pJson, String pName, @Nullable Boolean pValue) {
        if (pValue != null) {
            pJson.addProperty(pName, pValue);
        }
    }
}
