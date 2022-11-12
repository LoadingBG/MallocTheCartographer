package malloc;

import java.awt.*;
import java.io.*;

import javax.imageio.*;

import net.dv8tion.jda.api.utils.*;

public final class Utils {
    private Utils() {}

    public static final int TIMEOUT = 5000;

    public static void givePlayersTime() {
        try {
            Thread.sleep(TIMEOUT);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static final int IMAGE_SIZE = 108;
    public static final Image BORDER;
    public static final Image EMPTY;
    public static final Image EMPTY_RUINS;
    public static final Image FOREST;
    public static final Image FOREST_RUINS;
    public static final Image VILLAGE;
    public static final Image VILLAGE_RUINS;
    public static final Image FARM;
    public static final Image FARM_RUINS;
    public static final Image WATER;
    public static final Image WATER_RUINS;
    public static final Image MONSTER;
    public static final Image MONSTER_RUINS;
    public static final Image MOUNTAIN;
    public static final Image MOUNTAIN_COIN;

    static {
        try {
            BORDER = ImageIO.read(Utils.class.getResource("/images/border.png"));
            EMPTY = ImageIO.read(Utils.class.getResource("/images/empty.png"));
            EMPTY_RUINS = ImageIO.read(Utils.class.getResource("/images/ruins.png"));
            FOREST = ImageIO.read(Utils.class.getResource("/images/forest simple.png"));
            FOREST_RUINS = ImageIO.read(Utils.class.getResource("/images/forest simple ruins.png"));
            VILLAGE = ImageIO.read(Utils.class.getResource("/images/village.png"));
            VILLAGE_RUINS = ImageIO.read(Utils.class.getResource("/images/village ruins.png"));
            FARM = ImageIO.read(Utils.class.getResource("/images/farm.png"));
            FARM_RUINS = ImageIO.read(Utils.class.getResource("/images/farm ruins.png"));
            WATER = ImageIO.read(Utils.class.getResource("/images/water.png"));
            WATER_RUINS = ImageIO.read(Utils.class.getResource("/images/water ruins.png"));
            MONSTER = ImageIO.read(Utils.class.getResource("/images/monster.png"));
            MONSTER_RUINS = ImageIO.read(Utils.class.getResource("/images/monster ruins.png"));
            MOUNTAIN = ImageIO.read(Utils.class.getResource("/images/mountain.png"));
            MOUNTAIN_COIN = ImageIO.read(Utils.class.getResource("/images/mountain coin.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileUpload cardFile(final String card) {
        return FileUpload.fromData(Utils.class.getResourceAsStream("/images/cards/" + card + ".png"), "card.png");
    }
}
