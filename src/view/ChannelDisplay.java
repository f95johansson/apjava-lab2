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


/**
 * ChannelDisplay acts as a menu but using square image instead of a menu bar.
 * Each menu item is clickable and there's a special menu item which
 * is used as a refresh button.
 */
class ChannelDisplay extends JPanel {

    /**
     * A listener for whenever a menu item is clicked
     */
    public interface OnClick {
        /**
         * On result the name and id of the clicked menu item is provided
         * @param name Name of menu item
         * @param id Id corresponding to menu item. Will be -1 if refresh
         *           button is clicked, but that value is only useful when
         *           setting both click and refresh listener to same listener.
         *           A better idea would be to use two different listeners
         *           and then the -1 information is not needed to know.
         */
        void onClick(String name, int id);
    }
    private static final int IMAGE_BUTTON_SIZE = 58;

    private Image defaultImage;

    private Image imageButtonShadow;
    private Image refreshImage;
    private OnClick onClickListener;
    private OnClick onRefreshListener;

    /**
     * Loads the images from resource which is required for this component
     * Also sets appropriate styling
     * @throws CreationFailedException When images couldn't be loaded
     */
    public ChannelDisplay() throws CreationFailedException {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(new Color(0, 0, 0, 0));

        try {

            imageButtonShadow = ImageIO.read(getClass().getResourceAsStream(
                    "/image_button_shadow.png"));
            refreshImage = ImageIO.read(getClass().getResourceAsStream(
                    "/refresh.png"));
            defaultImage = new BufferedImage(IMAGE_BUTTON_SIZE,
                                             IMAGE_BUTTON_SIZE,
                                             BufferedImage.TYPE_INT_ARGB);

        } catch (IOException e) {
            throw new CreationFailedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CreationFailedException("Failed to load images");
        }
    }

    /**
     * Add a menu item, in form of an image. If image can't load, a
     * replacement image will be provided using the name
     * @param image Image to show as menu item
     * @param name Name of menu item
     * @param id Id of menu item
     */
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

    /**
     * Adds a refresh button after the menu items. Should be called after
     * all the menu items have been set
     */
    public void addRefreshButton() {
        add(Box.createHorizontalGlue());
        ImageButton button =
                new ImageButton(refreshImage, "", -1, onRefreshListener);
        button.setAlignmentY(JPanel.TOP_ALIGNMENT);
        add(button);
    }

    /**
     * Draw a name on top of the provided image
     * @param name Name to draw
     * @param image Image provided, should be blank as otherwise information
     *              might be overwritten
     */
    private void drawNameOnImage(String name, Image image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, IMAGE_BUTTON_SIZE, IMAGE_BUTTON_SIZE);
        graphics.setColor(Color.BLACK);
        graphics.drawString(name, 10, IMAGE_BUTTON_SIZE/2);
    }

    /**
     * Sets a click listener for whenever a menu item is clicked
     * @param listener Listener for menu item clicks
     */
    public void setOnClickListener(OnClick listener) {
        onClickListener = listener;
    }

    /**
     * Sets a click listener for whenever refresh button is clicked
     * @param listener Listener for refresh button clicks
     */
    public void setOnRefreshListener(OnClick listener) {
        onRefreshListener = listener;
    }

    /**
     * {@inheritDoc}
     *
     * Used to set a fixed size
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = IMAGE_BUTTON_SIZE+16;
        return size;
    }

    /**
     * {@inheritDoc}
     *
     * Used to set a fixed size
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.height = IMAGE_BUTTON_SIZE+16;
        return size;
    }

    /**
     * {@inheritDoc}
     *
     * Used to set a fixed size
     */
    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    /**
     * {@inheritDoc}
     *
     * Paint the graphics around the buttons
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Component component : getComponents()) {
            if (component instanceof ImageButton) {
                g.drawImage(imageButtonShadow, component.getX() - 4, -2, null);
            }
        }
    }

    /**
     * Helper class to make buttons remember their name and id
     * for the listener
     */
    private static class ImageButton extends JButton {

        /**
         * Creates a button which will remember the name and id
         * to be used when listener is trigger, i.e. when button is clicked
         * @param image Image of the button
         * @param name Name of button
         * @param id Id of button
         * @param listener Click listener
         */
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
