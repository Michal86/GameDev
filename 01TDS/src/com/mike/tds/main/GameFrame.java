package com.mike.tds.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame {

    protected JFrame frame;
    private final int SCALE = 1;

    public GameFrame(int width, int height, String title, Game game){

        this.frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width * SCALE, height* SCALE));
        frame.setMaximumSize(new Dimension(width * SCALE, height* SCALE));
        frame.setMinimumSize(new Dimension(width * SCALE, height* SCALE));


        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.add(game);
        frame.pack();

    }

}
