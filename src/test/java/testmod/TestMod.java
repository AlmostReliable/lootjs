package testmod;

import com.almostreliable.lootjs.LootEvents;
import com.almostreliable.lootjs.LootTableEvent;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.gametest.GameTestHooks;
import testmod.event.Entry;

@Mod("testmod")
public class TestMod {

    public TestMod() {
        if (GameTestHooks.isGametestEnabled()) {
            LootEvents.listen(registry -> {
                var event = new LootTableEvent() {

                    @Override
                    public WritableRegistry<LootTable> registry() {
                        return registry;
                    }
                };

                Entry.init(event);
            });
        }
    }
}
