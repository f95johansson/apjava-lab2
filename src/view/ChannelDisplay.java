/*
 * File: ChannelChooser.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.awt.Image.SCALE_SMOOTH;

class ChannelDisplay extends JPanel {

    public interface OnClick {
        void onClick(String name, int id);
    }
    private static final int IMAGE_BUTTON_SIZE = 58;

    private Image defaultImage;

    private Image imageButtonShadow;
    private Image refreshImage;
    private OnClick onClickListener;

    public ChannelDisplay() throws CreationFailedException {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(new Color(0, 0, 0, 0));

        try {

            imageButtonShadow = ImageIO.read(getClass().getResourceAsStream("/image_button_shadow.png"));
            refreshImage = ImageIO.read(getClass().getResourceAsStream("/refresh.png"));
            defaultImage = new BufferedImage(IMAGE_BUTTON_SIZE, IMAGE_BUTTON_SIZE, BufferedImage.TYPE_INT_ARGB);

        } catch (IOException e) {
            throw new CreationFailedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CreationFailedException("Failed to load images");
        }
    }

    public void addMenu(Image image, String name, int id) {
        ImageButton button;
        if (image == null) {
            drawNameOnImage(name, defaultImage);
            button = new ImageButton(defaultImage, name, id, onClickListener);
        } else {
            button = new ImageButton(image, name, id, onClickListener);
        }
        button.setAlignmentY(JPanel.TOP_ALIGNMENT);
        add(button);
    }

    public void addRefreshButton() {
        add(Box.createHorizontalGlue());
        ImageButton button = new ImageButton(refreshImage, "Refresh", -1, onClickListener);
        button.setAlignmentY(JPanel.TOP_ALIGNMENT);
        add(button);
    }

    private void drawNameOnImage(String name, Image image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, IMAGE_BUTTON_SIZE, IMAGE_BUTTON_SIZE);
        graphics.setColor(Color.BLACK);
        graphics.drawString(name, 10, IMAGE_BUTTON_SIZE/2);
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
            if (component instanceof ImageButton) {
                g.drawImage(imageButtonShadow, component.getX() - 4, -2, null);
            }
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
