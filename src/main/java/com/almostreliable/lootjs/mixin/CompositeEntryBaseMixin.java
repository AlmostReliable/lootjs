package com.almostreliable.lootjs.mixin;

import com.almostreliable.lootjs.loot.extension.CompositeEntryBaseExtension;
import net.minecraft.world.level.storage.loot.entries.ComposableEntryContainer;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CompositeEntryBase.class)
public abstract class CompositeEntryBaseMixin implements CompositeEntryBaseExtension {

    @Mutable @Final @Shadow public LootPoolEntryContainer[] children;

    @Mutable @Shadow @Final private ComposableEntryContainer composedChildren;

    @Shadow
    protected abstract ComposableEntryContainer compose(ComposableEntryContainer[] entries);

    @Override
    public LootPoolEntryContainer[] lootjs$getEntries() {
        return this.children;
    }

    @Override
    public void lootjs$setEntries(LootPoolEntryContainer[] children) {
        this.children = children;
        this.composedChildren = this.compose(this.children);
    }
}
