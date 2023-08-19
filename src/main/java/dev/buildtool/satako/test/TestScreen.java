package dev.buildtool.satako.test;

import dev.buildtool.satako.IntegerColor;
import dev.buildtool.satako.gui.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TestScreen extends InventoryScreen<TestScreenHandler> {
    public TestScreen(TestScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text, true);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(new BetterButton(centerX, 0, Text.literal("Button")));
        RadioButton radioButton1 = new RadioButton(centerX, 20, Text.literal("Radio button 1"));
        addDrawableChild(radioButton1);
        RadioButton radioButton2 = new RadioButton(radioButton1.getX() + radioButton1.getWidth(), 20, Text.literal("Radio button 2"));
        addDrawableChild(radioButton2);
        new ButtonGroup(radioButton1, radioButton2);
        ArrayList<Object> elements = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Label label = new Label(0, 20 * i, Text.literal("#" + i));
            addDrawableChild(label);
            elements.add(label);
        }
        ButtonWidget buttonWidget1 = new ButtonWidget(20, 120, 100, 20, Text.literal("Vanilla button 1"), button -> {
        });
        ButtonWidget buttonWidget2 = new BetterButton(20, 140, 100, 20, Text.literal("Vanilla button 2"), button -> {
        });
        addDrawableChild(buttonWidget1);
        addDrawableChild(buttonWidget2);
        elements.add(buttonWidget1);
        elements.add(buttonWidget2);
        SwitchButton switchButton = new SwitchButton(0, 200, Text.literal("true"), Text.literal("false"), true, button -> {
        });
        addDrawableChild(switchButton);
        elements.add(switchButton);
        addDrawableChild(new Label(this.leftPosition + backgroundWidth + 10, this.topPosition, Text.literal("Clickable 1"), this, button -> client.player.sendMessage(Text.literal("Clicked first label"), false)));
        addDrawableChild(new Label(this.leftPosition + backgroundWidth + 10, this.topPosition + 20, Text.literal("Clickable 2"), this, button -> client.player.sendMessage(Text.literal("Clicked second label"), false)));
        ScrollArea scrollArea = new ScrollArea(3, 3, leftPosition - 10, height, Text.literal("List"), new IntegerColor(0x22F8A55E), elements);
        addDrawableChild(scrollArea);
        DropDownButton dropDownButton = new DropDownButton(leftPosition, topPosition + this.backgroundHeight + 20, this, Text.literal(""));
        LinkedHashMap<Text, ButtonWidget.PressAction> linkedHashMap = new LinkedHashMap<>(3);
        linkedHashMap.put(Text.literal("1.0"), button -> {
            client.player.sendMessage(Text.literal("Pressed first button"), false);
            dropDownButton.setMessage(button.getMessage());
            dropDownButton.onPress();
        });
        linkedHashMap.put(Text.literal("2.0"), button -> {
            client.player.sendMessage(Text.literal("Pressed second button"), false);
            dropDownButton.setMessage(button.getMessage());
            dropDownButton.onPress();
        });
        linkedHashMap.put(Text.literal("3.0"), button -> {
            client.player.sendMessage(Text.literal("Pressed third button"), false);
            dropDownButton.setMessage(button.getMessage());
            dropDownButton.onPress();
        });
        dropDownButton.setChoices(linkedHashMap, 0);
        addDrawableChild(dropDownButton);
    }
}
