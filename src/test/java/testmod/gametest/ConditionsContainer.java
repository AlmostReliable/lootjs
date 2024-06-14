package testmod.gametest;

import com.almostreliable.lootjs.BuildConfig;
import com.almostreliable.lootjs.loot.LootCondition;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(value = BuildConfig.MOD_ID)
@PrefixGameTestTemplate(false)
public class ConditionsContainer {

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_Entity(GameTestHelper helper) {
        helper.succeedIf(() -> {
            var condition = (LootItemEntityPropertyCondition) new LootCondition().matchEntity(EntityPredicate.Builder.entity());
            GameTestUtils.assertEquals(helper,
                    condition.entityTarget,
                    LootContext.EntityTarget.THIS);
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_Killer(GameTestHelper helper) {
        helper.succeedIf(() -> {
            var condition = (LootItemEntityPropertyCondition) new LootCondition().matchKiller(EntityPredicate.Builder.entity());
            GameTestUtils.assertEquals(helper,
                    condition.entityTarget,
                    LootContext.EntityTarget.ATTACKER);
        });
    }

    @GameTest(template = GameTestTemplates.EMPTY)
    public void entityTarget_DirectKiller(GameTestHelper helper) {
        helper.succeedIf(() -> {
            var condition = (LootItemEntityPropertyCondition) new LootCondition().matchDirectKiller(EntityPredicate.Builder.entity());
            GameTestUtils.assertEquals(helper,
                    condition.entityTarget,
                    LootContext.EntityTarget.DIRECT_ATTACKER);
        });
    }
}
