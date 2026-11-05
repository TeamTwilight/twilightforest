Twilight Forest [![Discord](https://img.shields.io/discord/313006291012288521.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/6v3z26B) [![Crowdin](https://badges.crowdin.net/twilight-forest/localized.svg)](https://crowdin.com/project/twilight-forest) [![Curseforge](http://cf.way2muchnoise.eu/full_227639_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest) [![Curseforge](http://cf.way2muchnoise.eu/versions/For%20MC_227639_all.svg)](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest)
==============

This is the home of the Twilight Forest mod for Minecraft 1.12 and beyond.

## Licensing
Everything not stated below are **All Rights Reserved** by default.

Java Code specifically under these listed directories follow this [LICENSE file here](LICENSE):

- [src/main/java](src/main/java)
- [src/asm/java](src/asm/java)
- [src/data/java](src/data/java)
- [src/test/java](src/test/java)

## Supported Versions
### Latest Minecraft Version
This would be the most recent Minecraft Version that we have releases for on CurseForge.

Right now this is **26.1.2**.
### LTS
These will generally be the previous most recent Minecraft Version we have CurseForge releases for.

Normally that would be 1.21.1, however we have currently dropped support for that version. The next upcoming LTS will be 26.1.2.

## Downloads
Releases will be available on [CurseForge](https://minecraft.curseforge.com/projects/the-twilight-forest) when ready.

Alternatively, grab your experimental builds from our [Discord](https://discord.gg/6v3z26B). See the #faq channel for more details.

## Community
Keep in touch with development on our [Discord server](https://discord.gg/6v3z26B). We also have a [Twitter account](https://twitter.com/teamtwilightdev) we post development and Twilight Forest-related content on.

## Translation
Submitting translations for Twilight Forest can be done via our [Crowdin page](https://crowdin.com/project/twilight-forest). Consider helping us out if you're fluent in any language at all! Alternatively, translations can still be submitted via pull requests.

## Maven
### 1.20.6+
https://maven.tamaized.com/#/releases/team-twilight/twilightforest/

```
repositories {
    maven {
        url = "https://maven.tamaized.com/releases"
    }
}

dependencies {
    implementation "team-twilight:twilightforest:{version}:universal"
}
```
### 1.20.5 and below
https://modmaven.dev/teamtwilight/twilightforest/

```
repositories {
    maven {
        url = "https://modmaven.dev/"
    }
}

dependencies {
    implementation fg.deobf("teamtwilight:twilightforest:{version}:universal")
}
```
