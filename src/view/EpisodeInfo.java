/*
 * File: EpisodeInfo.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * EpisodeInfo displays information about an episode on a rectangle
 */
public class EpisodeInfo extends JComponent {

    private final int MARGIN = 12;
    private final int EPISODE_IMAGE_SIZE = 110;
    private final int SIDE_SHADOW_WIDTH = 4;
    private final int TOP_SHADOW_HEIGHT = 2;
    private final int BOTTOM_SHADOW_HEIGHT = 6;

    private Image backgroundLeft;
    private Image backgroundMiddle;
    private Image backgroundRight;

    private String title = "";
    private String subtitle = "";
    private String text = "";
    private BufferedImage episodeImage;

    private JLabel label;

    /**
     * Loads images and components needed for displaying
     * @throws CreationFailedException If images failed to load
     */
    public EpisodeInfo() throws CreationFailedException {
        setLayout(null);

        label = new JLabel(text);
        label.setLocation(MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE+MARGIN,
                          MARGIN+TOP_SHADOW_HEIGHT+MARGIN/2);
        add(label, BorderLayout.EAST);

        try {
            backgroundLeft = ImageIO.read(getClass()
                    .getResourceAsStream("/info_background_left.png"));
            backgroundMiddle = ImageIO.read(getClass()
                    .getResourceAsStream("/info_background_middle.png"));
            backgroundRight = ImageIO.read(getClass()
                    .getResourceAsStream("/info_background_right.png"));

        } catch (IOException e) {
            throw new CreationFailedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CreationFailedException("Failed to load images");
        }
    }

    /**
     * Sets the title
     * @param title If null, and empty string will be used instead
     */
    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    /**
     * Sets the subtitle
     * @param subtitle If null or empty string, nothing will be shown
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? "" : subtitle;
    }

    /**
     * Sets the text
     * @param text If null, and empty string will be used instead
     */
    public void setText(String text) {
        this.text = text == null ? "" : text;
    }

    /**
     * Sets the image, should be square
     * @param episodeImage If null, nothing will be shown
     */
    public void setImage(BufferedImage episodeImage) {
        this.episodeImage = episodeImage;
    }

    /**
     * Clears the information about the episode (title, subtitle, text, image)
     */
    public void clear() {
        title = "";
        subtitle = "";
        text = "";
        episodeImage = null;
        refresh();
    }

    /**
     * Repaints the component
     */
    public void refresh() {
        repaint();
    }

    /**
     * Used to set the maximum size of component
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = 152;
        return size;
    }

    /**
     * Used to set the minimum size of component
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.height = 152;
        return size;
    }

    /**
     * Used to set the preferred size of component
     */
    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    /**
     * {@inheritDoc}
     *
     * Paints the extra graphics around the information, plus the episode image
     */
    @Override
    protected void paintComponent(Graphics g) {
        label.setText(formatCenteredText());
        label.setSize(label.getPreferredSize());

        super.paintComponent(g);
        setSmoothRendering(g);

        int leftWidth = backgroundLeft.getWidth(null);
        int rightWidth = backgroundRight.getWidth(null);
        int middleHeight = backgroundMiddle.getHeight(null);

        g.drawImage(backgroundLeft, MARGIN, MARGIN, null);

        // Middle background image will be stretched across component
        g.drawImage(backgroundMiddle,
                    MARGIN+leftWidth,
                    MARGIN,
                    getWidth()-(MARGIN*2+leftWidth + rightWidth),
                    middleHeight, null);

        g.drawImage(backgroundRight,
                    getWidth() - (MARGIN+rightWidth),
                    MARGIN, null);

        if (episodeImage != null) {
            g.drawImage(episodeImage,
                        MARGIN+SIDE_SHADOW_WIDTH,
                        MARGIN+TOP_SHADOW_HEIGHT,
                        EPISODE_IMAGE_SIZE,
                        EPISODE_IMAGE_SIZE, null);
        }
    }

    /**
     * Format information about episode with appropriate html tags
     * @return A well formatted string
     */
    private String formatCenteredText() {
        String formattedSubtitle;
        if (subtitle == null || subtitle.equals("")) {
            formattedSubtitle = "";
        } else {
            formattedSubtitle = "</p><p><b>" +
                    subtitle +
                    "</b>";
        }

        return  "<html><body><p style=\"font-size:14px;\">" +
                title +
                formattedSubtitle +
                "</p><p style=\"width:" +
                (getWidth()-MARGIN*2-SIDE_SHADOW_WIDTH*2-EPISODE_IMAGE_SIZE) +
                "px\">" +
                text +
                "</p></body></html>";
    }

    /**
     * Turn on smooth rendering for image and text
     * @param g Graphics to turn on smooth rendering on
     */
    private void setSmoothRendering(Graphics g) {
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
