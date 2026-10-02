package me.cortex.voxy.gametest;

import me.cortex.voxy.client.core.IVoxyRenderSystemHolder;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class WorldSmokeTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.runOnClient(client -> client.options.renderDistance().set(3));
        try (var world = context.worldBuilder().adjustSettings(ui -> {
            ui.setName("Voxy smoke");
            ui.setSeed("42");
            ui.setWorldType(ui.getNormalPresetList().stream()
                    .filter(entry -> entry.preset().is(WorldPresets.FLAT))
                    .findFirst().orElseThrow());
        }).create()) {
            world.getConnection().waitForChunksRender();
            context.waitFor(client -> IVoxyRenderSystemHolder.getNullable() != null);
            context.waitTicks(120);
            context.runOnClient(client -> {
                if (client.level == null || IVoxyRenderSystemHolder.getNullable() == null) {
                    throw new AssertionError("Voxy renderer did not remain active in the test world");
                }
            });
            context.takeScreenshot("voxy-world");
        }
    }
}
