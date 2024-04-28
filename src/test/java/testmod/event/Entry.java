package testmod.event;

import com.almostreliable.lootjs.LootTableEvent;
import com.almostreliable.lootjs.core.entry.LootEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

public class Entry {

    public static void init(LootTableEvent event) {
        event.getEntityTable(EntityType.COW).clear().createPool().addEntry(LootEntry.ofItem(Items.DIAMOND));
    }
}
