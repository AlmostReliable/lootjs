onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier(LootType.ENTITY) // you also can use multiple types
        .logName("It's raining loot") // you can set a custom name for logging
        .weatherCheck({
            raining: true,
        })
        .thenModify(Ingredient.getAll(), (itemStack) => {
            // you have to return an item!
            return itemStack.withCount(itemStack.getCount() * 2);
        });
});
