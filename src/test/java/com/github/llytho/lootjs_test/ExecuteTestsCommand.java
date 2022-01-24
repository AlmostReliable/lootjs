package com.github.llytho.lootjs_test;

import com.github.llytho.lootjs.core.DebugStack;
import com.google.common.base.Strings;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.kubejs.util.ConsoleJS;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExecuteTestsCommand implements Command<CommandSource> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INDENT = "    ";

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> cmd = Commands
                .literal(TestGlobalLootJSMod.MODID)
                .requires(commandSource -> commandSource.hasPermission(4))
                .executes(new ExecuteTestsCommand());
        event.getDispatcher().register(cmd);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity e = context.getSource().getEntity();
        if (!(e instanceof ServerPlayer)) {
            return 0;
        }

        Supplier<String> instantNow = () -> {
            String instant = Instant.now().toString();
            return instant.substring(instant.indexOf('T') + 1).replace("Z", "").replace(".", "_");
        };

        context.getSource().sendSuccess(new StringTextComponent("______________________________________"), true);
        context.getSource().sendSuccess(new StringTextComponent(instantNow.get() + " Starting tests..."), true);
        AllTests.loadAll();

        DebugStack debugStack = new DebugStack();
        TestHelper helper = new TestHelper(debugStack, context.getSource().getLevel(), (ServerPlayer) e);
        StringBuilder builder = new StringBuilder().append("\n");

        try {
            for (Map.Entry<String, SimpleTest> entry : AllTests.TESTS.entrySet()) {
                debugStack.h1(entry.getKey());
                entry.getValue().test(helper);
            }

            String fin = "Finalize tests:";
            String result = helper.getFailed() + " / " + helper.getSum() + " failed";

            builder.append("\n").append(fin).append("\n").append(INDENT).append(result).append("\n");
            context.getSource().sendSuccess(new StringTextComponent(instantNow.get() + " " + fin + " " + result), true);
        } catch (Exception exception) {
            String crash = "Tests crashed. Stacktrace logged. Abort further tests.";
            context.getSource().sendFailure(new StringTextComponent(crash).withStyle(TextFormatting.DARK_RED));

            StackTraceElement[] stackTrace = exception.getStackTrace();
            debugStack.pushLayer();
            debugStack.write("[EXCEPTION ERROR] " + crash);
            debugStack.pushLayer();
            debugStack.write("[ERR  ] " + exception.getClass() + ": " + exception.getMessage());
            for (int i = 0; i < Math.max(stackTrace.length, 15); i++) {
                debugStack.write("[ERR  ] " + stackTrace[i].toString());
            }
            debugStack.popLayer();
            debugStack.popLayer();
        } finally {
            for (DebugStack.Entry dse : debugStack.getEntries()) {
                builder.append(Strings.repeat(INDENT, dse.getLayer())).append(dse.getText()).append("\n");
            }
            ConsoleJS.SERVER.info(builder.toString());
        }
        return 0;
    }
}
