package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.DebugStack;
import com.google.common.base.Strings;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.function.BiConsumer;

public class ConsoleDebugAction implements BiConsumer<LootContext, DebugStack> {
    public static final String BASE_INDENT = Strings.repeat("    ", DebugStack.BASE_LAYER);

    @Override
    public void accept(LootContext context, DebugStack lmd) {
        if (lmd.getEntries().isEmpty()) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n### Executing loot modifications ###\n");
        DebugStack cd = DebugStack.context(context);

        builder.append(BASE_INDENT).append("[ Loot information ] \n");
        for (DebugStack.Entry entry : cd.getEntries()) {
            builder
                    .append(BASE_INDENT)
                    .append(Strings.repeat("    ", entry.getLayer()))
                    .append(entry.getText())
                    .append("\n");
        }
        builder.append(BASE_INDENT).append("[ Modifications ] \n");

        for (DebugStack.Entry entry : lmd.getEntries()) {
            builder
                    .append(BASE_INDENT)
                    .append(Strings.repeat("    ", entry.getLayer()))
                    .append(entry.getText())
                    .append("\n");
        }

        cd.reset();
        lmd.reset();
        ConsoleJS.SERVER.info(builder.toString());
    }
}
