package com.github.llytho.lootjs.core;

import com.github.llytho.lootjs.util.LootContextUtils;
import com.github.llytho.lootjs.util.Utils;
import com.google.common.base.Strings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebugStack {
    public static final String CONDITION_PREFIX = "";
    public static final int BASE_LAYER = 1;
    protected final List<Entry> entries = new ArrayList<>();
    private int layer = BASE_LAYER;

    public static void popLayer(@Nullable DebugStack stack) {
        if (stack != null) {
            stack.popLayer();
        }
    }

    public static void pushLayer(@Nullable DebugStack stack) {
        if (stack != null) {
            stack.pushLayer();
        }
    }

    public static void write(@Nullable DebugStack stack, String text) {
        if (stack != null) {
            stack.write(text);
        }
    }

    public static <T> void write(@Nullable DebugStack stack, String prefix, T t) {
        if (stack != null) {
            stack.write(prefix + Utils.getClassNameEnding(t));
        }
    }

    @Nullable
    public static <T> TestedEntry write(@Nullable DebugStack stack, @Nullable String prefix, T t, @Nullable String suffix, boolean succeed) {
        if (stack == null) {
            return null;
        }

        String prefixStr = prefix == null ? "" : prefix;
        String suffixStr = suffix == null ? "" : suffix;
        return stack.write(succeed, prefixStr + Utils.getClassNameEnding(t) + suffixStr);
    }

    public static DebugStack context(LootContext context) {
        DebugStack debugStack = new DebugStack();

        debugStack.write("LootTable", Utils.quote(context.getQueriedLootTableId()));

        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            debugStack.write("Loot type", data.getLootContextType());
            debugStack.write("Current loot :");
            debugStack.pushLayer();
            for (ItemStack itemStack : data.getGeneratedLoot()) {
                String s = Utils.formatItemStack(itemStack);
                if (s != null) {
                    debugStack.write("- " + s);
                }
            }
            debugStack.popLayer();
        }

        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        debugStack.write("Position", Utils.formatPosition(origin));
        debugStack.write("Block", context.getParamOrNull(LootContextParams.BLOCK_STATE));
        debugStack.write("Explosion", context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS));
        debugStack.write("Entity", Utils.formatEntity(context.getParamOrNull(LootContextParams.THIS_ENTITY)));
        debugStack.write("Killer Entity", Utils.formatEntity(context.getParamOrNull(LootContextParams.KILLER_ENTITY)));
        debugStack.write("Direct Killer",
                Utils.formatEntity(context.getParamOrNull(LootContextParams.DIRECT_KILLER_ENTITY)));

        ServerPlayer playerOrNull = LootContextUtils.getPlayerOrNull(context);
        if (playerOrNull != null) {
            debugStack.write("Player", Utils.formatEntity(playerOrNull));
            debugStack.write("Player Pos", Utils.formatPosition(playerOrNull.position()));
            if (origin != null) {
                debugStack.write("Distance", String.format("%.2f", playerOrNull.position().distanceTo(origin)));
            }
            debugStack.write("MainHand", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.MAINHAND)));
            debugStack.write("OffHand", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.OFFHAND)));
            debugStack.write("Head", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.HEAD)));
            debugStack.write("Chest", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.CHEST)));
            debugStack.write("Legs", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.LEGS)));
            debugStack.write("Feet", Utils.formatItemStack(playerOrNull.getItemBySlot(EquipmentSlot.FEET)));
        }

        return debugStack;
    }

    public void pushLayer() {
        layer++;
    }

    public void popLayer() {
        if (layer > BASE_LAYER) {
            layer--;
        }
    }

    public void h1(String string) {
        write("- \uD83E\uDDF0 " + string);
    }

    public void h2(String string) {
        write("- \uD83D\uDD27 " + string);
    }

    public void h3(String string) {
        write("- \uD83D\uDD29 " + string);
    }

    public void write(String string) {
        entries.add(new Entry(layer, string));
    }

    public TestedEntry write(boolean succeed, String string) {
        TestedEntry testedEntry = new TestedEntry(layer, succeed, string);
        entries.add(testedEntry);
        return testedEntry;
    }

    public <T> void write(String prefix, @Nullable T t) {
        if (t != null) {
            write(prefix + Strings.repeat(" ", 13 - prefix.length()) + ": " + t);
        }
    }

    public void reset() {
        entries.clear();
        layer = BASE_LAYER;
    }

    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public static class Entry {
        protected final int layer;
        protected final String text;

        protected Entry(int layer, String text) {
            this.layer = layer;
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public int getLayer() {
            return layer;
        }
    }

    public static class TestedEntry extends Entry {
        protected boolean succeed;

        protected TestedEntry(int layer, boolean succeed, String text) {
            super(layer, text);
            this.succeed = succeed;
        }

        @Override
        public String getText() {
            return (succeed ? "\u2714\uFE0F" : "\u274C") + " " + super.getText();
        }

        public void setSucceed(boolean flag) {
            succeed = flag;
        }
    }
}
