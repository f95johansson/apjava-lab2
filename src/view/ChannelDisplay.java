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

import static java.awt.Image.SCALE_SMOOTH;

public class ChannelDisplay extends JPanel {

    public interface OnClick {
        void onClick(String name, int id);
    }

    private static final int IMAGE_BUTTON_SIZE = 58;

    private Image p2Image;
    private Image imageButtonShadow;
    private OnClick onClickListener;

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
    }

    public void addMenu(Image image, String name, int id) {
        add(new ImageButton(image, name, id, onClickListener));
    }

    public void setOnClickListener(OnClick listener) {
        onClickListener = listener;
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
            g.drawImage(imageButtonShadow, component.getX() - 4, -2, null);
        }
    }

    private static class ImageButton extends JButton {

        public ImageButton(Image image, String name, int id, OnClick listener) {
            super(new ImageIcon(
                    image.getScaledInstance(IMAGE_BUTTON_SIZE,
                                            IMAGE_BUTTON_SIZE,
                                            SCALE_SMOOTH)));

            setBorder(BorderFactory.createEmptyBorder());

            addActionListener(e -> listener.onClick(name, id));
        }
    }
}
