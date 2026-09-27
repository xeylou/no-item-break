<div align="center">
  <a href="https://github.com/xeylou/no-item-break"><img src="https://raw.githubusercontent.com/xeylou/no-item-break/main/src/main/resources/assets/no_item_break/icon.png" alt="No Item Break icon" width="128"></a>
  <h1>No Item Break</h1>
</div>

a simple client-side mod that takes your tools, weapons & armor off before they break.

works with Fabric 26.3. when an item's durability drops below 10, it is moved to another inventory slot if one is available; otherwise, it is dropped to prevent it from beaking. works with all armor pieces, including elytras, & every item with a durability value.

download the jar from the [releases page](https://github.com/xeylou/no-item-break/releases) and put it in your `mods` folder.
the mod is **client-side only**: it works in singleplayer and servers, but **the server does not need it**.

## before you create an issue

- **elytras** are taken off at 10 durability too, even in mid-air.
- **a single strong hit can still break an item**, for example a shield blocking a creeper explosion. the mod can only react after the server has applied the damage.
- **full inventory:** the item is dropped on the ground. pick it up or it despawns after 5 minutes. you can use `F3 + B` to find it quicker.
- the mod moves items with a normal inventory click, the same one as a shift-click. some servers forbid automated inventory actions, so check the rules of the server you play on.

## reporting a bug

if you have any idea to improve the mod, please create a pull request or contact me on [discord](https://discordapp.com/users/835863642498793483).

open an issue: https://github.com/xeylou/no-item-break/issues/new/choose

please read the [contributing guide](https://github.com/xeylou/no-item-break/blob/main/CONTRIBUTING.md) first.

## building from source

need JDK 25. gradle comes w/ the project.

```bash
./gradlew build
```

the jar file is created in `build/libs/`.

## license

Copyright 2026 xeylou.
Licensed under the [Apache License 2.0](https://github.com/xeylou/no-item-break/blob/main/LICENSE).