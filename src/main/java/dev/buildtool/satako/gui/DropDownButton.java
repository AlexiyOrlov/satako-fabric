package dev.buildtool.satako.gui;

import dev.buildtool.satako.api.Hideable;
import dev.buildtool.satako.api.Positionable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class DropDownButton extends BetterButton {
    private final Screen parent;
    private HashMap<Text, RadioButton> choices;
    private boolean open;
    private final List<Element> overlappingElements = new ArrayList<>();

    public DropDownButton(int x, int y, Screen parent, Text text) {
        super(x, y, text);
        this.parent = parent;
    }

    @Override
    public void onPress() {
        open = !open;
        if (open) {
            if (overlappingElements.isEmpty()) {
                choices.values().forEach(radioButton -> {
                    radioButton.setHidden(false);
                    parent.children().forEach(clickableWidget -> {
                        if (!choices.containsValue(clickableWidget) && clickableWidget != this) {
                            if (clickableWidget instanceof Positionable positionable) {
                                if (clickableWidget instanceof Hideable hideable) {
                                    for (int i = 0; i < positionable.getElementWidth(); i++) {
                                        for (int j = 0; j < positionable.getElementHeight(); j++) {
                                            if (isInsideArea(positionable.getX() + i, positionable.getY() + j, radioButton.x, radioButton.x + radioButton.getElementWidth(), radioButton.y, radioButton.y + radioButton.getElementHeight())) {
                                                hideable.setHidden(true);
                                                overlappingElements.add(clickableWidget);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else if (clickableWidget instanceof ClickableWidget abstractWidget) {
                                for (int i = 0; i < abstractWidget.getWidth(); i++) {
                                    for (int j = 0; j < abstractWidget.getHeight(); j++) {
                                        if (isInsideArea(abstractWidget.x + i, abstractWidget.y + j, radioButton.x, radioButton.x + radioButton.getElementWidth(), radioButton.y, radioButton.y + radioButton.getElementHeight())) {
                                            abstractWidget.visible = false;
                                            overlappingElements.add(clickableWidget);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });
                });
            } else {
                choices.values().forEach(radioButton -> radioButton.setHidden(false));
                overlappingElements.forEach(guiEventListener -> {
                    if (guiEventListener instanceof Positionable) {
                        if (guiEventListener instanceof Hideable hideable) {
                            hideable.setHidden(true);
                        }
                    } else if (guiEventListener instanceof ClickableWidget abstractWidget) {
                        abstractWidget.visible = false;
                    }
                });
            }
        } else {
            choices.values().forEach(radioButton -> radioButton.setHidden(true));
            overlappingElements.forEach(guiEventListener -> {
                if (guiEventListener instanceof Positionable) {
                    if (guiEventListener instanceof Hideable hideable)
                        hideable.setHidden(false);
                } else if (guiEventListener instanceof ClickableWidget a) {
                    a.visible = true;
                }
            });
        }
    }

    private boolean isInsideArea(int x, int y, int x1, int x2, int y1, int y2) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (open)
            drawStringWithShadow(matrices, textRenderer, " :", x + width, y + height / 2 - 4, 0xffffffff);
        else
            drawStringWithShadow(matrices, textRenderer, " V", x + width, y + height / 2 - 4, 0xffffffff);
    }

    /**
     * @param map            text to action pairs. Actions must change this button's message and call {@link DropDownButton#onPress()} method
     * @param selectedButton initially selected button index
     */
    public void setChoices(LinkedHashMap<Text, PressAction> map, int selectedButton) {
        int offset = 1;
        ButtonGroup buttonGroup = new ButtonGroup();
        this.choices = new HashMap<>(map.size());
        for (Map.Entry<Text, PressAction> entry : map.entrySet()) {
            Text component = entry.getKey();
            PressAction onPress1 = entry.getValue();
            RadioButton radioButton = new RadioButton(x, y + 20 * offset++, component, onPress1);
            radioButton.setHidden(true);
            radioButton.selected = selectedButton + 1 - offset == -1;
            if (radioButton.selected)
                setMessage(radioButton.getMessage());
            this.parent.addSelectableChild(radioButton);
            this.choices.put(component, radioButton);
            buttonGroup.add(radioButton);
            if (radioButton.getElementWidth() > getElementWidth())
                this.width = radioButton.getElementWidth();
        }
        buttonGroup.connect();
    }
}
