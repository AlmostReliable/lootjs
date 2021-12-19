package com.github.llytho.lootjs.kube;

import com.github.llytho.lootjs.core.LootModificationDebug;
import com.google.common.base.Strings;
import dev.latvian.kubejs.util.ConsoleJS;
import net.minecraft.loot.LootContext;

import java.util.function.BiConsumer;

public class ConsoleDebugAction implements BiConsumer<LootContext, LootModificationDebug> {
    public static final String BASE_INDENT = Strings.repeat("    ", LootModificationDebug.BASE_LAYER);

    @Override
    public void accept(LootContext context, LootModificationDebug lmd) {
        if (lmd.getEntries().isEmpty()) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n### Executing loot modifications ###\n");
        LootModificationDebug cd = LootModificationDebug.context(context);

        builder.append(BASE_INDENT).append("[ Loot information ] \n");
        for (LootModificationDebug.Entry entry : cd.getEntries()) {
            builder
                    .append(BASE_INDENT)
                    .append(Strings.repeat("    ", entry.getLayer()))
                    .append(entry.getText())
                    .append("\n");
        }
        builder.append(BASE_INDENT).append("[ Modifications ] \n");

        for (LootModificationDebug.Entry entry : lmd.getEntries()) {
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
