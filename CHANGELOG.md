# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog],
and this project adheres to [Semantic Versioning].

## [Unreleased]
- /

## [3.0.0] - 2024-06-11

- Alpha Release for FTB modpack. I wanted to skip 1.20.4 lol. LootJS 3.0 is not compatible with the current wiki. Read
  the source if you want to use it. Wiki will be available for Minecraft 1.21, when LootJS 3.0 is fully released.

## [2.10.3] - 2023-09-27
### Fixed
- Fixed null pointer exception when using `addLootTableModifier` and an entity dies

## [2.10.2] - 2023-09-23
### Add
- Add custom loot function `customFunction(json)`

## [2.10.1] - 2023-09-18
### Fix
- Fix bug where loot does not drop for entities on fabric

## [2.10.0] - 2023-09-03
### Changed
- 1.20.1 Release

## [2.9.1] - 2023-08-17
### Changed
- Fix bug where loot does not drop if CraftTweaker is installed together with LootJS on fabric

## [2.9.0] - 2023-07-18
### Changed
- Update to KubeJS 6.1

## [2.8.0] - 2023-04-12
### Added
- Added `LootEntry.when()` and `blockEntityPredicate`

## [2.7.11] - 2023-04-03
### Fix
- Fix item filters on forge. Moved the specific filters from last update to `ForgeItemFilter.canPerformAnyAction()` and `ForgeItemFilter.canPerformAction()`.

## [2.7.10] - 2023-04-02
### Added
- Added `ItemFilter.canPerformAnyAction()` and `ItemFilter.canPerformAction()` for Forge to check if an item can perform a tool action.

### Fix
- Fix crash when trying to remove global modifiers from forge

## [2.7.9] - 2023-01-08
### Changed
- Add structure tags `#tag` support in `anyStructure()` condition
- Remove structure id validation in `anyStructure()` because of timing issues ... :')
- Fix load issue on fabric (Should fix weird null pointer exception on fabric)

## [2.7.8] - 2023-01-08
### Changed
- Fix ([#10](https://github.com/AlmostReliable/lootjs/issues/10))
- Remove tag validation because of timing issues

## [2.7.7] - 2022-11-23
### Added
- `disableWitherStarDrop()`, `disableCreeperHeadDrop()`, `disableSkeletonHeadDrop()`, `disableZombieHeadDrop()`

## [2.7.6] - 2022-11-14
### Changed
- Bump KubeJS Version ([#9](https://github.com/AlmostReliable/lootjs/issues/9))

## [2.7.5] - 2022-09-02
### Changed
- Fix `addBlockLootModifier` when using tags

## [2.7.4] - 2022-07-30
### Changed
- Fix invalid items result into an always true filter.
- Change `ItemFilter.hasEnchantment()` so it works with regex and enchanted books.

## [2.7.3] - 2022-07-05
### Changed
- Fix loot context check for params when loot is rolled multiple times.

## [2.7.2] - 2022-07-05
### Changed
- Fabric Fix: Old drop still dropped.

## [2.7.1] - 2022-07-04
### Changed
- Fabric Fix: Modifications did not trigger for entities.

## [2.7.0] - 2022-06-27
### Added
- Hello Fabric! LootJS is now for Fabric

## [2.6.0] - 2022-06-23
### Added
- `replaceLoot(filter, entry, preserveCount)` now exists with the third argument to preserve item count (use true or false)
### Changed
- `replaceLoot` now also takes an `LootEntry` as the second argument

## [2.5.0] - 2022-06-16
### Added
- Added `dropExperience(amount)` action to drop experience orbs

## [2.4.0] - 2022-06-15
### Added
- Added `LootEntry` to directly call loot functions on items.
- Added `limitCount` function with single argument.

### Changed
- `.pool()` action is now `.group()`. There was some misunderstanding for the user as LootJS pools work differently than vanilla pools.

### Removed
- Remove deprecated actions from 2.3.0

## [2.3.0-beta] - 2022-04-18
We are now on 1.18.2!

### Added
- [ItemFilter](https://github.com/AlmostReliable/lootjs/wiki/1.18.2-Types#itemfilters)
- [Functions](https://github.com/AlmostReliable/lootjs/wiki/1.18.2-Functions) to apply loot table functions
- [CustomPlayerAction](https://github.com/AlmostReliable/lootjs/wiki/1.18.2-Actions#playeractioncallback)

### Changed
- Refactoring the builders for loot modifications.
- Removing usage of kubejs `DamageSourceJS` and use the vanilla damage source.
- Rename actions. You can find the new names [here](https://github.com/AlmostReliable/lootjs/wiki/1.18.2-Actions)
- `biome()` & `anyBiome()` conditions now uses the new tag system

## [2.2.0] - 2022-03-15
### Added
- Action `thenRollPool`
### Changed
- Refactor loot logging

## [2.1.0] - 2022-03-04
### Added
- Action `thenAddWeighted`
- Conditions `hasAnyStage`, `playerPredicate`, `entityPredicate`, `directKillerPredicate`, `killerPredicate`
### Changed
- bump to newest KubeJS version

## [2.0.0] - 2022-01-25
- initial release for 1.18.1

<!-- Links -->
[keep a changelog]: https://keepachangelog.com/en/1.0.0/
[semantic versioning]: https://semver.org/spec/v2.0.0.html

<!-- Versions -->
[unreleased]: https://github.com/AlmostReliable/lootjs/compare/1.18...HEAD
[2.10.3]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.10.3
[2.10.2]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.10.2
[2.10.1]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.10.1
[2.9.1]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.9.1
[2.9.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.9.0
[2.7.11]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.11
[2.7.10]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.10
[2.7.9]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.9
[2.7.8]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.8
[2.7.7]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.7
[2.7.6]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.6
[2.7.5]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.5
[2.7.4]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.4
[2.7.3]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.3
[2.7.2]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.2
[2.7.1]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.1
[2.7.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.0
[2.6.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.6.0
[2.5.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.5.0
[2.4.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.4.0
[2.3.0-beta]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.3.0-beta
[2.2.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.2.0
[2.1.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.1.0
[2.0.0]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.0.0
