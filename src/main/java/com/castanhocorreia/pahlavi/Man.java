package com.castanhocorreia.pahlavi;

public enum Man {
  BOARD("/assets/board.png"),
  WHITE_PAWN("/assets/white_pawn.png"),
  WHITE_ROOK("/assets/white_rook.png"),
  WHITE_KNIGHT("/assets/white_knight.png"),
  WHITE_BISHOP("/assets/white_bishop.png"),
  WHITE_QUEEN("/assets/white_queen.png"),
  WHITE_KING("/assets/white_king.png"),
  BLACK_PAWN("/assets/black_pawn.png"),
  BLACK_ROOK("/assets/black_rook.png"),
  BLACK_KNIGHT("/assets/black_knight.png"),
  BLACK_BISHOP("/assets/black_bishop.png"),
  BLACK_QUEEN("/assets/black_queen.png"),
  BLACK_KING("/assets/black_king.png");

  final String image;

  Man(String image) {
    this.image = image;
  }
}
