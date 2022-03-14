package com.castanhocorreia.pahlavi;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@EqualsAndHashCode(callSuper = true)
public class Listener extends ListenerAdapter {
  private final User whitePlayer;
  private final User blackPlayer;
  private final MessageChannel channel;
  private final Main parent;
  private int turn = 1;
  private User nextPlayer;
  private LocalDateTime lastMove;
  private Board board = new Board();
  private Image image = new Image();

  public Listener(User whitePlayer, User blackPlayer, MessageChannel channel, Main parent) {
    this.whitePlayer = whitePlayer;
    this.blackPlayer = blackPlayer;
    this.nextPlayer = whitePlayer;
    this.channel = channel;
    this.parent = parent;
    this.lastMove = LocalDateTime.now();
    send();
  }

  public static Move generate(String character, Board board) {
    Move move = null;
    try {
      String start = character.substring(0, 1).toUpperCase() + character.charAt(1);
      String end = character.substring(2, 3).toUpperCase() + character.substring(3);
      move = new Move(Square.fromValue(start), Square.fromValue(end));
      Square[] square = new Square[1];
      square[0] = Square.fromValue(start);
      if (Board.isPromoRank(Side.WHITE, move) && board.hasPiece(Piece.WHITE_PAWN, square)) {
        move = new Move(Square.fromValue(start), Square.fromValue(end), Piece.WHITE_QUEEN);
      } else if (Board.isPromoRank(Side.BLACK, move) && board.hasPiece(Piece.BLACK_PAWN, square)) {
        move = new Move(Square.fromValue(start), Square.fromValue(end), Piece.BLACK_QUEEN);
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return move;
  }

  public void resolve(MessageReceivedEvent event) {
    String message = event.getMessage().getContentDisplay();
    String command = message.substring(3).trim();
    if (message.startsWith(Main.PREFIX + "m")) {
      if (validate(generate(command, board), board)) {
        turn(event, nextPlayer);
        if (nextPlayer.equals(blackPlayer)) turn++;
        nextPlayer = nextPlayer.equals(whitePlayer) ? blackPlayer : whitePlayer;
      } else {
        channel
            .sendMessage(
                String.format(
                    "%s is an invalid move", event.getMessage().getContentDisplay().substring(3)))
            .queue();
      }
    } else if (message.startsWith(Main.PREFIX + "ff")) {
      channel.sendMessage(String.format("%s surrendered", nextPlayer.getAsMention())).queue();
      kill(event.getJDA());
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    LocalDateTime now = LocalDateTime.now();
    if (ChronoUnit.DAYS.between(now, lastMove) <= -2) {
      channel
          .sendMessage(
              String.format(
                  "The game ended in a win for %s due to %s 's timeout",
                  nextPlayer.equals(whitePlayer)
                      ? blackPlayer.getAsMention()
                      : whitePlayer.getAsMention(),
                  nextPlayer.equals(blackPlayer)
                      ? whitePlayer.getAsMention()
                      : blackPlayer.getAsMention()))
          .queue();
      kill(event.getJDA());
    }
    if (event.getChannel() == channel && event.getAuthor().equals(nextPlayer)) {
      resolve(event);
    }
  }

  public void turn(MessageReceivedEvent event, User player) {
    board.doMove(generate(event.getMessage().getContentDisplay().substring(3).trim(), board));
    send();
    verify(event, player);
  }

  public boolean validate(Move move, Board board) {
    MoveList moves = new MoveList();
    try {
      moves.addAll(MoveGenerator.generateLegalMoves(board));
    } catch (MoveGeneratorException moveGeneratorException) {
      moveGeneratorException.printStackTrace();
    }
    return moves.contains(move);
  }

  public void send() {
    image.update(Matrix.generate(board.toString()));
    try {
      channel.sendFile(image.post(image.render()), "image.png").queue();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void verify(MessageReceivedEvent event, User player) {
    if (board.isDraw()) {
      channel.sendMessage("The game resulted in draw").queue();
      kill(event.getJDA());
    } else if (board.isMated()) {
      channel
          .sendMessage(
              String.format("%s performed a checkmate and won the game", player.getAsMention()))
          .queue();
      kill(event.getJDA());
    } else if (board.isKingAttacked()) {
      channel.sendMessage(String.format("%s performed a check", player.getAsMention())).queue();
    }
  }

  public void kill(JDA jda) {
    parent.getRooms().remove(channel);
    jda.removeEventListener(this);
  }
}
