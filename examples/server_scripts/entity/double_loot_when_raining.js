onEvent("lootjs", (event) => {
    event
        .addModifierForType(LootType.ENTITY) // you also can use multiple types
        .logName("It's raining loot") // you can set a custom name for logging
        .weatherCheck({
            raining: true,
        })
        .thenModify(Ingredient.getAll(), (itemStack) => {
            // you have to return a new item!
            return itemStack.withCount(itemStack.getCount() * 2);
        });
});
