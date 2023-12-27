package testmod;

import com.almostreliable.lootjs.LootJS;
import net.neoforged.fml.common.Mod;

@Mod("testmod")
public class TestMod {


    public TestMod() {
        LootJS.LOG.info("TestMod loaded");
    }
}
