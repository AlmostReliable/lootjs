package com.github.llytho.lootjs.predicate;

import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public class ExtendedDamageSourcePredicate extends DamageSourcePredicate {

    private final String[] sourceNames;

    public ExtendedDamageSourcePredicate(@Nullable Boolean isProjectile, @Nullable Boolean isExplosion, @Nullable Boolean bypassesArmor, @Nullable Boolean bypassesInvulnerability, @Nullable Boolean bypassesMagic, @Nullable Boolean isFire, @Nullable Boolean isMagic, @Nullable Boolean isLightning, EntityPredicate directEntity, EntityPredicate sourceEntity, String[] sourceNames) {
        super(isProjectile,
                isExplosion,
                bypassesArmor,
                bypassesInvulnerability,
                bypassesMagic,
                isFire,
                isMagic,
                isLightning,
                directEntity,
                sourceEntity);
        this.sourceNames = sourceNames;
    }

    @Override
    public boolean matches(ServerWorld level, Vector3d origin, DamageSource damageSource) {
        return super.matches(level, origin, damageSource);
    }

    private boolean containsId(DamageSource damageSource) {
        if (sourceNames.length == 0) return true;

        for (String id : sourceNames) {
            if (id.equalsIgnoreCase(damageSource.getMsgId())) {
                return true;
            }
        }
        return false;
    }

    public static class Builder extends DamageSourcePredicate.Builder {
        private final ArrayList<String> sourceNames = new ArrayList<>();

        public Builder anyType(String... types) {
            sourceNames.addAll(Arrays.asList(types));
            return this;
        }

        @Override
        public ExtendedDamageSourcePredicate build() {
            return new ExtendedDamageSourcePredicate(this.isProjectile,
                    this.isExplosion,
                    this.bypassesArmor,
                    this.bypassesInvulnerability,
                    this.bypassesMagic,
                    this.isFire,
                    this.isMagic,
                    this.isLightning,
                    this.directEntity,
                    this.sourceEntity,
                    sourceNames.toArray(new String[0]));

        }
    }
}
