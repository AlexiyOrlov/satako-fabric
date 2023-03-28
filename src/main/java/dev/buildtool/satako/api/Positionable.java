package dev.buildtool.satako.api;

/**
 * Created on 5/21/17.
 */
public interface Positionable {
    int getElementWidth();

    int getElementHeight();

    int getX();

    void setX(int X);

    int getY();

    void setY(int Y);
}
