package com.almostreliable.lootjs.loot.results;

import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.ILootHandler;
import com.almostreliable.lootjs.loot.action.CompositeLootAction;
import com.almostreliable.lootjs.loot.action.LootItemFunctionWrapperAction;
import com.almostreliable.lootjs.loot.condition.AndCondition;
import com.almostreliable.lootjs.loot.condition.NotCondition;
import com.almostreliable.lootjs.loot.condition.OrCondition;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

public class LootInfoCollector {

    public static final Class<?>[] COMPOSITES = new Class[]{
            OrCondition.class,
            AndCondition.class,
            NotCondition.class,
            CompositeLootAction.class,
            };

    protected final List<Info> firstLayer = new ArrayList<>();
    protected final Stack<Info.Composite> cursorHistory = new Stack<>();

    @Nullable
    public static Info create(@Nullable LootInfoCollector collector, ILootHandler lootHandler) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING || collector == null) return null;

        Info info = createBaseInfo(lootHandler);
        for (Class<?> composite : COMPOSITES) {
            if (composite.isInstance(lootHandler)) {
                return createInfo(collector, new Info.Composite(info));
            }
        }

        return createInfo(collector, info);
    }

    public static void createFunctionInfo(@Nullable LootInfoCollector collector, LootItemFunction function) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING || collector == null) return;
        Info info = new Info.TitledInfo(Icon.ACTION, function.getClass().getSimpleName());
        collector.addOrPush(info);
    }

    @Nullable
    public static Info createInfo(@Nullable LootInfoCollector collector, Info info) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING || collector == null) return null;
        collector.addOrPush(info);
        return info;
    }

    public static void finalizeInfo(@Nullable LootInfoCollector collector, @Nullable Info info) {
        finalizeInfo(collector, info, null);
    }

    public static void finalizeInfo(@Nullable LootInfoCollector collector, @Nullable Info info, @Nullable Boolean result) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING || collector == null || info == null) return;

        if (result != null && info instanceof Info.ResultInfo resultInfo) {
            resultInfo.setResult(result);
        }

        if (info instanceof Info.Composite composite) {
            Info.Composite oldComposite = collector.pop();
            if (!oldComposite.equals(composite)) {
                throw new IllegalStateException(
                        "pop() mismatch on history. Some composite info was not finalize correctly");
            }

            finalizeInfo(collector, oldComposite.getBase(), result);
        }
    }

    private static Info createBaseInfo(ILootHandler lootHandler) {
        String title = createTitle(lootHandler);

        if (lootHandler instanceof LootItemCondition) {
            return new Info.ResultInfo(title);
        }

        return new Info.TitledInfo(Icon.ACTION, title);
    }

    private static String createTitle(ILootHandler lootHandler) {
        if (lootHandler instanceof LootItemFunctionWrapperAction lif) {
            return lif.getLootItemFunction().getClass().getSimpleName();
        }

        return lootHandler.getClass().getSimpleName();
    }

    public static void append(Info info, int indentDepth, StringBuilder sb) {
        String indent = StringUtils.repeat("    ", indentDepth);

        sb.append(indent).append(info.transform());
        if (info instanceof Info.Composite composite) {
            sb.append(" {\n");
            for (Info child : composite.getChildren()) {
                append(child, indentDepth + 1, sb);
            }
            sb.append(indent).append("}");
        }
        sb.append("\n");
    }

    public Collection<Info> getFirstLayer() {
        return Collections.unmodifiableCollection(firstLayer);
    }

    public Info.Composite pop() {
        return cursorHistory.pop();
    }

    public void add(Info info) {
        if (cursorHistory.empty()) {
            firstLayer.add(info);
        } else {
            Info.Composite peek = cursorHistory.peek();
            peek.addChildren(info);
        }

    }

    public void addOrPush(Info info) {
        add(info);
        if (info instanceof Info.Composite composite) {
            cursorHistory.push(composite);
        }
    }

    public void append(StringBuilder stringBuilder, int indentDepth) {
        for (Info root : getFirstLayer()) {
            append(root, indentDepth, stringBuilder);
        }
    }
}
