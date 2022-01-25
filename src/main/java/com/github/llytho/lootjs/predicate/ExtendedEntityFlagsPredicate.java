package com.github.llytho.lootjs.predicate;

import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

public class ExtendedEntityFlagsPredicate extends EntityFlagsPredicate {

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
        super(isOnFire, isCrouching, isSprinting, isSwimming, isBaby);
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

    @Override
    public boolean matches(Entity entity) {
        return super.matches(entity) && matchesInWater(entity) && matchesUnderWater(entity) && matchesMonster(entity) &&
               matchesCreature(entity) && matchesOnGround(entity) && matchesMobTypes(entity);
    }

    private boolean matchesMobTypes(Entity entity) {
        return matchesMobType(isUndeadMob, entity, CreatureAttribute.UNDEAD) &&
               matchesMobType(isArthropodMob, entity, CreatureAttribute.ARTHROPOD) &&
               matchesMobType(isIllegarMob, entity, CreatureAttribute.ILLAGER) &&
               matchesMobType(isWaterMob, entity, CreatureAttribute.WATER);
    }

    private boolean matchesMobType(@Nullable Boolean flag, Entity entity, CreatureAttribute mobType) {
        if (flag == null) {
            return true;
        }

        if (entity instanceof LivingEntity) {
            return flag == (((LivingEntity) entity).getMobType() == mobType);
        }

        return false;
    }

    private boolean matchesOnGround(Entity entity) {
        return isOnGround == null || isOnGround == entity.isOnGround();
    }

    private boolean matchesCreature(Entity entity) {
        return isCreature == null || (entity.getType().getCategory() == EntityClassification.CREATURE) == isCreature;
    }

    private boolean matchesMonster(Entity entity) {
        return isMonster == null || (entity.getType().getCategory() == EntityClassification.MONSTER) == isMonster;
    }

    private boolean matchesUnderWater(Entity entity) {
        return isUnderWater == null || isUnderWater == entity.isUnderWater();
    }

    private boolean matchesInWater(Entity entity) {
        return isInWater == null || isInWater == entity.isInWater();
    }

    @Override
    public JsonElement serializeToJson() {
        JsonElement jsonElement = super.serializeToJson();
        JsonObject jsonobject = jsonElement.getAsJsonObject();
        this.addOptionalBoolean(jsonobject, "isInWater", this.isInWater);
        this.addOptionalBoolean(jsonobject, "isUnderWater", this.isUnderWater);
        this.addOptionalBoolean(jsonobject, "isMonster", this.isMonster);
        this.addOptionalBoolean(jsonobject, "isCreature", this.isCreature);
        this.addOptionalBoolean(jsonobject, "isOnGround", this.isOnGround);
        this.addOptionalBoolean(jsonobject, "isUndeadMob", this.isUndeadMob);
        this.addOptionalBoolean(jsonobject, "isArthropodMob", this.isArthropodMob);
        this.addOptionalBoolean(jsonobject, "isIllegarMob", this.isIllegarMob);
        this.addOptionalBoolean(jsonobject, "isWaterMob", this.isWaterMob);
        return jsonobject;
    }

    private void addOptionalBoolean(JsonObject pJson, String pName, @Nullable Boolean pValue) {
        if (pValue != null) {
            pJson.addProperty(pName, pValue);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public interface IBuilder<T> {
        T build();

        IBuilder<T> isOnFire(boolean flag);

        IBuilder<T> isCrouching(boolean flag);

        IBuilder<T> isSprinting(boolean flag);

        IBuilder<T> isSwimming(boolean flag);

        IBuilder<T> isBaby(boolean flag);

        IBuilder<T> isInWater(boolean flag);

        IBuilder<T> isUnderWater(boolean flag);

        IBuilder<T> isMonster(boolean flag);

        IBuilder<T> isCreature(boolean flag);

        IBuilder<T> isOnGround(boolean flag);

        IBuilder<T> isUndeadMob(boolean flag);

        IBuilder<T> isArthropodMob(boolean flag);

        IBuilder<T> isIllegarMob(boolean flag);

        IBuilder<T> isWaterMob(boolean flag);
    }

    public static class Builder extends EntityFlagsPredicate.Builder implements IBuilder<ExtendedEntityFlagsPredicate> {
        @Nullable
        protected Boolean isInWater;
        @Nullable
        protected Boolean isUnderWater;
        @Nullable
        protected Boolean isMonster;
        @Nullable
        protected Boolean isCreature;
        @Nullable
        protected Boolean isOnGround;
        @Nullable
        protected Boolean isUndeadMob;
        @Nullable
        protected Boolean isArthropodMob;
        @Nullable
        protected Boolean isIllegarMob;
        @Nullable
        protected Boolean isWaterMob;

        @Override
        public ExtendedEntityFlagsPredicate build() {
            return new ExtendedEntityFlagsPredicate(isOnFire,
                    isCrouching,
                    isSprinting,
                    isSwimming,
                    isBaby,
                    isInWater,
                    isUnderWater,
                    isMonster,
                    isCreature,
                    isOnGround,
                    isUndeadMob,
                    isArthropodMob,
                    isIllegarMob,
                    isWaterMob);
        }

        @Override
        public Builder isOnFire(boolean flag) {
            isOnFire = flag;
            return this;
        }

        @Override
        public Builder isCrouching(boolean flag) {
            isCrouching = flag;
            return this;
        }

        @Override
        public Builder isSprinting(boolean flag) {
            isSprinting = flag;
            return this;
        }

        @Override
        public Builder isSwimming(boolean flag) {
            isSwimming = flag;
            return this;
        }

        @Override
        public Builder isBaby(boolean flag) {
            isBaby = flag;
            return this;
        }

        @Override
        public Builder isInWater(boolean flag) {
            isInWater = flag;
            return this;
        }

        @Override
        public Builder isUnderWater(boolean flag) {
            isUnderWater = flag;
            return this;
        }

        @Override
        public Builder isMonster(boolean flag) {
            isMonster = flag;
            return this;
        }

        @Override
        public Builder isCreature(boolean flag) {
            isCreature = flag;
            return this;
        }

        @Override
        public Builder isOnGround(boolean flag) {
            isOnGround = flag;
            return this;
        }

        @Override
        public Builder isUndeadMob(boolean flag) {
            isUndeadMob = flag;
            return this;
        }

        @Override
        public Builder isArthropodMob(boolean flag) {
            isArthropodMob = flag;
            return this;
        }

        @Override
        public Builder isIllegarMob(boolean flag) {
            isIllegarMob = flag;
            return this;
        }

        @Override
        public Builder isWaterMob(boolean flag) {
            isWaterMob = flag;
            return this;
        }
    }
}
