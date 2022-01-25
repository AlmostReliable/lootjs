package com.github.llytho.lootjs.loot.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;

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
        DamageSource damagesource = lootContext.getParamOrNull(LootParameters.DAMAGE_SOURCE);
        Vector3d vec3 = lootContext.getParamOrNull(LootParameters.ORIGIN);
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
