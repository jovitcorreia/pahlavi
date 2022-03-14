package com.castanhocorreia.pahlavi;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class Comp extends Component {
  private transient BufferedImage image;

  public Comp(String path) {
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  @Override
  public Dimension getSize() {
    return image == null
        ? new Dimension(100, 100)
        : new Dimension(image.getWidth(), image.getHeight());
  }

  @Override
  public int getWidth() {
    return image == null ? 100 : image.getWidth();
  }

  @Override
  public int getHeight() {
    return image == null ? 100 : image.getHeight();
  }
}
