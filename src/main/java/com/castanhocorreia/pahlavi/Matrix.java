package com.castanhocorreia.pahlavi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Matrix {
  public static char[][] generate(String string) {
    char[] dummy = string.toCharArray();
    char[][] base = new char[8][8];
    int k = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        base[i][j] = dummy[k];
        k++;
      }
      k++;
    }
    return base;
  }
}
