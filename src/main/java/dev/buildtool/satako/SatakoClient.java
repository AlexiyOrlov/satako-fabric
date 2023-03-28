package dev.buildtool.satako;

import dev.buildtool.satako.test.TestScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SatakoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Satako.testScreenHandlerType, TestScreen::new);
    }
}
