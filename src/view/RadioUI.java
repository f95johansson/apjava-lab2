/*
 * File: RadioUI.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package view;

import javax.swing.*;
import java.awt.*;
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
        SwingUtilities.invokeLater(this::setup);
    }

    private void setup() {
        window = new JFrame();
        window.getContentPane().setBackground(DEFAULT_BACKGROUND);

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

        channelMenu = new ChannelMenu();
        channelDisplay = new ChannelDisplay();
        episodeInfo = new EpisodeInfo();
        tableau = new Tableau(0, 2);

        window.setJMenuBar(channelMenu.menuBar);
        window.add(channelDisplay);
        window.add(episodeInfo);
        window.add(tableau);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(503, 600));
        window.setPreferredSize(new Dimension(800, 685));

        window.setLocationRelativeTo(null);

        window.setVisible(true);




        channelMenu.addMenu("Lokal kanal");
        channelMenu.addMenu("Minoritet och språk");
        channelMenu.addMenu("Fler kanaler");
        channelMenu.addMenu("Extrakanaler");

        channelMenu.addMenuItem("Lokal kanal", "P4 Västebotten", 164);
    }

    public void setMenus(Collection<Pair<String, Collection<Pair<String, Integer>>>> menusWithDropDown) {
        for (Pair<String, Collection<Pair<String, Integer>>> menu : menusWithDropDown) {

            channelMenu.addMenu(menu.getFirst());

            for (Pair<String, Integer> menuItem : menu.getSecond()) {
                channelMenu.addMenuItem(menu.getFirst(), menuItem.getFirst(), menuItem.getSecond());
            }
        }
    }

    public void setDisplayMenu() {

    }

    public void setTableauContent(Collection<Triplet<String, String, Integer>> episodesInfo) {
        for (Triplet<String, String, Integer> info : episodesInfo) {
            tableau.addEpisode(info.getFirst(), info.getSecond(), info.getThird());
        }
    }


    public void setColor(String hexColor) {
        SwingUtilities.invokeLater(() -> {
            String newHex = hexColor; // to be able to change
            if (!newHex.startsWith("#")) {
                newHex = "#" + newHex;
            }

            try {
                window.getContentPane().setBackground(Color.decode(newHex));
            } catch (NumberFormatException e) {
                window.getContentPane().setBackground(DEFAULT_BACKGROUND);
            }
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
        });
    }
}
