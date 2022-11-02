package malloc.discord;

import java.awt.Color;
import java.util.*;

import malloc.game.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.interactions.components.*;
import net.dv8tion.jda.api.interactions.components.buttons.*;

public final class StartCommand extends DiscordCommand {
    private boolean inProgress = false;
    private Board board;
    private boolean forestGoalsDisabled = false;
    private boolean waterGoalsDisabled = false;
    private boolean villageGoalsDisabled = false;
    private boolean generalGoalsDisabled = false;
    private Goal goalA;
    private Goal goalB;
    private Goal goalC;
    private Goal goalD;
    private String boardName = null;
    private String goalAName = null;
    private String goalBName = null;
    private String goalCName = null;
    private String goalDName = null;

    private void reset() {
        inProgress = false;
        board = null;
        forestGoalsDisabled = false;
        waterGoalsDisabled = false;
        villageGoalsDisabled = false;
        generalGoalsDisabled = false;
        goalA = null;
        goalB = null;
        goalC = null;
        goalD = null;
        boardName = null;
        goalAName = null;
        goalBName = null;
        goalCName = null;
        goalDName = null;
    }

    @Override
    public CommandData data() {
        return Commands.slash("start", "Starts a game");
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        if (inProgress) {
            event.getInteraction().reply("A game is already getting set up").setEphemeral(true).queue();
            return;
        }
        if (GameHandler.isInProgress()) {
            event.getInteraction().reply("A game has already started").setEphemeral(true).queue();
            return;
        }

        event.getInteraction()
            .replyEmbeds(createBuilderEmbed())
            .addActionRow(
                Button.secondary("board:plain", "Plain"),
                Button.secondary("board:ravineMiddle", "Ravine in middle"),
                Button.danger("setup:stop", "Stop")
            )
            .setEphemeral(true)
            .queue();
        inProgress = true;
    }

    @Override
    public void onButtonInteraction(final ButtonInteractionEvent event) {
        if (event.getComponentId().equals("setup:stop")) {
            sendEndMessage(event);
            reset();
            return;
        }

        if (event.getComponentId().startsWith("board:")) {
            board = switch (event.getComponentId()) {
                case "board:plain" -> {
                    boardName = "Plain";
                    yield Board.plain();
                }
                case "board:ravineMiddle" -> {
                    boardName = "With a ravine in the middle";
                    yield Board.ravineMiddle();
                }
                default -> throw new UnsupportedOperationException("Unreachable");
            };

            sendGoalMessage(1, event);
            return;
        }

        if (event.getComponentId().startsWith("goalD:")) {
            setupGoal(event.getComponentId());

            event.editMessageEmbeds(createBuilderEmbed()).setComponents().queue();

            GameHandler.start(event, new Bot(board, goalA, goalB, goalC, goalD));
            reset();
            return;
        }

        var goalNum = 0;
        if (event.getComponentId().startsWith("goalA:")) {
            goalNum = 2;
        } else if (event.getComponentId().startsWith("goalB:")) {
            goalNum = 3;
        } else if (event.getComponentId().startsWith("goalC:")) {
            goalNum = 4;
        }

        setupGoal(event.getComponentId());
        sendGoalMessage(goalNum, event);
    }

    private void sendGoalMessage(final int goal, final ButtonInteractionEvent event) {
        var prefix = switch (goal) {
            case 1 -> "goalA";
            case 2 -> "goalB";
            case 3 -> "goalC";
            case 4 -> "goalD";
            default -> throw new UnsupportedOperationException("Unreachable");
        };
//        var title = switch (goal) {
//            case 1 -> "Goal A:";
//            case 2 -> "Goal B:";
//            case 3 -> "Goal C:";
//            case 4 -> "Goal D:";
//            default -> throw new UnsupportedOperationException("Unreachable");
//        };

        event.editMessageEmbeds(createBuilderEmbed())
            .setComponents(
                ActionRow.of(
                    Button.success(prefix + ":sentinelWood", "Sentinel Wood").withDisabled(forestGoalsDisabled),
                    Button.success(prefix + ":treetower", "Treetower").withDisabled(forestGoalsDisabled),
                    Button.success(prefix + ":greenbough", "Greenbough").withDisabled(forestGoalsDisabled),
                    Button.success(prefix + ":stonesideForest", "Stoneside Forest").withDisabled(forestGoalsDisabled)
                ),
                ActionRow.of(
                    Button.primary(prefix + ":canalLake", "Canal Lake").withDisabled(waterGoalsDisabled),
                    Button.primary(prefix + ":theGoldenGranary", "The Golden Granary").withDisabled(waterGoalsDisabled),
                    Button.primary(prefix + ":magesValley", "Mages Valley").withDisabled(waterGoalsDisabled),
                    Button.primary(prefix + ":shoresideExpanse", "Shoreside Expanse").withDisabled(waterGoalsDisabled)
                ),
                ActionRow.of(
                    Button.danger(prefix + ":wildholds", "Wildholds").withDisabled(villageGoalsDisabled),
                    Button.danger(prefix + ":greengoldPlains", "Greengold Plains").withDisabled(villageGoalsDisabled),
                    Button.danger(prefix + ":greatCity", "Great City").withDisabled(villageGoalsDisabled),
                    Button.danger(prefix + ":shieldgate", "Shieldgate").withDisabled(villageGoalsDisabled)
                ),
                ActionRow.of(
                    Button.secondary(prefix + ":borderlands", "Borderlands").withDisabled(generalGoalsDisabled),
                    Button.secondary(prefix + ":theBrokenRoad", "The Broken Road").withDisabled(generalGoalsDisabled),
                    Button.secondary(prefix + ":lostBarony", "Lost Barony").withDisabled(generalGoalsDisabled),
                    Button.secondary(prefix + ":theCauldrons", "The Cauldrons").withDisabled(generalGoalsDisabled)
                ),
                ActionRow.of(
                    Button.danger("setup:stop", "Stop")
                )
            )
            .queue();
    }

    private void setupGoal(final String buttonId) {
        var ids = buttonId.split(":");
        var goal = findGoal(ids[1]);

        switch (ids[0]) {
            case "goalA" -> {
                goalA = goal.getKey();
                goalAName = goal.getValue();
            }
            case "goalB" -> {
                goalB = goal.getKey();
                goalBName = goal.getValue();
            }
            case "goalC" -> {
                goalC = goal.getKey();
                goalCName = goal.getValue();
            }
            case "goalD" -> {
                goalD = goal.getKey();
                goalDName = goal.getValue();
            }
            default -> throw new UnsupportedOperationException("Unreachable");
        }
    }

    private Map.Entry<Goal, String> findGoal(final String goalId) {
        switch (goalId) {
            case "sentinelWood", "treetower", "greenbough", "stonesideForest" -> forestGoalsDisabled = true;
            case "canalLake", "theGoldenGranary", "magesValley", "shoresideExpanse" -> waterGoalsDisabled = true;
            case "wildholds", "greengoldPlains", "greatCity", "shieldgate" -> villageGoalsDisabled = true;
            case "borderlands", "theBrokenRoad", "lostBarony", "theCauldrons" -> generalGoalsDisabled = true;
            default -> throw new UnsupportedOperationException("Unreachable");
        }

        return switch (goalId) {
            case "sentinelWood" -> Map.entry(Goal.SENTINEL_WOOD, "Sentinel Wood");
            case "treetower" -> Map.entry(Goal.TREETOWER, "Treetower");
            case "greenbough" -> Map.entry(Goal.GREENBOUGH, "Greenbough");
            case "stonesideForest" -> Map.entry(Goal.STONESIDE_FOREST, "Stoneside Forest");
            case "canalLake" -> Map.entry(Goal.CANAL_LAKE, "Canal Lake");
            case "theGoldenGranary" -> Map.entry(Goal.THE_GOLDEN_GRANARY, "The Golden Granary");
            case "magesValley" -> Map.entry(Goal.MAGES_VALLEY, "Mages Valley");
            case "shoresideExpanse" -> Map.entry(Goal.SHORESIDE_EXPANSE, "Shoreside Expanse");
            case "wildholds" -> Map.entry(Goal.WILDHOLDS, "Wildholds");
            case "greengoldPlains" -> Map.entry(Goal.GREENGOLD_PLAINS, "Greengold Plains");
            case "greatCity" -> Map.entry(Goal.GREAT_CITY, "Great City");
            case "shieldgate" -> Map.entry(Goal.SHIELDGATE, "Shieldgate");
            case "borderlands" -> Map.entry(Goal.BORDERLANDS, "Borderlands");
            case "theBrokenRoad" -> Map.entry(Goal.THE_BROKEN_ROAD, "The Broken Road");
            case "lostBarony" -> Map.entry(Goal.LOST_BARONY, "Lost Barony");
            case "theCauldrons" -> Map.entry(Goal.THE_CAULDRONS, "The Cauldrons");
            default -> throw new UnsupportedOperationException("Unreachable");
        };
    }

    private void sendEndMessage(final ButtonInteractionEvent event) {
        event.editMessageEmbeds(
            new EmbedBuilder()
                .setTitle("Game Initialization Stopped")
                .setDescription("To start a new game, use `/start`")
                .setColor(Color.RED)
                .build()
        ).setComponents().queue();
    }

    private MessageEmbed createBuilderEmbed() {
        var builder = new EmbedBuilder()
            .setTitle("Game Setup")
            .setColor(Color.CYAN);
        var optionsBuilder = new StringBuilder();
        if (boardName != null) {
            optionsBuilder.append("**Board**: ").append(boardName);
        }
        if (goalAName != null) {
            optionsBuilder.append("\n**Goal A**: ").append(goalAName);
        }
        if (goalBName != null) {
            optionsBuilder.append("\n**Goal B**: ").append(goalBName);
        }
        if (goalCName != null) {
            optionsBuilder.append("\n**Goal C**: ").append(goalCName);
        }
        if (goalDName != null) {
            optionsBuilder.append("\n**Goal D**: ").append(goalDName);
        }

        if (!optionsBuilder.isEmpty()) {
            builder.addField("Options", optionsBuilder.toString(), false);
        }

        if (boardName == null) {
            return builder
                .addField("Board", "???", false)
                .build();
        } else if (goalAName == null) {
            return builder
                .addField("Goal A", "???", false)
                .build();
        } else if (goalBName == null) {
            return builder
                .addField("Goal B", "???", false)
                .build();
        } else if (goalCName == null) {
            return builder
                .addField("Goal C", "???", false)
                .build();
        } else if (goalDName == null) {
            return builder
                .addField("Goal D", "???", false)
                .build();
        } else {
            return builder
                .addField("Setup done!", "Game is starting.", false)
                .build();
        }
    }
}
