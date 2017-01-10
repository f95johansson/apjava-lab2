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
 * This class acts as the main class for the whole ui/view. It uses the
 * Facade design pattern to ensure that only this class is needed for
 * interaction with the view package. It will funnel all listener into
 * the appropriate usage.
 * All methods to this class is non-blocking.
 */
public class RadioUI {

    /**
     * Listener for when an episode has been selected
     */
    public interface EpisodeSelect {
        /**
         * Returns information about the selected episode in
         * form of a method call to listener
         * @param name Title of episode
         * @param index Index of episode
         * @param id Id for episode
         */
        void onEpisodeSelect(String name, int index, int id);
    }

    /**
     * Listener for when a channel has been selected
     */
    public interface ChannelSelect {
        /**
         * Returns information about the selected channel in
         * form of a method call to listener
         * @param name Name of channel
         * @param id Id of channel
         */
        void onChannelSelect(String name, int id);
    }

    /**
     * Listener for when a refresh event has happen
     */
    public interface Refresh {
        /**
         * Called when user manually clicks a a refresh button
         */
        void onRefresh();
    }

    private static final Color DEFAULT_BACKGROUND = new Color(189, 196, 200);

    private JFrame window;
    private ChannelMenu channelMenu;
    private ChannelDisplay channelDisplay;
    private EpisodeInfo episodeInfo;
    private Tableau tableau;

    /**
     * This will start the GUI on the Swing thread.
     */
    public RadioUI() {
        SwingUtilities.invokeLater(this::setup);
    }

    /**
     * Setup all the components of the GUI, in the window
     */
    private void setup() {
        window = new JFrame();
        window.getContentPane().setBackground(DEFAULT_BACKGROUND);

        window.setLayout(
                new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

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

    /**
     * Set the menus which should be shown in the GUI.
     * @param menus Names of menus
     * @param dropdowns Dropdowns containing information about each
     *                  menu item, and corresponding to a menu name
     */
    public void setMenus(Collection<String> menus, Map<String,
                         List<MenuInfo>> dropdowns) {

        SwingUtilities.invokeLater(() -> {
            for (String menu : menus) {
                channelMenu.addMenu(menu);
            }
            dropdowns.forEach((inMenu, items) -> {
                for (MenuInfo info : items) {
                    try {
                        channelMenu.addMenuItem(inMenu,
                                                info.getName(),
                                                info.getID());
                    } catch (IllegalArgumentException e) {
                        // inMenu not a menu, ignore
                    }
                }
            });
        });
    }

    /**
     * Set spacial menu items which will be shown using an image
     * @param imageMenus Menu items to show
     */
    public void setDisplayMenu(Collection<MenuInfo> imageMenus) {
        SwingUtilities.invokeLater(() -> {
            for (MenuInfo menu : imageMenus) {
                try {
                    channelDisplay.addMenu(ImageIO.read(menu.getImage()),
                                           menu.getName(),
                                           menu.getID());
                } catch (IOException e) {
                    channelDisplay.addMenu(null, menu.getName(), menu.getID());
                }
            }
            channelDisplay.addRefreshButton();

        });
    }

    /**
     * Set content of the Tableau
     * @param episodes Each episode, which will be shown as a row
     */
    public void setTableauContent(Collection<TableauRow> episodes) {
        SwingUtilities.invokeLater(() -> {
            tableau.clear();

            for (TableauRow episode : episodes) {
                tableau.addEpisode(episode);
            }
        });
    }

    /**
     * Set all information about an episode. All parameters are allowed
     * to be NULL if that piece of information should not be shown
     * @param title Title of episode. Can be NULL
     * @param subtitle Subtitle of episode. Can be NULL
     * @param text A description of the episode. Can be NULL
     * @param image An image for the episode. Can be NULL
     */
    public void setEpisodeContent(String title, String subtitle,
                                  String text, InputStream image) {
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

    /**
     * Set the title of the window
     * @param title Title
     */
    public void setTitle(String title) {
        SwingUtilities.invokeLater(() -> {
            window.setTitle(title);
        });
    }

    /**
     * Set the background color of the window. Should be a hexadecimal
     * representation of color, e.g. #8BF2A2. Hash symbol can be left out.
     * @param hexColor Hex representation of color
     */
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

    /**
     * Clear information in about both episode and tableau
     */
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            episodeInfo.clear();
            tableau.clear();
        });
    }

    /**
     * Set which episode should be selected
     * @param index Index of the episode. Ignores values to large
     */
    public void setEpisodeSelected(int index) {
        SwingUtilities.invokeLater(() -> {
            tableau.setSelected(index);
        });
    }

    /**
     * Sets a listener for whenever the user selects an episode.
     * Only one listener at a time can be set.
     * @param listener Episode select listener. Null to remove existing listener
     */
    public void setEpisodeSelectListener(EpisodeSelect listener) {
        SwingUtilities.invokeLater(() -> {
            tableau.setItemSelectListener(listener::onEpisodeSelect);
        });
    }

    /**
     * Sets a listener for whenever the user selects a channel.
     * Only one listener at a time can be set.
     * @param listener Channel select listener. Null to remove existing listener
     */
    public void setChannelSelectListener(ChannelSelect listener) {
        SwingUtilities.invokeLater(() -> {
            channelMenu.setOnClickListener(listener::onChannelSelect);
            channelDisplay.setOnClickListener(listener::onChannelSelect);
        });
    }

    /**
     * Sets a listener for whenever the user manually refreshes.
     * Only one listener at a time can be set.
     * @param listener Refresh listener. Null to remove existing listener
     */
    public void setOnRefreshListener(Refresh listener) {
        SwingUtilities.invokeLater(() -> {
            channelDisplay.setOnRefreshListener((n, s) -> listener.onRefresh());
        });
    }
}
