package com.almostreliable.lootjs.core;

import com.almostreliable.lootjs.loot.extension.LootContextExtension;
import com.almostreliable.lootjs.util.LootContextUtils;
import com.almostreliable.lootjs.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LootContextInfo {
    private final List<String> info = new ArrayList<>();
    private final List<String> itemInfoBefore = new ArrayList<>();
    private final List<String> itemInfoAfter = new ArrayList<>();

    private LootContextInfo() {}

    public static LootContextInfo create(LootContext context, Iterable<ItemStack> loot) {
        LootContextInfo lci = new LootContextInfo();

        lci.add("LootTable", Utils.quote(context.getQueriedLootTableId()));
        lci.add("LootType", LootContextExtension.cast(context).lootjs$getType().name());

        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        lci.addOptional("Position", origin, Utils::formatPosition);
        lci.addOptional("Block", context.getParamOrNull(LootContextParams.BLOCK_STATE));
        lci.addOptional("Explosion", context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS));
        lci.addOptional("Entity", context.getParamOrNull(LootContextParams.THIS_ENTITY), Utils::formatEntity);
        lci.addOptional("Attacking Entity",
                context.getParamOrNull(LootContextParams.ATTACKING_ENTITY),
                Utils::formatEntity);
        lci.addOptional("Direct Attacker",
                context.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY),
                Utils::formatEntity);

        ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
        if (player != null) {
            lci.addOptional("Player", player, Utils::formatEntity);
            lci.addOptional("Player Pos", player.position(), Utils::formatPosition);
            if (origin != null) {
                lci.addOptional("Distance", String.format("%.2f", player.position().distanceTo(origin)));
            }
            lci.addItem("MainHand", player.getItemBySlot(EquipmentSlot.MAINHAND));
            lci.addItem("OffHand", player.getItemBySlot(EquipmentSlot.OFFHAND));
            lci.addItem("Head", player.getItemBySlot(EquipmentSlot.HEAD));
            lci.addItem("Chest", player.getItemBySlot(EquipmentSlot.CHEST));
            lci.addItem("Legs", player.getItemBySlot(EquipmentSlot.LEGS));
            lci.addItem("Feet", player.getItemBySlot(EquipmentSlot.FEET));
        }


        lci.updateLootBefore(loot);
        return lci;
    }

    private String f(String left, String right) {
        return String.format("%-15s: %s", left, right);
    }

    private void add(String left, String right) {
        var txt = f(left, right);
        info.add(txt);
    }

    private <T> void addOptional(String left, @Nullable T t, Function<T, String> formatter) {
        if (t == null) return;
        add(left, formatter.apply(t));
    }

    private <T> void addOptional(String left, @Nullable T t) {
        if (t == null) return;
        add(left, t.toString());
    }

    private void addItem(String left, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;
        add(left, Utils.formatItemStack(itemStack));
    }

    public void updateLootBefore(Iterable<ItemStack> loot) {
        for (ItemStack itemStack : loot) {
            itemInfoBefore.add(Utils.formatItemStack(itemStack));
        }
    }

    public void updateLootAfter(Iterable<ItemStack> loot) {
        for (ItemStack itemStack : loot) {
            itemInfoAfter.add(Utils.formatItemStack(itemStack));
        }
    }

    public void release(StringBuilder sb) {
        sb.append("[ Loot Information ]").append("\n");
        String indent = StringUtils.repeat(" ", 3);
        info.forEach(s -> sb.append(indent).append(s).append("\n"));

        sb.append(indent).append(f("Loot before", "{")).append("\n");
        itemInfoBefore.forEach(s -> sb.append(indent).append(indent).append("- ").append(s).append("\n"));
        sb.append(indent).append("}").append("\n");

        sb.append(indent).append(f("Loot after", "{")).append("\n");
        itemInfoAfter.forEach(s -> sb.append(indent).append(indent).append("- ").append(s).append("\n"));
        sb.append(indent).append("}").append("\n");
    }
}
