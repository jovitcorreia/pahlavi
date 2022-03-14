package com.castanhocorreia.pahlavi;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class Coin {
  private static final SecureRandom secureRandom = new SecureRandom();

  public static Side flip() {
    int result = secureRandom.nextInt(2);
    return result == 0 ? Side.HEADS : Side.TAILS;
  }

  public enum Side {
    HEADS,
    TAILS
  }
}
