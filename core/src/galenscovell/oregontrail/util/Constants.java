package galenscovell.oregontrail.util;

public class Constants {
    private Constants() {}

    // Lighting masks
    public static final short BIT_LIGHT = 1;
    public static final short BIT_WALL = 2;
    public static final short BIT_GROUP = 5;

    // Custom screen dimension units
    public static final int SCREEN_X = 200;
    public static final int SCREEN_Y = 120;

    // Exact pixel dimensions
    public static final int EXACT_X = 800;
    public static final int EXACT_Y = 480;

    // Map dimensions
    public static final int MAPWIDTH = 24;  // 576px @ 24px
    public static final int MAPHEIGHT = 16; // 384px @ 24px
    public static final int TILESIZE = 24;
    public static final int MAPBORDERWIDTH = (MAPWIDTH * TILESIZE) + 22;   // 598px
    public static final int MAPBORDERHEIGHT = (MAPHEIGHT * TILESIZE) + 20; // 404px
}
