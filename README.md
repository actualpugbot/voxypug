# Voxypug

Voxy is a level-of-detail rendering mod for Minecraft. This fork targets
Minecraft Java Edition **26.3**, Fabric Loader **0.19.5**, and Sodium **0.9.2**.

## Build

Use a **Java 25 JDK** and the committed Gradle wrapper:

```sh
./gradlew build
```

The mod JAR is written to `build/libs/`. Development dependencies are pinned in
`gradle.properties`; Minecraft's LWJGL version is matched in `build.gradle`.

For the local Java 25 toolchain installed during setup in this checkout:

```sh
export JAVA_HOME="$PWD/.gradle/toolchains/java25"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew build
```

## Run

Install the built JAR with Minecraft 26.3, Fabric Loader, Fabric API, and Sodium
for 26.3. Iris 1.11.6 for 26.3 is optional. Select Minecraft's **OpenGL** graphics
backend with OpenGL 4.6 support; Voxy's renderer does not support Vulkan and disables itself there.

To launch the development client with the core runtime mods:

```sh
./gradlew runClient
```

To include Iris in a development launch:

```sh
./gradlew runClient -Pruntime.iris_version=1.11.6+26.3-fabric
```

Other optional integrations compile against their pinned 26.3 releases.

## Verify in-world startup

With an OpenGL-capable desktop available, run the development-only client test:

```sh
./gradlew runClientGameTest
./gradlew runClientGameTest -Pruntime.iris_version=1.11.6+26.3-fabric
```

The test creates a temporary flat world, waits for chunks and the Voxy renderer,
keeps rendering for 120 ticks, saves a screenshot, and exits. Test artifacts are
under `build/run/clientGameTest/`. The test mod is excluded from the production
JAR. A selected shaderpack and hardware-GPU visual testing are separate checks.

### Validation on 2026-10-01

- The clean Java 25 build and access-widener validation passed.
- Client startup passed with Sodium, both with and without Iris installed.
- The automated flat-world test passed in both configurations: the Voxy render
  system was created, the world rendered for 120 ticks, and shutdown completed.
- In-world tests used WSL llvmpipe with `MESA_GL_VERSION_OVERRIDE=4.6` and
  `MESA_GLSL_VERSION_OVERRIDE=460`. Unmodified WSL OpenGL 4.5 cannot compile
  Voxy's existing GLSL 4.60 shaders. These results do not validate Windows GPU
  performance or a selected Iris shaderpack.

The model port preserves the prior unshaded-face behavior through 26.3's `up`
lighting override. Voxy's existing aggregate shading bit cannot reproduce every
custom per-face lighting override or independent ambient-occlusion setting.
