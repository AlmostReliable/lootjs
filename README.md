<div align="center">
<h1>LootJS</h1>

A [Minecraft] mod for packdevs to easily modify the loot system with [KubeJS].

[![Version][version_badge]][version_link]
[![Total Downloads][total_downloads_badge]][curseforge]
[![Workflow Status][workflow_status_badge]][workflow_status_link]
[![License][license_badge]][license]

[Discord] | [CurseForge]

</div>

## **📑 Overview**
This is a mod for [Minecraft] [Forge] and needs [KubeJS].<br>
The mod uses the [Global Loot System][forgeloot] from Forge together with KubeJS to add functionality to modify loot based on given conditions and actions. 
## **🔧 Installation**
1. Download the latest **mod jar** from [CurseForge] or the latest [releases].
2. Download the latest **mod jar** from [KubeJS].
3. Install Minecraft [Forge].
4. Drop both **jar files** into your mods folder.

## **✏️ Your first loot modification**
Loot modifications are handled server side. So all your scripts will go into your `your_minecraft_instance/kubejs/server_scripts` directory. Just create a `.js` file and let's get started.

Let's add a simple example which adds an additional gunpowder for creepers:
```js
onEvent("lootjs", (event) => {
    event
        .addModifierForLootTable("minecraft:entities/creeper")
        .randomChance(0.3) // 30% chance
        .thenAdd("minecraft:gunpowder");
});
```

Instead of using a loot table for reference you also can apply the modification to all entities:
```js
onEvent("lootjs", (event) => {
    event
        .addModifierForType(LootType.ENTITY) // you also can use multiple types
        .logName("It's raining loot") // you can set a custom name for logging
        .weatherCheck({
            raining: true,
        })
        .thenModify(Ingredient.getAll(), (itemStack) => {
            // you have to return an item!
            return itemStack.withCount(itemStack.getCount() * 2);
        });
});
```
Now, let's check if the player holds a specific item:
```js
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
```

## **⚙️ More Information**
For more information about the usage and the functionality of the mod, please
visit our [wiki] or explore the [examples].

## **❌ Disable loot tables for loot modifications**
Some blocks like leaves are getting randomly destroyed. If you don't want them to trigger your loot modifications, you can disable their loot tables. The default loot tables will still be triggered.
```js
onEvent("lootjs", (event) => {
    // all leaves disabled via regex
    event.disableLootModification(/.*:blocks\/.*_leaves/);
    
    // disable bats
    event.disableLootModification("minecraft:entities/bat");
});

```

## **📜 Enable logging for loot modifications**
With a lot of modifications it can be hard to track which modification triggers on specific conditions. With `enableLogging` LootJS will log every modification trigger into `your_minecraft_instance/logs/kubejs/server.txt`
```js
onEvent("lootjs", (event) => {
    event.enableLogging();
});
```

How it would look like for the `additional gunpowder` and `raining loot` examples:
```lua
[ Loot information ] 
    LootTable    : "minecraft:entities/creeper"
    Loot Type    : ENTITY
    Current loot :
    Position     : (151.86, 80.00, -264.23)
    Entity       : Type="minecraft:creeper", Id=378, Dim="minecraft:overworld", x=151.86, y=80.00, z=-264.23
    Killer Entity: Type="minecraft:player", Id=122, Dim="minecraft:overworld", x=152.52, y=80.00, z=-262.85
    Direct Killer: Type="minecraft:player", Id=122, Dim="minecraft:overworld", x=152.52, y=80.00, z=-262.85
    Player       : Type="minecraft:player", Id=122, Dim="minecraft:overworld", x=152.52, y=80.00, z=-262.85
    Player Pos   : (152.52, 80.00, -262.85)
    Distance     : 1.53
    MainHand     : 1 netherite_sword {Damage:0}
[ Modifications ] 
    🔧 LootTables["minecraft:entities/creeper"] {
        ❌ RandomChance
        ➥ conditions are false. Stopping at AddLootAction
    }
    🔧 "It's raining loot" {
        ✔️ WeatherCheck
        ➥ invoke ModifyLootAction
    }
```

## **🎓 License**
This project is licensed under the [GNU Lesser General Public License v3.0][license].

<!-- Badges -->
[version_badge]: https://img.shields.io/github/v/release/AlmostReliable/lootjs-forge?style=flat-square
[version_link]: https://github.com/AlmostReliable/lootjs-forge/releases/latest
[total_downloads_badge]: http://cf.way2muchnoise.eu/full_570630.svg?badge_style=flat
[workflow_status_badge]: https://img.shields.io/github/workflow/status/AlmostReliable/lootjs-forge/CI?style=flat-square
[workflow_status_link]: https://github.com/AlmostReliable/lootjs-forge/actions
[license_badge]: https://img.shields.io/github/license/AlmostReliable/lootjs-forge?style=flat-square

<!-- Links -->
[forgeloot]: https://mcforge.readthedocs.io/en/latest/items/globallootmodifiers/
[minecraft]: https://www.minecraft.net/
[kubejs]: https://www.curseforge.com/minecraft/mc-mods/kubejs-forge
[discord]: https://discord.com/invite/ThFnwZCyYY
[releases]: https://github.com/AlmostReliable/lootjs-forge/releases
[curseforge]: https://www.curseforge.com/minecraft/mc-mods/lootjs-forge
[forge]: http://files.minecraftforge.net/
[wiki]: https://github.com/AlmostReliable/lootjs-forge/wiki
[changelog]: CHANGELOG.md
[license]: LICENSE
[examples]: examples/server_scripts
