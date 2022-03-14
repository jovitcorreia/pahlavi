package com.castanhocorreia.pahlavi;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class Main extends ListenerAdapter {
  public static final String TOKEN = System.getenv("TOKEN");
  public static final String PREFIX = ".";
  public final Set<MessageChannel> rooms = new HashSet<>();

  public static void main(String[] args) throws LoginException {
    DefaultShardManager defaultShardManager = new DefaultShardManager(TOKEN);
    defaultShardManager.setActivity(Activity.competing("chess"));
    defaultShardManager.addEventListener(new Main());
    defaultShardManager.login();
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    Message messageContent = event.getMessage();
    if (messageContent.getContentRaw().contains(Main.PREFIX + "ce")) {
      if (messageContent.getMentionedMembers().isEmpty()
          || messageContent.getMentionedMembers().get(0).getUser().isBot()) {
        event.getChannel().sendMessage("Please mention someone human as opponent").queue();
      } else if (messageContent
          .getMentionedMembers()
          .get(0)
          .getUser()
          .equals(messageContent.getAuthor())) {
        event.getChannel().sendMessage("You cannot challenge yourself... yet").queue();
      } else {
        User whitePlayer;
        User blackPlayer;
        Coin.Side coinSide = Coin.flip();
        if (coinSide.equals(Coin.Side.HEADS)) {
          whitePlayer = event.getAuthor();
          blackPlayer = event.getMessage().getMentionedMembers().get(0).getUser();
        } else {
          whitePlayer = event.getMessage().getMentionedMembers().get(0).getUser();
          blackPlayer = event.getAuthor();
        }
        prepareGame(whitePlayer, blackPlayer, event);
      }
    }
  }

  public void prepareGame(User whitePlayer, User blackPlayer, MessageReceivedEvent event) {
    if (rooms.contains(event.getChannel())) {
      event.getChannel().sendMessage("There is already a game in progress on this channel").queue();
    } else {
      event
          .getChannel()
          .sendMessage(
              String.format(
                  "White player is %s, black player is %s",
                  whitePlayer.getAsMention(), blackPlayer.getAsMention()))
          .queue();
      rooms.add(event.getChannel());
      event
          .getJDA()
          .addEventListener(new Listener(whitePlayer, blackPlayer, event.getChannel(), this));
    }
  }
}
