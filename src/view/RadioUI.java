/*
 * File: RadioUI.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Facade
 */
public class RadioUI {

    public interface EpisodeSelect {
        void onEpisodeSelect(String name, int index, int id);
    }

    public interface ChannelSelect {
        void onChannelSelect(String name, int id);
    }

    private static final Color DEFAULT_BACKGROUND = new Color(189, 196, 200);

    private JFrame window;
    private ChannelMenu channelMenu;
    private ChannelDisplay channelDisplay;
    private EpisodeInfo episodeInfo;
    private Tableau tableau;

    public RadioUI() {
        //System.setProperty("apple.laf.useScreenMenuBar", "true");
        //System.setProperty("com.apple.mrj.application.apple.menu.about.name", "RadioInfo");

        SwingUtilities.invokeLater(this::setup);
    }

    private void setup() {
        window = new JFrame();
        window.getContentPane().setBackground(DEFAULT_BACKGROUND);

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

        channelMenu = new ChannelMenu();
        channelDisplay = new ChannelDisplay();
        episodeInfo = new EpisodeInfo();
        tableau = new Tableau();

        window.setJMenuBar(channelMenu);
        window.add(channelDisplay);
        window.add(episodeInfo);
        window.add(tableau);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(503, 600));
        window.setPreferredSize(new Dimension(800, 685));

        window.setLocationRelativeTo(null);

        window.setVisible(true);
    }

    public void setMenus(Collection<String> menus, Collection<Triplet<String, String, Integer>> dropdowns) {
        SwingUtilities.invokeLater(() -> {
            for (String menu : menus) {
                channelMenu.addMenu(menu);
            }
            for (Triplet<String, String, Integer> menuItem : dropdowns) {
                channelMenu.addMenuItem(menuItem.getFirst(), menuItem.getSecond(), menuItem.getThird());
            }
        });
    }

    public void setDisplayMenu(Collection<Triplet<InputStream, String, Integer>> imageMenus) {
        SwingUtilities.invokeLater(() -> {
            for (Triplet<InputStream, String, Integer> menu : imageMenus) {
                try {
                    channelDisplay.addMenu(ImageIO.read(menu.getFirst()), menu.getSecond(), menu.getThird());
                } catch (IOException e) {
                    e.printStackTrace(); // FIXME empty catch
                }
            }
        });
    }

    public void setTableauContent(Collection<TableauRow> episodes) {
        SwingUtilities.invokeLater(() -> {
            tableau.clear();

            for (TableauRow episode : episodes) {
                tableau.addEpisode(episode);
            }
        });
    }

    public void setEpisodeContent(String title, String subtitle, String text, InputStream image) {
        SwingUtilities.invokeLater(() -> {
            episodeInfo.setTitle(title);
            episodeInfo.setSubtitle(subtitle);
            episodeInfo.setText(text);
            if (image != null) {
                try {
                    episodeInfo.setImage(ImageIO.read(image));
                } catch (IOException e) {
                    e.printStackTrace(); // FIXME empty catch
                }
            } else {
                episodeInfo.setImage(null);
            }
            episodeInfo.refresh();
        });
    }

    public void setTitle(String title) {
        SwingUtilities.invokeLater(() -> {
            window.setTitle(title);
        });
    }

    public void setColor(String hexColor) {
        SwingUtilities.invokeLater(() -> {
            try {
                String newHex = hexColor; // to be able to change
                if (!newHex.startsWith("#")) {
                    newHex = "#" + newHex;
                }
                window.getContentPane().setBackground(Color.decode(newHex));
            } catch (NumberFormatException | NullPointerException e) {
                window.getContentPane().setBackground(DEFAULT_BACKGROUND);
            }
        });
    }

    public void clear() {
        SwingUtilities.invokeLater(() -> {
            episodeInfo.clear();
            tableau.clear();
        });
    }

    public void setEpisodeSelected(int index) {
        SwingUtilities.invokeLater(() -> {
            tableau.setSelected(index);
        });
    }

    public void setChannelSelected(int index) {
        SwingUtilities.invokeLater(() -> {
            ((JButton) channelDisplay.getComponents()[index]).doClick();
        });
    }

    public void setEpisodeSelectListener(EpisodeSelect listener) {
        SwingUtilities.invokeLater(() -> {
            tableau.setItemSelectListener(listener::onEpisodeSelect);
        });
    }

    public void setChannelSelectListener(ChannelSelect listener) {
        SwingUtilities.invokeLater(() -> {
            channelMenu.setOnClickListener(listener::onChannelSelect);
            channelDisplay.setOnClickListener(listener::onChannelSelect);
        });
    }
}
