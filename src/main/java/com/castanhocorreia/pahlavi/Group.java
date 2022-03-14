package com.castanhocorreia.pahlavi;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

@Data
public class Group {
  private int boardSize;
  private int squareSize;
  private Vector top = new Vector(45, 45);
  private Vector bot = new Vector(405, 405);
  private Comp board = new Comp(Man.BOARD.image);
  private Comp whitePawn = scale(Man.WHITE_PAWN);
  private Comp whiteRook = scale(Man.WHITE_ROOK);
  private Comp whiteKnight = scale(Man.WHITE_KNIGHT);
  private Comp whiteBishop = scale(Man.WHITE_BISHOP);
  private Comp whiteQueen = scale(Man.WHITE_QUEEN);
  private Comp whiteKing = scale(Man.WHITE_KING);
  private Comp blackPawn = scale(Man.BLACK_PAWN);
  private Comp blackRook = scale(Man.BLACK_ROOK);
  private Comp blackKnight = scale(Man.BLACK_KNIGHT);
  private Comp blackBishop = scale(Man.BLACK_BISHOP);
  private Comp blackQueen = scale(Man.BLACK_QUEEN);
  private Comp blackKing = scale(Man.BLACK_KING);

  public Group() {
    boardSize = bot.getX() - top.getX();
    squareSize = boardSize / 8;
  }

  private Comp scale(Man man) {
    int scale = 45;
    Comp comp = new Comp(man.image);
    BufferedImage scaled;
    if (comp.getImage() != null) {
      scaled = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = scaled.createGraphics();
      graphics.drawImage(comp.getImage(), 0, 0, scale, scale, null);
      graphics.dispose();
      comp.setImage(scaled);
    }
    return comp;
  }
}
