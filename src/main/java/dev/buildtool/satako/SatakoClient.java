package dev.buildtool.satako;

import dev.buildtool.satako.test.TestScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SatakoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Satako.TEST_SCREEN_HANDLER_OBJECT_EXTENDED_SCREEN_HANDLER_TYPE, TestScreen::new);
    }
}
