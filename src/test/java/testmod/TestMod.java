package testmod;

import com.almostreliable.lootjs.LootEvents;
import com.almostreliable.lootjs.loot.LootTableEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.gametest.GameTestHooks;
import testmod.event.Entry;

@Mod("testmod")
public class TestMod {

    public TestMod() {
        if (GameTestHooks.isGametestEnabled()) {
            LootEvents.listen(registry -> {
                var event = new LootTableEvent(registry);
                Entry.init(event);
            });
        }
    }
}
