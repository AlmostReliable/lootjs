package com.almostreliable.lootjs.kube.wrappers;

import com.almostreliable.lootjs.core.filters.IdFilter;
import com.almostreliable.lootjs.util.BlockFilter;
import com.almostreliable.lootjs.util.Utils;
import com.mojang.serialization.MapCodec;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.util.NBTUtils;
import dev.latvian.mods.kubejs.util.RegExpKJS;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.RecordTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public class BasicWrapper {

    public static final EntityTypePredicate EMPTY_ENTITY_TYPE_PREDICATE = new EntityTypePredicate(HolderSet.direct());
    private static final TypeInfo ENTITY_HOLDER_SET = TypeInfo
            .of(HolderSet.class)
            .withParams(TypeInfo.of(EntityType.class));

    public static BlockFilter ofBlockFilter(Object o) {
        if (o instanceof BlockFilter bf) {
            return bf;
        }

        BlockStatePredicate bsp = BlockStatePredicate.of(o);
        return new BlockFilter() {
            @NotNull
            @Override
            public Iterator<Block> iterator() {
                return bsp.getBlocks().iterator();
            }

            @Override
            public boolean test(BlockState blockState) {
                return bsp.test(blockState);
            }
        };
    }

    public static NbtPredicate ofNbtPredicate(Context cx, Object o, TypeInfo target) {
        if (o instanceof NbtPredicate nbt) {
            return nbt;
        }

        if (o instanceof Map<?, ?> map) {
            return new NbtPredicate((CompoundTag) NBTUtils.compoundTag(cx, map));
        }

        return new NbtPredicate(new CompoundTag());
    }

    public static PlayerPredicate.AdvancementPredicate ofAdvancementPredicate(RegistryAccessContainer registry, Object o) {
        return PlayerPredicate.AdvancementPredicate.CODEC.parse(registry.java(), o).getOrThrow();
    }

    public static LightPredicate ofLightPredicate(RegistryAccessContainer registry, Object o) {
        MinMaxBounds.Ints range = MinMaxBoundsWrapper.ofMinMaxInt(registry, o);
        return new LightPredicate(range);
    }

    public static EntitySubPredicate ofEntitySubPredicate(Context cx, Object o, TypeInfo target) {
        if (o instanceof Map<?, ?>) {
            RegistryAccessContainer registries = ((KubeJSContext) cx).getRegistries();
            return registries.decode(cx, EntitySubPredicate.CODEC, o);
        }

        return new EntitySubPredicate() {
            @Override
            public MapCodec<? extends EntitySubPredicate> codec() {
                throw new UnsupportedOperationException("Custom EntitySubPredicate does not have a codec");
            }

            @Override
            public boolean matches(Entity arg, ServerLevel arg2, @Nullable Vec3 arg3) {
                return false;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static EntityTypePredicate ofEntityTypePredicate(Context cx, @Nullable Object o) {
        if (o instanceof EntityType<?> type) {
            return EntityTypePredicate.of(type);
        }

        HolderSet<EntityType<?>> holderSet = (HolderSet<EntityType<?>>) cx.jsToJava(o, ENTITY_HOLDER_SET);
        return new EntityTypePredicate(holderSet);
    }

    public static DamageSourcePredicate ofDamageSourcePredicate(Context cx, Object o, TypeInfo target) {
        if (o instanceof String str && str.startsWith("#")) {
            var tag = str.substring(0, 1);
            var predicate = TagPredicate.is(TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(tag)));
            return DamageSourcePredicate.Builder.damageType().tag(predicate).build();
        }

        var map = new HashMap<>(Utils.mapOrThrow(o));
        if (map.get("tags") instanceof List<?> list) {
            List<TagPredicate<DamageType>> predicates = new ArrayList<>();
            for (Object obj : list) {
                getTagPredicate(obj).ifPresent(predicates::add);
            }

            map.put("tags", predicates);
        }

        return (DamageSourcePredicate) ((RecordTypeInfo) target).createInstance(cx, map);
    }

    private static Optional<TagPredicate<DamageType>> getTagPredicate(Object rawPredicate) {
        try {
            var map = Utils.mapOrThrow(rawPredicate);
            if (!map.containsKey("id") || !map.containsKey("expected")) {
                throw new IllegalArgumentException(
                        "Missing id and expected in damage source predicate: " + rawPredicate);
            }

            var id = map.get("id").toString();
            boolean expected = (boolean) map.get("expected");

            var tag = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(id));
            return Optional.of(new TagPredicate<>(tag, expected));
        } catch (Exception e) {
            ConsoleJS.SERVER.error("Error parsing damage source predicate: " + rawPredicate, e);
        }

        return Optional.empty();
    }

    public static IdFilter ofIdFilter(Object o) {
        Pattern pattern = RegExpKJS.wrap(o);
        if (pattern != null) {
            return new IdFilter.ByPattern(pattern);
        }

        return switch (o) {
            case List<?> list -> new IdFilter.Or(list.stream().map(BasicWrapper::ofIdFilter).toList());
            case String str -> {
                if (str.startsWith("@")) {
                    yield new IdFilter.ByMod(str.substring(1));
                }

                yield new IdFilter.ByLocation(ResourceLocation.parse(str));
            }
            case ResourceLocation rl -> new IdFilter.ByLocation(rl);
            default -> throw new IllegalArgumentException("Invalid resource location filter: " + o);
        };
    }
}
