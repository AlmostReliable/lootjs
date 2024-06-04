package testmod.event;

import com.almostreliable.lootjs.core.entry.LootEntry;
import com.almostreliable.lootjs.loot.LootTableEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

public class Entry {

    public static final String CREEPER_TEST_POOL = "creeper_test_pool";

    public static void init(LootTableEvent event) {
        event.getEntityTable(EntityType.COW).clear().createPool().addEntry(LootEntry.ofItem(Items.DIAMOND));

        event.getEntityTable(EntityType.CREEPER).createPool(pool -> {
//    pool.name(CREEPER_TEST_POOL).addEntry(LootEntry.ofItem(Items.ANDESITE).)
        });
    }
}
