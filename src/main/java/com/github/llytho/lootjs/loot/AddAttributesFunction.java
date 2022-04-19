package com.github.llytho.lootjs.loot;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AddAttributesFunction implements LootItemFunction {
    private static final Function<ItemStack, EquipmentSlot[]> SLOTS_BY_ITEM = itemStack -> new EquipmentSlot[]{
            LivingEntity.getEquipmentSlotForItem(itemStack)
    };

    private final boolean preserveDefaultModifier;
    private final List<Modifier> modifiers;

    public AddAttributesFunction(boolean preserveDefaultModifier, List<Modifier> modifiers) {
        this.preserveDefaultModifier = preserveDefaultModifier;
        this.modifiers = modifiers;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext context) {
        for (Modifier modifier : modifiers) {
            if (context.getRandom().nextFloat() < modifier.probability) {
                var am = modifier.createAttributeModifier(context);
                for (EquipmentSlot slot : modifier.slots.apply(itemStack)) {
                    if (preserveDefaultModifier) {
                        preserveDefaultAttributes(itemStack, slot);
                    }
                    itemStack.addAttributeModifier(modifier.attribute, am, slot);
                }
            }
        }

        return itemStack;
    }

    public void preserveDefaultAttributes(ItemStack itemStack, EquipmentSlot slot) {
        //noinspection ConstantConditions
        if (itemStack.hasTag() && itemStack.getTag().contains("AttributeModifiers", 9)) {
            return;
        }

        Multimap<Attribute, AttributeModifier> defaultAttributeModifiers = itemStack
                .getItem()
                .getDefaultAttributeModifiers(slot);
        for (var entry : defaultAttributeModifiers.entries()) {
            itemStack.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
        }
    }


    @Override
    public LootItemFunctionType getType() {
        throw new UnsupportedOperationException("Do not call");
    }

    public static class Modifier {
        protected final Attribute attribute;
        protected final float probability;
        protected final AttributeModifier.Operation operation;
        protected final NumberProvider amount;
        protected final String name;
        protected final Function<ItemStack, EquipmentSlot[]> slots;
        @Nullable
        protected UUID uuid;

        public Modifier(float probability, Attribute attribute, AttributeModifier.Operation operation, NumberProvider amount, String name, Function<ItemStack, EquipmentSlot[]> slots, @Nullable UUID uuid) {
            this.attribute = attribute;
            this.probability = probability;
            this.operation = operation;
            this.amount = amount;
            this.name = name;
            this.slots = slots;
            this.uuid = uuid;
        }

        public AttributeModifier createAttributeModifier(LootContext context) {
            return new AttributeModifier(UUID.randomUUID(), name, amount.getFloat(context), operation);
        }

        public static class Builder {
            protected final Attribute attribute;
            protected final NumberProvider amount;
            protected float probability;
            protected AttributeModifier.Operation operation;
            protected Function<ItemStack, EquipmentSlot[]> slots;
            @Nullable
            protected UUID uuid;
            @Nullable
            protected String name;

            public Builder(Attribute attribute, NumberProvider amount) {
                this.attribute = attribute;
                this.amount = amount;
                this.probability = 1f;
                this.operation = AttributeModifier.Operation.ADDITION;
                this.slots = SLOTS_BY_ITEM;
            }

            public void setProbability(float probability) {
                this.probability = probability;
            }

            public void setOperation(AttributeModifier.Operation operation) {
                this.operation = operation;
            }

            public void setSlots(EquipmentSlot[] slots) {
                this.slots = itemStack -> slots;
            }

            public void setName(@Nullable String name) {
                this.name = name;
            }

            public void setUuid(@Nullable UUID uuid) {
                this.uuid = uuid;
            }

            public Modifier build() {
                if (name == null) {
                    name = "lootjs." + attribute.getDescriptionId() + "." + operation.name().toLowerCase();
                }

                return new Modifier(probability, attribute, operation, amount, name, slots, uuid);
            }
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder implements LootItemFunction.Builder {
        private final List<Modifier> modifiers = new ArrayList<>();
        private boolean preserveDefaults = true;

        public Builder preserveDefaults(boolean flag) {
            preserveDefaults = flag;
            return this;
        }

        public Builder simple(Attribute attribute, NumberProvider amount) {
            return simple(1f, attribute, amount);
        }

        public Builder simple(float probability, Attribute attribute, NumberProvider amount) {
            return add(attribute, amount, m -> m.setProbability(probability));
        }

        public Builder forSlots(Attribute attribute, NumberProvider amount, EquipmentSlot[] slots) {
            return add(attribute, amount, m -> m.setSlots(slots));
        }

        public Builder forSlots(float probability, Attribute attribute, NumberProvider amount, EquipmentSlot[] slots) {
            return add(attribute, amount, m -> {
                m.setProbability(probability);
                m.setSlots(slots);
            });
        }

        public Builder add(Attribute attribute, NumberProvider amount, Consumer<Modifier.Builder> action) {
            Modifier.Builder builder = new Modifier.Builder(attribute, amount);
            action.accept(builder);
            return add(builder.build());
        }

        public Builder add(Modifier modifier) {
            Objects.requireNonNull(modifier);
            modifiers.add(modifier);
            return this;
        }

        public AddAttributesFunction build() {
            return new AddAttributesFunction(preserveDefaults, modifiers);
        }
    }
}
