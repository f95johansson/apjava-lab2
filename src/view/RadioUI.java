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
import java.util.List;
import java.util.Map;

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
        SwingUtilities.invokeLater(this::setup);
    }

    private void setup() {
        window = new JFrame();
        window.getContentPane().setBackground(DEFAULT_BACKGROUND);

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

        try {
            channelMenu = new ChannelMenu();
            channelDisplay = new ChannelDisplay();
            episodeInfo = new EpisodeInfo();
            tableau = new Tableau();
        } catch (CreationFailedException e) {
            JOptionPane.showMessageDialog(window, "Application failed to load");
            System.exit(0);
        }

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

    public void setMenus(Collection<String> menus, Map<String, List<MenuInfo>> dropdowns) {
        SwingUtilities.invokeLater(() -> {
            for (String menu : menus) {
                channelMenu.addMenu(menu);
            }
            dropdowns.forEach((inMenu, items) -> {
                for (MenuInfo info : items) {
                    try {
                        channelMenu.addMenuItem(inMenu, info.getName(), info.getID());
                    } catch (IllegalArgumentException e) {
                        // inMenu not a menu, ignore
                    }
                }
            });
        });
    }

    public void setDisplayMenu(Collection<MenuInfo> imageMenus) {
        SwingUtilities.invokeLater(() -> {
            for (MenuInfo menu : imageMenus) {
                try {
                    channelDisplay.addMenu(ImageIO.read(menu.getImage()), menu.getName(), menu.getID());
                } catch (IOException e) {
                    channelDisplay.addMenu(null, menu.getName(), menu.getID());
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
                    episodeInfo.setImage(null);
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
            Color color;
            try {
                String newHex = hexColor; // to be able to change
                if (!newHex.startsWith("#")) {
                    newHex = "#" + newHex;
                }
                color = Color.decode(newHex);
            } catch (NumberFormatException | NullPointerException e) {
                color = DEFAULT_BACKGROUND;
            }
            window.getContentPane().setBackground(color);
            tableau.setBackground(color);
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
