# minecraft-mods

## Building pipi_mod locally

1. Install Java 8 JDK and a compatible Gradle version (7.3.3 is used).
2. Run `gradle wrapper` to generate the wrapper jar if it is missing. You may use a minimal wrapper script like `wrapper.gradle` with a `Wrapper` task.
3. Execute `./gradlew build` to compile the mod. After a successful build, remove the `build/` directory and any generated binaries to keep the repository clean.

## Continuous integration

GitHub Actions automatically installs the toolchain, builds the mod, and publishes the compiled JARs as workflow artifacts. Every push, pull request, or manual dispatch triggers the **Build and Package pipi_mod** workflow, which:

- checks out the repository;
- provisions Temurin JDK 8 and configures the Gradle wrapper;
- runs `./gradlew build --console=plain --no-daemon` inside `pipi_mod`; and
- uploads all JARs from `pipi_mod/build/libs` as downloadable artifacts named `pipi-mod-artifacts`.

You can download the latest artifacts from the workflow run summary on GitHub without needing to build locally.
