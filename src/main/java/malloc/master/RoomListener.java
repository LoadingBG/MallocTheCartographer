package malloc.master;

import java.awt.*;

import malloc.*;
import malloc.master.logic.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.utils.*;

public final class RoomListener extends ListenerAdapter {
    @Override
    public void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event) {
        if (!event.getComponentId().startsWith("room")) {
            return;
        }
        var parts = event.getComponentId().split(":");
        var room = Malloc.ROOM_MANAGER.execute(parts[1], parts[2], event);

        room.updateHook(event.getMember(), event.getHook());

        switch (parts[2]) {
            case "changeBoard" -> {
                event.getInteraction()
                    .editMessageAttachments(FileUpload.fromData(room.board().toImageBytes(), "board.png"))
                    .setEmbeds(Embeds.settings(room))
                    .setComponents(Embeds.settingsComponents(room))
                    .queue();

                updatePlayerMenus(room);
            }
            case "leave" -> {
                event.getInteraction()
                    .editMessageAttachments()
                    .setEmbeds(Embeds.gameLeft(room.code()))
                    .setComponents()
                    .queue();

                updateSettingsMenu(room);
            }
            case "stop" -> {
                event.editMessageAttachments()
                    .setEmbeds(Embeds.gameStopped(Color.GREEN, room.code()))
                    .setComponents()
                    .queue();

                room.playerHooks()
                    .forEach(hook -> hook.editOriginalAttachments()
                        .setEmbeds(Embeds.gameStopped(Color.RED, room.code()))
                        .setComponents()
                        .queue());
            }
            case "start" -> {
                event.getInteraction()
                    .editMessageAttachments()
                    .setEmbeds(Embeds.gameInitialized())
                    .setComponents()
                    .queue();

                room.playerHooks()
                    .forEach(hook -> hook.editOriginalAttachments()
                        .setEmbeds(Embeds.gameInitialized())
                        .setComponents()
                        .queue());

                Utils.givePlayersTime();

                GameListener.startGame(room.code());
            }
        }
    }

    public void updateSettingsMenu(final Room room) {
        room.hostHook()
            .editOriginalAttachments(FileUpload.fromData(room.board().toImageBytes(), "board.png"))
            .setEmbeds(Embeds.settings(room))
            .setComponents(Embeds.settingsComponents(room))
            .queue();
    }

    public void updatePlayerMenus(final Room room) {
        room.playerHooks()
            .forEach(hook -> hook.editOriginalAttachments(FileUpload.fromData(room.board().toImageBytes(), "board.png"))
                .setEmbeds(Embeds.player(room))
                .setComponents(Embeds.playerComponents(room))
                .queue());
    }
}
