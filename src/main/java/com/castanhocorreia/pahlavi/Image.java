package com.castanhocorreia.pahlavi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Image {
  private final Group group;
  private final char[][] board;

  public Image() {
    group = new Group();
    board = new char[8][8];
  }

  public BufferedImage render() {
    int x;
    int y;
    BufferedImage image =
        new BufferedImage(
            group.getBoard().getHeight(), group.getBoard().getWidth(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics2D = image.createGraphics();
    graphics2D.drawImage(group.getBoard().getImage(), 0, 0, null);
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Comp comp = find(board[j][i]);
        if (comp != null) {
          BufferedImage draw = comp.getImage();
          x =
              group.getSquareSize() * i
                  + group.getTop().getX()
                  + (group.getSquareSize() - draw.getWidth()) / 2;
          y =
              group.getSquareSize() * j
                  + group.getTop().getY()
                  + (group.getSquareSize() - draw.getHeight()) / 2;
          graphics2D.drawImage(draw, x, y, null);
        }
      }
    }
    graphics2D.dispose();
    return image;
  }

  private Comp find(char piece) {
    switch (piece) {
      case ' ':
        return null;
      case 'r':
        return group.getBlackRook();
      case 'n':
        return group.getBlackKnight();
      case 'b':
        return group.getBlackBishop();
      case 'q':
        return group.getBlackQueen();
      case 'k':
        return group.getBlackKing();
      case 'p':
        return group.getBlackPawn();
      case 'R':
        return group.getWhiteRook();
      case 'N':
        return group.getWhiteKnight();
      case 'B':
        return group.getWhiteBishop();
      case 'Q':
        return group.getWhiteQueen();
      case 'K':
        return group.getWhiteKing();
      case 'P':
        return group.getWhitePawn();
      default:
        return null;
    }
  }

  public void update(char[][] updated) {
    for (int i = 0; i < 8; i++) {
      System.arraycopy(updated[i], 0, board[i], 0, 8);
    }
  }

  public byte[] post(BufferedImage image) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", byteArrayOutputStream);
      byteArrayOutputStream.flush();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    byte[] imageInByte = byteArrayOutputStream.toByteArray();
    byteArrayOutputStream.close();
    return imageInByte;
  }
}
