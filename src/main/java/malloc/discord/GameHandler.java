package malloc.discord;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import malloc.*;
import malloc.game.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.utils.*;

public final class GameHandler extends ListenerAdapter {
    private static Bot bot;

    public static void reset() {
        bot = null;
    }

    public static void start(final ButtonInteractionEvent event, final Bot bot) {
        GameHandler.bot = bot;
        event.getHook().deleteOriginal().queue();
        event.getChannel()
            .sendMessage("Coins: " + bot.board().coins() + "/10")
//            .addFiles(createMapImage(bot.board()))
            .queue();
    }

    public static boolean isInProgress() {
        return bot != null;
    }

//    private static FileUpload createMapImage(final Board board) {
//        final var CELL_SIZE = 110;
//
//        var image = new BufferedImage(board.width() * CELL_SIZE, board.height() * CELL_SIZE, BufferedImage.TYPE_INT_RGB);
//        var graphics = image.createGraphics();
//
//        for (var i = 0; i < board.height(); ++i) {
//            for (var j = 0; j < board.width(); ++j) {
//                var tileImage = switch (board.get(i, j)) {
//                    case Cell.Empty c -> c.hasRuins ? Constants.EMPTY_RUINS : Constants.EMPTY;
//                    case Cell.Forest c -> c.hasRuins ? Constants.FOREST_RUINS : Constants.FOREST;
//                    default -> Constants.BORDER;
//                };
//                graphics.drawImage(tileImage, j * CELL_SIZE, i * CELL_SIZE, null);
//            }
//        }
//
//        var bytes = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(image, "png", bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return FileUpload.fromData(bytes.toByteArray(), "board.png");
//    }
}
