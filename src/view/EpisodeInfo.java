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

    public EpisodeInfo() throws CreationFailedException {
        setLayout(null);

        label = new JLabel(text);
        label.setLocation(MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE+MARGIN, MARGIN+TOP_SHADOW_HEIGHT+MARGIN/2);
        add(label, BorderLayout.EAST);

        try {
            backgroundLeft = ImageIO.read(getClass().getResourceAsStream("/info_background_left.png"));
            backgroundMiddle = ImageIO.read(getClass().getResourceAsStream("/info_background_middle.png"));
            backgroundRight = ImageIO.read(getClass().getResourceAsStream("/info_background_right.png"));
        } catch (IOException e) {
            throw new CreationFailedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CreationFailedException("Failed to load images");
        }
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? "" : subtitle;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
    }

    public void setImage(BufferedImage episodeImage) {
        this.episodeImage = episodeImage;
    }

    public void clear() {
        title = "";
        subtitle = "";
        text = "";
        episodeImage = null;
        refresh();
    }

    public void refresh() {
        repaint();
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = 152;
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.height = 152;
        return size;
    }


    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

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
        g.drawImage(backgroundMiddle, MARGIN+leftWidth, MARGIN, getWidth()-(MARGIN*2+leftWidth + rightWidth), middleHeight, null);
        g.drawImage(backgroundRight, getWidth() - (MARGIN+rightWidth), MARGIN, null);

        if (episodeImage != null) {
            g.drawImage(episodeImage, MARGIN+SIDE_SHADOW_WIDTH, MARGIN+TOP_SHADOW_HEIGHT, EPISODE_IMAGE_SIZE, EPISODE_IMAGE_SIZE, null);
        }
    }

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
                (getWidth()-MARGIN*2-SIDE_SHADOW_WIDTH*2-EPISODE_IMAGE_SIZE-100) +
                "px\">" +
                text +
                "</p></body></html>";
    }

    private void setSmoothRendering(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
