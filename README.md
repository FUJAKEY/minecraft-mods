# minecraft-mods

## Building energymod

1. Install Java 8 JDK and a compatible Gradle version (7.3.3 is used).
2. Run `gradle wrapper` to generate the wrapper jar if it is missing. You may use a minimal wrapper script like `wrapper.gradle` with a `Wrapper` task.
3. Execute `./gradlew build` to compile the mod. After a successful build, remove the `build/` directory and any generated binaries to keep the repository clean.
