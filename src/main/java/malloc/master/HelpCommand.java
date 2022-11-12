package malloc.master;

import java.awt.Color;
import java.util.*;

import malloc.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public final class HelpCommand extends DiscordCommand {
    @Override
    public CommandData data() {
        return Commands.slash("help", "Help about the game")
            .addSubcommands(
                new SubcommandData("card", "Help for a card")
                    .addOptions(
                        new OptionData(OptionType.STRING, "name", "The name of the card")
                            .setRequired(true)
                            .addChoices(CARD_INFOS.entrySet().stream().map(e -> new Command.Choice(e.getValue().title, e.getKey())).toList())
                    )
            );
    }

    private record CardInfo(String title, Color color, String description, String filename) {}

    private static final Map<String, CardInfo> CARD_INFOS = Map.ofEntries(
        Map.entry("sentinelWood", new CardInfo(
            "Sentinel Wood",
            Color.GREEN,
            """
                Earn **1** reputation for each forest space adjacent to the edge of the map.
                Forest spaces located in the corners of the map score only **1** reputation.
                """,
            "sentinel wood"
        )),
        Map.entry("treetower", new CardInfo(
            "Treetower",
            Color.GREEN,
            """
                Earn **1** reputation for each forest space surrounded on all four sides by filled spaces or the edge of the map.
                Mountain and wasteland spaces are considered filled.
                """,
            "treetower"
        )),
        Map.entry("greenbough", new CardInfo(
            "Greenbough",
            Color.GREEN,
            """
                Earn **1** reputation for each row and column with at least one forest space.
                The same forest space may be scored in a row and a column, thus potentially awarding **2** reputation for a single space.
                """,
            "greenbough"
        )),
        Map.entry("stonesideForest", new CardInfo(
            "Stoneside Forest",
            Color.GREEN,
            """
                Earn **3** reputation for each mountain space connected to another one by a cluster of forest spaces.
                A single mountain space can score at most **3** reputation even if connected to multiple others by different clusters of forest spaces.
                """,
            "stoneside forest"
        )),
        Map.entry("canalLake", new CardInfo(
            "Canal Lake",
            Color.BLUE,
            """
                Earn **1** reputation for each water space adjacent to at least one farm space.
                Earn **1** reputation for each farm space adjacent to at least one water space.
                Each such space can score at most **1** reputation even if adjacent to multiple required spaces.
                """,
            "canal lake"
        )),
        Map.entry("theGoldenGranary", new CardInfo(
            "The Golden Granary",
            Color.BLUE,
            """
                Earn **1** reputation for each water space adjacent to a ruins space.
                Water spaces don't score any reputation if drawn on top of a ruins space.
                Earn **3** reputation for each farm space on top of a ruins space.
                """,
            "the golden granary"
        )),
        Map.entry("magesValley", new CardInfo(
            "Mages Valley",
            Color.BLUE,
            """
                Earn **2** reputation for each water space adjacent to a mountain space.
                Earn **1** reputation for each farm space adjacent to a mountain space.
                """,
            "magesValley"
        )),
        Map.entry("shoresideExpanse", new CardInfo(
            "Shoreside Expanse",
            Color.BLUE,
            """
                Earn **3** reputation for each cluster of farm spaces *not* adjacent to a water space or the edge of the map.
                Earn **3** reputation for each cluster of water spaces *not* adjacent to a farm space or the edge of the map.
                A single square is also considered a cluster and will score **3** reputation if it satisfies the rules above.
                """,
            "shoreside expanse"
        )),
        Map.entry("wildholds", new CardInfo(
            "Wildholds",
            Color.RED,
            """
                Earn **8** reputation for each cluster of 6 or more village spaces.
                """,
            "wildholds"
        )),
        Map.entry("greengoldPlains", new CardInfo(
            "Greengold Plains",
            Color.RED,
            """
                Earn **3** reputation for each cluster of village spaces adjacent to 3 or more different terrain types.
                Reputation is scored for the whole cluster, not for individual spaces in it.
                """,
            "greengold plains"
        )),
        Map.entry("greatCity", new CardInfo(
            "Great City",
            Color.RED,
            """
                Earn **1** reputation for each village space in the largest cluster of village spaces not adjacent to a mountain space.
                """,
            "great city"
        )),
        Map.entry("shieldgate", new CardInfo(
            "Shieldgate",
            Color.RED,
            """
                Earn **2** reputation for each village space in the second largest cluster of village spaces.
                If there are two clusters of the same size, one is considered "the largest" and the other one is scored according to the rules above.
                """,
            "shieldgate"
        )),
        Map.entry("borderlands", new CardInfo(
            "Borderlands",
            Color.GRAY,
            """
                Earn **6** reputation for each complete row or complete column of filled spaces.
                Mountain and wasteland spaces are considered filled.
                """,
            "borderlands"
        )),
        Map.entry("theBrokenRoad", new CardInfo(
            "The Broken Road",
            Color.GRAY,
            """
                Earn **3** reputation for each complete diagonal line of filled spaces that touches the left and bottom edges of the map.
                Mountain and wasteland spaces are considered filled.
                """,
            "the broken road"
        )),
        Map.entry("lostBarony", new CardInfo(
            "Lost Barony",
            Color.GRAY,
            """
                Earn **3** reputation for each space along the edge of the largest square of filled spaces.
                Mountain spaces are considered filled.
                """,
            "lost barony"
        )),
        Map.entry("theCauldrons", new CardInfo(
            "The Cauldrons",
            Color.GRAY,
            """
                Earn **1** reputation for each empty space surrounded on all sides by filled spaces or the edge of the map.
                Mountain and wasteland spaces are considered filled.
                """,
            "the cauldrons"
        ))
    );

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        if ("card".equals(event.getSubcommandName())) {
            var info = CARD_INFOS.get(event.getOption("name").getAsString());
            var embed = new EmbedBuilder()
                .setTitle(info.title)
                .setColor(info.color)
                .setDescription(info.description)
                .setImage("attachment://card.png")
                .build();
            event.getInteraction()
                .replyFiles(Utils.cardFile(info.filename))
                .setEmbeds(embed)
                .setEphemeral(true)
                .queue();
        }
    }
}
