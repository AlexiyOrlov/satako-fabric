package dev.buildtool.satako.api;

public interface Scrollable {

    /**
     * @param direction -1 or 1
     */
    void scroll(int direction, boolean vertically);

    void setScrollable(boolean vertically, boolean doScroll);

    /**
     * @param amount pixels
     */
    void setScrollingAmount(boolean vertically, int amount);
}
