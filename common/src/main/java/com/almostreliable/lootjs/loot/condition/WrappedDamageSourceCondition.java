package com.almostreliable.lootjs.loot.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class WrappedDamageSourceCondition implements IExtendedLootCondition {
    private final DamageSourcePredicate predicate;
    @Nullable
    private final String[] sourceNames;

    public WrappedDamageSourceCondition(DamageSourcePredicate predicate, @Nullable String[] sourceNames) {
        this.predicate = predicate;
        this.sourceNames = sourceNames;
    }

    @Override
    public boolean test(LootContext lootContext) {
        DamageSource damagesource = lootContext.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        Vec3 vec3 = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        return vec3 != null && damagesource != null &&
               this.predicate.matches(lootContext.getLevel(), vec3, damagesource) && containsId(damagesource);

    }

    private boolean containsId(DamageSource damageSource) {
        if (sourceNames == null || sourceNames.length == 0) return true;

        for (String id : sourceNames) {
            if (id.equalsIgnoreCase(damageSource.getMsgId())) {
                return true;
            }
        }
        return false;
    }

    public JsonObject serializeToJson() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.add("DamageSourcePredicate", predicate.serializeToJson());

        if (sourceNames != null) {
            JsonArray arr = new JsonArray();
            jsonobject.add("sourceNames", arr);
            for (String sourceName : sourceNames) {
                arr.add(sourceName);
            }
        }

        return jsonobject;
    }
}
