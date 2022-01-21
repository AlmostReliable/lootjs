onEvent("lootjs", (event) => {
    event
        .addModifierForType(LootType.BLOCK)
        .matchBlock("#forge:ores") // keep in mind this is a block tag not an item tag
        .matchEquip(EquipmentSlot.MAINHAND, Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .thenAdd("minecraft:gravel");

    // for MainHand and OffHand you can also use:
    // matchMainHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
    // matchOffHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
});
