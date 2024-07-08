package dev.buildtool.satako.api;

/**
 * Created on 5/21/17.
 */
public interface Positionable {
    int getElementWidth();

    int getElementHeight();

    int getXPosition();

    void setXPosition(int X);

    int getYPosition();

    void setYPosition(int Y);
}
