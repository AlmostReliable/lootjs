# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog],
and this project adheres to [Semantic Versioning].

## [Unreleased]
- /

## [2.7.7] - 2022-11-23
### Added
- `disableWitherStarDrop()`, `disableCreeperHeadDrop()`, `disableSkeletonHeadDrop()`, `disableZombieHeadDrop()`
- Skip 2.7.6 as it's a 1.19 fix version

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
[2.7.7]: https://github.com/AlmostReliable/lootjs/releases/tag/v1.18-2.7.7
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
