package com.almostreliable.lootjs.loot;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.*;
import java.util.function.Consumer;

// TODO test pls help
public class AddAttributesFunction implements LootItemFunction {
    private final boolean preserveDefaultModifier;
    private final List<Modifier> modifiers;

    public AddAttributesFunction(boolean preserveDefaultModifier, List<Modifier> modifiers) {
        this.preserveDefaultModifier = preserveDefaultModifier;
        this.modifiers = modifiers;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext context) {
        var stackModifiers = ItemAttributeModifiers.builder();
        ItemAttributeModifiers existing = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (existing != null && preserveDefaultModifier) {
            for (ItemAttributeModifiers.Entry entry : existing.modifiers()) {
                stackModifiers.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }

        var slot = itemStack.getEquipmentSlot();
        if (slot == null) {
            slot = EquipmentSlot.MAINHAND; // TODO: Is this correct? Need to check
        }

        for (Modifier modifier : modifiers) {
            if (context.getRandom().nextFloat() < modifier.probability) {
                var am = modifier.createAttributeModifier(context);
                for (EquipmentSlotGroup slotGroup : modifier.slots) {
                    if (slotGroup.test(slot)) {
                        stackModifiers.add(modifier.attribute, am, slotGroup);
                    }
                }
            }
        }

        return itemStack;
    }

    @Override
    public LootItemFunctionType getType() {
        throw new UnsupportedOperationException("Do not call");
    }

    public static class Modifier {
        protected final Holder<Attribute> attribute;
        protected final float probability;
        protected final AttributeModifier.Operation operation;
        protected final NumberProvider amount;
        protected final ResourceLocation name;
        protected final Set<EquipmentSlotGroup> slots;

        public Modifier(float probability, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount, ResourceLocation name, Set<EquipmentSlotGroup> slots) {
            this.attribute = attribute;
            this.probability = probability;
            this.operation = operation;
            this.amount = amount;
            this.name = name;
            this.slots = slots;
        }

        public AttributeModifier createAttributeModifier(LootContext context) {
            return new AttributeModifier(name, amount.getFloat(context), operation);
        }

        public static class Builder {
            protected final Attribute attribute;
            protected final ResourceLocation name;
            protected final NumberProvider amount;
            protected float probability;
            protected AttributeModifier.Operation operation;
            protected Set<EquipmentSlotGroup> slots;


            public Builder(Attribute attribute, ResourceLocation name, NumberProvider amount) {
                this.attribute = attribute;
                this.name = name;
                this.amount = amount;
                this.probability = 1f;
                this.operation = AttributeModifier.Operation.ADD_VALUE;
                this.slots = new HashSet<>();
            }

            public void setProbability(float probability) {
                this.probability = probability;
            }

            public void setOperation(AttributeModifier.Operation operation) {
                this.operation = operation;
            }

            public void setSlots(EquipmentSlotGroup[] slots) {
                this.slots = new HashSet<>(Arrays.asList(slots));
            }

            public Modifier build() {
                Holder<Attribute> attributeHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
                return new Modifier(probability, attributeHolder, operation, amount, name, slots);
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

        public Builder simple(Attribute attribute, ResourceLocation name, NumberProvider amount) {
            return simple(1f, attribute, name, amount);
        }

        public Builder simple(float probability, Attribute attribute, ResourceLocation name, NumberProvider amount) {
            return add(attribute, name, amount, m -> m.setProbability(probability));
        }

        public Builder forSlots(Attribute attribute, ResourceLocation name, NumberProvider amount, EquipmentSlotGroup[] slots) {
            return add(attribute, name, amount, m -> m.setSlots(slots));
        }

        public Builder forSlots(float probability, Attribute attribute, ResourceLocation name, NumberProvider amount, EquipmentSlotGroup[] slots) {
            return add(attribute, name, amount, m -> {
                m.setProbability(probability);
                m.setSlots(slots);
            });
        }

        public Builder add(Attribute attribute, ResourceLocation name, NumberProvider amount, Consumer<Modifier.Builder> action) {
            Modifier.Builder builder = new Modifier.Builder(attribute, name, amount);
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
