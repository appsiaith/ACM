package view.components;

import util.ProgramSettings;
import util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 23/11/13
 * Time: 22:52
 */
public abstract class ImageButton extends JButton implements ActionListener {

    public ImageButton(String fileName, int size) {
        super();
        addActionListener(this);
        try {
            Image image = SwingUtils.resize(SwingUtils.class.getClass().getResource(ProgramSettings.IMG_ROOT + fileName), size, size);
            setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(size, size));
        setBorderPainted(false);
        setContentAreaFilled(false);
//        setFocusPainted(false);
        setOpaque(true);
    }

    public ImageButton(String fileName, String text, int size, boolean vertical) {
        super();
        addActionListener(this);
        try {
            Image image = SwingUtils.resize(SwingUtils.class.getClass().getResource(ProgramSettings.IMG_ROOT + fileName), size, size);
            setText(text);
            setIcon(new ImageIcon(image));
            setFont(new Font("Tahoma", Font.PLAIN, 12));
            if (vertical) {
                setVerticalTextPosition(SwingConstants.BOTTOM);
                setHorizontalTextPosition(SwingConstants.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBorderPainted(false);
        setContentAreaFilled(false);
//        setFocusPainted(false);
        setOpaque(true);
        setBackground(Color.white);
    }

    public ImageButton(String fileName, String text, int size, boolean vertical, Dimension dimension) {
        super();
        addActionListener(this);
        try {
            Image image = SwingUtils.resize(SwingUtils.class.getClass().getResource(ProgramSettings.IMG_ROOT + fileName), size, size);
            setText(text);
            setIcon(new ImageIcon(image));
            setFont(new Font("Tahoma", Font.PLAIN, 12));
            if (vertical) {
                setVerticalTextPosition(SwingConstants.BOTTOM);
                setHorizontalTextPosition(SwingConstants.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(dimension);
        setBorderPainted(false);
        setContentAreaFilled(false);
//        setFocusPainted(false);
        setOpaque(true);
        setBackground(Color.white);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}
