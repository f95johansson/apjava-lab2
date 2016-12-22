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

    private final int MARGIN = 16;
    private final int EPISODE_IMAGE_SIZE = 110;
    private final int SIDE_SHADOW_WIDTH = 4;
    private final int TOP_SHADOW_HEIGHT = 2;
    private final int BOTTOM_SHADOW_HEIGHT = 6;

    private Image backgroundLeft;
    private Image backgroundMiddle;
    private Image backgroundRight;

    private String title = "Title";
    private String subtitle = "Subtitle";
    private String text = "Hello asdflkasdflk öasjksf  lkfsjk fsd jlsdfs ödfads fjk ldsf klöfsakl öfsdkl ödsf kljödsfa kljösdffkl öasdkl öfsal ösdafdfls öklösfd klöfsd lködfsa klösfa fklsöa j";
    private BufferedImage episodeImage;

    private String centerTextPart1 = "<html><body><h1>hello</h1><p style=\"width:";
    private String centerTextPart2 = "px\">";
    private String centerTextPart3 = "</p></body></html>";

    private JLabel label;

    public EpisodeInfo() {
        //setLayout(null);

        label = new JLabel();
        label.setLocation(MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE, MARGIN+TOP_SHADOW_HEIGHT);

        try {
            backgroundLeft = ImageIO.read(getClass().getResourceAsStream("/info_background_left.png"));
            backgroundMiddle = ImageIO.read(getClass().getResourceAsStream("/info_background_middle.png"));
            backgroundRight = ImageIO.read(getClass().getResourceAsStream("/info_background_right.png"));

            episodeImage = ImageIO.read(getClass().getResourceAsStream("/ekot.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            // FIXME empty catch
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(BufferedImage episodeImage) {
        this.episodeImage = episodeImage;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        return new Dimension((int) size.getWidth(), 152);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        return new Dimension((int) size.getWidth(), 152);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setSmoothRendering(g);

        int leftWidth = backgroundLeft.getWidth(null);
        int rightWidth = backgroundRight.getWidth(null);
        int middleHeight = backgroundMiddle.getHeight(null);

        g.drawImage(backgroundLeft, MARGIN, MARGIN, null);
        g.drawImage(backgroundMiddle, MARGIN+leftWidth, MARGIN, getWidth()-(MARGIN*2+leftWidth + rightWidth), middleHeight, null);
        g.drawImage(backgroundRight, getWidth() - (MARGIN+rightWidth), MARGIN, null);

        g.drawImage(episodeImage, MARGIN+SIDE_SHADOW_WIDTH, MARGIN+TOP_SHADOW_HEIGHT, EPISODE_IMAGE_SIZE, EPISODE_IMAGE_SIZE, null);


        /*
        g.setFont(RobotoFont.getFont(18f));
        g.drawString(title, MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE, MARGIN+TOP_SHADOW_HEIGHT+24);
        g.setFont(RobotoFont.getFont(13f));
        g.drawString(subtitle, MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE, MARGIN+TOP_SHADOW_HEIGHT+40);
        g.setFont(RobotoFont.getFont(13f));
        g.drawString(text, MARGIN+SIDE_SHADOW_WIDTH+EPISODE_IMAGE_SIZE, MARGIN+TOP_SHADOW_HEIGHT+50);
        */

        label.setText(formatCenteredText(text));
        label.setSize(label.getPreferredSize());
        label.paint(g);
    }

    private String formatCenteredText(String text) {
        return centerTextPart1+(getWidth()-100)+centerTextPart2+text+centerTextPart3;
    }

    private void setSmoothRendering(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
