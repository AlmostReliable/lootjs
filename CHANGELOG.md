# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog],
and this project adheres to [Semantic Versioning].

## [Unreleased]
- /

## [2.3.0-beta] - 2022-04-18
We are now on 1.18.2! 

### Added
- [ItemFilter](https://github.com/AlmostReliable/lootjs-forge/wiki/1.18.2-Types#itemfilters)
- [Functions](https://github.com/AlmostReliable/lootjs-forge/wiki/1.18.2-Functions) to apply loot table functions
- [CustomPlayerAction](https://github.com/AlmostReliable/lootjs-forge/wiki/1.18.2-Actions#playeractioncallback)

### Changed
- Refactoring the builders for loot modifications.
- Removing usage of kubejs `DamageSourceJS` and use the vanilla damage source.
- Rename actions. You can find the new names [here](https://github.com/AlmostReliable/lootjs-forge/wiki/1.18.2-Actions)
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
[unreleased]: https://github.com/AlmostReliable/lootjs-forge/compare/1.18...HEAD
[2.3.0-beta]: https://github.com/AlmostReliable/lootjs-forge/releases/tag/v1.18-2.3.0-beta
[2.2.0]: https://github.com/AlmostReliable/lootjs-forge/releases/tag/v1.18-2.2.0
[2.1.0]: https://github.com/AlmostReliable/lootjs-forge/releases/tag/v1.18-2.1.0
[2.0.0]: https://github.com/AlmostReliable/lootjs-forge/releases/tag/v1.18-2.0.0
