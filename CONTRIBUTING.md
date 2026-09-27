# contributing

thanks for your interest in No Item Break, a lot.
the mod is deliberately tiny, so the most useful contributions are clear bug reports.

## reporting a bug

1. search the [existing issues](https://github.com/xeylou/no-item-break/issues?q=is%3Aissue), including closed ones.
2. update to the latest version & try again with **only this mod** installed. Fabric API is not needed.
3. [open a bug report](https://github.com/xeylou/no-item-break/issues/new/choose) & fill in the form. attach `logs/latest.log`, or paste a link from [mclo.gs](https://mclo.gs).

## suggesting a feature

the mod does one thing: it takes equipped items off before they break.
it has no config file, no commands, no menus & no dependencies.

before writing any code, open a feature request so we can discuss on the idea (it could save you a lot of time lol).

## security issues

do not open a public issue: follow the [security policy](https://github.com/xeylou/no-item-break/blob/main/SECURITY.md).

## dev

- install JDK 25. Gradle comes with the project (`gradlew`).
- build w/ `./gradlew build`. the `.jar` is created in `build/libs/`.
- test by copying the jar into the `mods` folder of your Minecraft 26.3 Fabric instance. try singleplayer **and** a vanilla server: the mod is client-side & must work on both.
- Minecraft 26.3 is not obfuscated & uses Mojang's names, so you can read the game's classes directly. e.g.:
  `javap -p -classpath minecraft-26.3-client.jar net.minecraft.client.player.LocalPlayer`

### code style

- indent with tabs, like the existing files.
- mark every field or helper you add to a target class with `@Unique`.
- comments explain *why*, not *what*.
- keep it small pleease: no new dependency, no config.

## commits & pr

- write commit messages with [Conventional Commits](https://www.conventionalcommits.org/): `fix: …`, `feat: …`, `docs: …`, `build: …`, `ci: …`, `refactor: …`, `chore: …`.
- one topic per pull request. explain how you tested it in game.
- add a line to the `Unreleased` section of `CHANGELOG.md` if players will notice the change.
- if AI tools helped you write the change, say so in the pull request (no shame).

## License

By contributing, you agree that your contributions are licensed under the [Apache License 2.0](https://github.com/xeylou/no-item-break/blob/main/LICENSE), as stated in section 5 of the license.
