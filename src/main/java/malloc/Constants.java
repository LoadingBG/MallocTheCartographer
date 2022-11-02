package malloc;

import java.awt.*;
import java.io.*;

import javax.imageio.*;

public final class Constants {
    private Constants() {}

    public static final Image BORDER;
    public static final Image EMPTY;
    public static final Image EMPTY_RUINS;
    public static final Image FOREST;
    public static final Image FOREST_RUINS;

    static {
        try {
            BORDER = ImageIO.read(Constants.class.getResource("/images/border.png"));
            EMPTY = ImageIO.read(Constants.class.getResource("/images/empty.png"));
            EMPTY_RUINS = ImageIO.read(Constants.class.getResource("/images/ruins.png"));
            FOREST = ImageIO.read(Constants.class.getResource("/images/forest.png"));
            FOREST_RUINS = ImageIO.read(Constants.class.getResource("/images/forest ruins.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
