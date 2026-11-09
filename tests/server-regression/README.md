# Dedicated-server regression fixture

Use a disposable Minecraft 26.1.2 / NeoForge 126 server, Java 25 and Gradle 9.2.1.
This opt-in test add-on is not part of the release and must never go on a live server.

1. Put the tested twilightforest JAR and its required mod dependencies in `mods/` next to this file.
2. Run `gradle -p tests/server-regression jar` from the repository root.
3. Add `build/libs/server-regression-1.0.0.jar` to a disposable dedicated server with the same mods.
4. Set `server-ip=127.0.0.1`, start that server with no players, then enter `serverregression` in its console.
5. Check that every named case reports `passed=true`; stop and discard the test server afterward.

These cases run the real mod methods against native Minecraft classes. Reflection accesses private callbacks/state; the Twilight fixture allocates an isolated menu without opening a GUI. They do not require a graphical client or modify real player data. Tests are intentionally excluded from normal release builds.
