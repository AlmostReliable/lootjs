package com.github.llytho.lootjs.loot.results;

import com.github.llytho.lootjs.LootModificationsAPI;
import com.github.llytho.lootjs.core.Constants;
import com.github.llytho.lootjs.core.ILootContextData;
import com.github.llytho.lootjs.util.LootContextUtils;
import com.github.llytho.lootjs.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class LootContextInfo {
    LootInfoCollector collector = new LootInfoCollector();

    private LootContextInfo() {}

    @Nullable
    public static LootContextInfo create(LootContext context) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING) return null;

        LootContextInfo lci = new LootContextInfo();

        lci.add("LootTable", Utils.quote(context.getQueriedLootTableId()));

        ILootContextData data = context.getParamOrNull(Constants.DATA);
        if (data != null) {
            lci.add("LootType", data.getLootContextType().name());
            lci.updateLoot(data.getGeneratedLoot());
        }

        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        lci.addOptional("Position", origin, Utils::formatPosition);
        lci.addOptional("Block", context.getParamOrNull(LootContextParams.BLOCK_STATE));
        lci.addOptional("Explosion", context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS));
        lci.addOptional("Entity", context.getParamOrNull(LootContextParams.THIS_ENTITY), Utils::formatEntity);
        lci.addOptional("Killer Entity", context.getParamOrNull(LootContextParams.KILLER_ENTITY), Utils::formatEntity);
        lci.addOptional("Direct Killer",
                context.getParamOrNull(LootContextParams.DIRECT_KILLER_ENTITY),
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

        return lci;
    }

    private void add(String left, String right) {
        collector.addOrPush(new Info.RowInfo(left, right));
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

    public void updateLoot(Collection<ItemStack> loot) {
        LootComposite lootComposite = (LootComposite) collector
                .getFirstLayer()
                .stream()
                .filter(LootComposite.class::isInstance)
                .findFirst()
                .orElse(null);

        if (lootComposite == null) {
            LootComposite lc = new LootComposite();
            collector.add(lc);
            updateLoot(loot, lc.getBefore());
        } else {
            updateLoot(loot, lootComposite.getAfter());
        }
    }

    private void updateLoot(Collection<ItemStack> loot, Info.Composite composite) {
        for (ItemStack itemStack : loot) {
            composite.addChildren(new Info.TitledInfo(Utils.formatItemStack(itemStack)));
        }
    }

    public LootInfoCollector getCollector() {
        return collector;
    }

    public static class LootComposite extends Info.Composite {
        private final Info.Composite before = new Composite("Before");
        private final Info.Composite after = new Composite("After");

        public LootComposite() {
            super("Loot");
            children.add(before);
            children.add(after);
        }

        @Override
        public void addChildren(Info info) {
            throw new UnsupportedOperationException("LootComposite cannot add custom children");
        }

        @Override
        public Collection<Info> getChildren() {
            List<Info> c = new ArrayList<>();
            c.add(before.getChildren().isEmpty() ? new TitledInfo("before {}") : before);
            c.add(after.getChildren().isEmpty() ? new TitledInfo("after {}") : after);
            return c;
        }

        public Info.Composite getBefore() {
            return before;
        }

        public Info.Composite getAfter() {
            return after;
        }
    }
}
