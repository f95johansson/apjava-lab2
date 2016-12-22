/*
 * File: ChannelChooser.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChannelDisplay extends JPanel {

    private static final int IMAGE_BUTTON_SIZE = 58;

    private Image p2Image;
    private Image imageButtonShadow;

    public ChannelDisplay() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(new Color(0, 0, 0, 0));

        try {

            p2Image = ImageIO.read(getClass().getResourceAsStream("/p2.jpg"));
            imageButtonShadow = ImageIO.read(getClass().getResourceAsStream("/image_button_shadow.png"));

        } catch (IOException e) {
            e.printStackTrace();
            // FIXME empty catch
        }

        add(createButton(p2Image));
        add(createButton(p2Image));
        add(createButton(p2Image));
        add(createButton(p2Image));
        add(createButton(p2Image));
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = IMAGE_BUTTON_SIZE+16;
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.height = IMAGE_BUTTON_SIZE+16;
        return size;
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Component component : getComponents()) {
            if (component instanceof JButton) {
                g.drawImage(imageButtonShadow, component.getX() - 4, -2, null);
            }
        }
    }


    private JButton createButton(Image image) {
        Image imageScaled = image.getScaledInstance(IMAGE_BUTTON_SIZE, IMAGE_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(imageScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }
}
