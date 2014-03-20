package view.panel;

import util.Appearance;
import util.Config;
import util.SwingUtils;
import view.components.ImageButton;
import view.components.SwingDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 24/01/14
 * Time: 22:55
 */
public class AppearancePanel extends JPanel {

    JPanel optionPanel;
    DisplayPanel displayPanel;

    ColorButtonPanel backColorButtonPanel;
    ColorButtonPanel cellColorButtonPanel;
    ColorButtonPanel mainFontColorButtonPanel;
    ColorButtonPanel subFontColorButtonPanel;
    ColorButtonPanel headerFontColorButtonPanel;
    ColorButtonPanel headerCellColorButtonPanel;
    ImageFilePanel backImagePanel;
    ImageFilePanel headerImagePanel;
    SizePanel mainFontSizePanel;
    SizePanel subFontSizePanel;
    SizePanel headerHeightPanel;
    SizePanel cellHeightPanel;
    SizePanel headerFontSizePanel;
    StringField titleField;
    StringField headerField;

    Appearance appearance;
    String hint = "1,0,1,1,1,1";

    JComboBox tableComboBox;
    JComboBox styleComboBox;

    public AppearancePanel() {
        super(new GridBagLayout());
        if (Appearance.appearances.containsKey("MainMenu"))
            appearance = Appearance.appearances.get("MainMenu");

        JPanel background = new JPanel(new GridBagLayout());
        JPanel topPanel = new JPanel(new GridBagLayout());

        optionPanel = new JPanel(new GridBagLayout());

        tableComboBox = new JComboBox(Appearance.appearances.keySet().toArray());
        tableComboBox.setSelectedItem("MainMenu");
        tableComboBox.setMaximumRowCount(Appearance.appearances.keySet().size());
        tableComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String tableName = (String) comboBox.getSelectedItem();
                appearance = Appearance.appearances.get(tableName);
                styleComboBox.setSelectedItem(null);
                backColorButtonPanel.refresh();
                cellColorButtonPanel.refresh();
                mainFontColorButtonPanel.refresh();
                subFontColorButtonPanel.refresh();
                headerFontColorButtonPanel.refresh();
                headerCellColorButtonPanel.refresh();
                backImagePanel.refresh();
                headerImagePanel.refresh();
                mainFontSizePanel.refresh();
                subFontSizePanel.refresh();
                headerHeightPanel.refresh();
                cellHeightPanel.refresh();
                headerFontSizePanel.refresh();
                titleField.refresh();
                headerField.refresh();
                displayPanel.repaint();
            }
        });
        topPanel.add(new JLabel("Active Table:"), SwingUtils.makeConstraints("001100"));
        topPanel.add(tableComboBox, SwingUtils.makeConstraints("101110"));

        topPanel.add(new JLabel("Table Title:"), SwingUtils.makeConstraints("011100"));
        titleField = new StringField("title");
        topPanel.add(titleField, SwingUtils.makeConstraints("111110"));

        topPanel.add(new JLabel("Table Header:"), SwingUtils.makeConstraints("021101"));
        headerField = new StringField("header", 2);
        topPanel.add(headerField, SwingUtils.makeConstraints("121111"));

        styleComboBox = new JComboBox(Appearance.appearances.keySet().toArray());
        styleComboBox.setSelectedItem(null);
        styleComboBox.setMaximumRowCount(Appearance.appearances.keySet().size());
        styleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (styleComboBox.getSelectedItem() == null) return;
                String tableName = (String) tableComboBox.getSelectedItem();
                Appearance copiedAppearance = new Appearance(tableName);
                Appearance sourceAppearance = Appearance.appearances.get(styleComboBox.getSelectedItem());
                copiedAppearance.title = appearance.title;
                copiedAppearance.header = appearance.header;
                copiedAppearance.copyStyleFrom(sourceAppearance);
                Appearance.appearances.put(tableName, copiedAppearance);
                appearance = Appearance.appearances.get(tableName);

                backColorButtonPanel.refresh();
                cellColorButtonPanel.refresh();
                mainFontColorButtonPanel.refresh();
                subFontColorButtonPanel.refresh();
                headerFontColorButtonPanel.refresh();
                headerCellColorButtonPanel.refresh();
                backImagePanel.refresh();
                headerImagePanel.refresh();
                mainFontSizePanel.refresh();
                subFontSizePanel.refresh();
                headerHeightPanel.refresh();
                cellHeightPanel.refresh();
                headerFontSizePanel.refresh();
                titleField.refresh();
                headerField.refresh();

                displayPanel.repaint();
            }
        });
        topPanel.add(new JLabel("Copy Style From:"), SwingUtils.makeConstraints("031100"));
        topPanel.add(styleComboBox, SwingUtils.makeConstraints("131110"));
        background.add(topPanel, SwingUtils.makeConstraints("002111"));

//        optionPanel.add(new JLabel("Color values are: RED,GREEN,BLUE,AlPHA (0-255)"), SwingUtils.makeConstraints("002110"));

        addComponent(optionPanel, new JLabel("Background Color:"));
        backColorButtonPanel = new ColorButtonPanel("back");
        addComponent(optionPanel, backColorButtonPanel);

        addComponent(optionPanel, new JLabel("Cell Color:"));
        cellColorButtonPanel = new ColorButtonPanel("cell");
        addComponent(optionPanel, cellColorButtonPanel);

        addComponent(optionPanel, new JLabel("Main Font Color:"));
        mainFontColorButtonPanel = new ColorButtonPanel("mainFont");
        addComponent(optionPanel, mainFontColorButtonPanel);

        addComponent(optionPanel, new JLabel("Sub Font Color:"));
        subFontColorButtonPanel = new ColorButtonPanel("subFont");
        addComponent(optionPanel, subFontColorButtonPanel);

        addComponent(optionPanel, new JLabel("Header Font Color:"));
        headerFontColorButtonPanel = new ColorButtonPanel("headerFont");
        addComponent(optionPanel, headerFontColorButtonPanel);

        addComponent(optionPanel, new JLabel("Header Cell Color:"));
        headerCellColorButtonPanel = new ColorButtonPanel("headerCell");
        addComponent(optionPanel, headerCellColorButtonPanel);

        addComponent(optionPanel, new JLabel("Header Image:"));
        headerImagePanel = new ImageFilePanel("header");
        addComponent(optionPanel, headerImagePanel);

        addComponent(optionPanel, new JLabel("Background Image:"));
        backImagePanel = new ImageFilePanel("back");
        addComponent(optionPanel, backImagePanel);

        addComponent(optionPanel, new JLabel("Main Font Size:"));
        mainFontSizePanel = new SizePanel("mainFont");
        addComponent(optionPanel, mainFontSizePanel);

        addComponent(optionPanel, new JLabel("Sub Font Size:"));
        subFontSizePanel = new SizePanel("subFont");
        addComponent(optionPanel, subFontSizePanel);

        addComponent(optionPanel, new JLabel("Cell Height:"));
        cellHeightPanel = new SizePanel("cell");
        addComponent(optionPanel, cellHeightPanel);

        addComponent(optionPanel, new JLabel("Header Height:"));
        headerHeightPanel = new SizePanel("headerHeight");
        addComponent(optionPanel, headerHeightPanel);

        addComponent(optionPanel, new JLabel("Header Font Size:"));
        headerFontSizePanel = new SizePanel("headerFont");
        addComponent(optionPanel, headerFontSizePanel);

        displayPanel = new DisplayPanel();
        displayPanel.setPreferredSize(new Dimension(320, 480));
        displayPanel.setMinimumSize(new Dimension(320, 480));
        background.add(optionPanel, SwingUtils.makeConstraints("041211"));
        JLabel preview = new JLabel("Preview");
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setBackground(Color.darkGray);
        preview.setForeground(Color.white);
        preview.setOpaque(true);
        background.add(preview, SwingUtils.makeConstraints("141101"));
        background.add(displayPanel, SwingUtils.makeConstraints("151100"));
        add(background, SwingUtils.makeConstraints("001111"));
    }

    private void addComponent(JPanel panel, JComponent component) {
        hint = SwingUtils.setLayout(hint, 2);
        panel.add(component, SwingUtils.makeConstraints(hint));
    }

    private String getRGB(Color color) {
        StringBuffer buffer = new StringBuffer();
        if (color.getRed() < 10) buffer.append("0" + "0" + color.getRed());
        else if (color.getRed() < 100) buffer.append("0" + color.getRed());
        else buffer.append(color.getRed());
        buffer.append(",");
        if (color.getGreen() < 10) buffer.append("0" + "0" + color.getGreen());
        else if (color.getGreen() < 100) buffer.append("0" + color.getGreen());
        else buffer.append(color.getGreen());
        buffer.append(",");
        if (color.getBlue() < 10) buffer.append("0" + "0" + color.getBlue());
        else if (color.getBlue() < 100) buffer.append("0" + color.getBlue());
        else buffer.append(color.getBlue());
        buffer.append(",");
        if (color.getAlpha() < 10) buffer.append("0" + "0" + color.getAlpha());
        else if (color.getAlpha() < 100) buffer.append("0" + color.getAlpha());
        else buffer.append(color.getAlpha());
        return buffer.toString();
    }

    private String getRGB(Color color, int colorType) {
        switch (colorType) {
            case 0:
                return String.valueOf(color.getRed());
            case 1:
                return String.valueOf(color.getGreen());
            case 2:
                return String.valueOf(color.getBlue());
            case 3:
                return String.valueOf(color.getAlpha());
            default:
                return null;
        }
    }

    class DisplayPanel extends JPanel {
        int screenHeight = 480;
        int screenWidth = 320;
        int headerOffset = 30;
        int innerPadding = 4;
        int leftPadding = 10;
        Image backgroundImage;
        Image headerImage;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(hints);

//            System.out.println("repainting");
            if (appearance.backImage.isEmpty()) {
                g2.setColor(appearance.backColor);
                g2.fillRect(0, 0, screenWidth, screenHeight);
            } else {
                try {
                    backgroundImage = SwingUtils.resize(Config.getImageFolder() + appearance.backImage, screenWidth, screenHeight);
                    g2.drawImage(backgroundImage, 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (appearance.headerImage.isEmpty()) {
                g2.setColor(appearance.headerCellColor);
                g2.fillRect(0, headerOffset, screenWidth, appearance.headerHeight);
                g2.setColor(appearance.headerFontColor);
                Font font = new Font("Helvetica", Font.PLAIN, appearance.headerFontSize);
                g2.setFont(font);
                String[] headerSplit = appearance.header.split("\n");
                for (int i = 0; i < headerSplit.length; i++) {
                    g2.drawString(headerSplit[i], leftPadding, headerOffset + g2.getFontMetrics(font).getHeight() * (i + 1));
                }
            } else {
                try {
                    headerImage = SwingUtils.resize(Config.getImageFolder() + appearance.headerImage, screenWidth, appearance.headerHeight);
                    g2.drawImage(headerImage, 0, headerOffset, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int currentY = headerOffset + appearance.headerHeight + headerOffset;
            if (appearance.tableName.equals("MainMenu")) {
                currentY = paintCell(g2, currentY, util.Menu.MAIN_1, util.Menu.MAIN_SUB_1);
                currentY = paintCell(g2, currentY, util.Menu.MAIN_2, util.Menu.MAIN_SUB_2);
                currentY = paintCell(g2, currentY, util.Menu.MAIN_3, util.Menu.MAIN_SUB_3);
                currentY = paintCell(g2, currentY, util.Menu.MAIN_4, util.Menu.MAIN_SUB_4);
                currentY = paintCell(g2, currentY, util.Menu.MAIN_5, util.Menu.MAIN_SUB_5);
            } else if (appearance.tableName.equals("UnitChoice")) {
                currentY = paintCell(g2, currentY, util.Menu.UNIT_1, util.Menu.UNIT_SUB_1);
                currentY = paintCell(g2, currentY, util.Menu.UNIT_2, util.Menu.UNIT_SUB_2);
                currentY = paintCell(g2, currentY, util.Menu.UNIT_3, util.Menu.UNIT_SUB_3);
                currentY = paintCell(g2, currentY, util.Menu.UNIT_4, util.Menu.UNIT_SUB_4);
                currentY = paintCell(g2, currentY, util.Menu.UNIT_5, util.Menu.UNIT_SUB_5);
            } else {
                for (int i = 0; i < 5; i++) {
                    currentY = paintCell(g2, currentY);
                }
            }
        }

        private int paintCell(Graphics2D g2, int currentY) {
            g2.setColor(Color.white);
            g2.drawLine(0, currentY - 1, screenWidth, currentY - 1);
            g2.setColor(appearance.cellColor);
            g2.fillRect(0, currentY, screenWidth, appearance.cellHeight);
            g2.setColor(Color.white);
            g2.drawLine(screenWidth - 20, currentY + appearance.cellHeight / 2 - 5, screenWidth - 15, currentY + appearance.cellHeight / 2);
            g2.drawLine(screenWidth - 15, currentY + appearance.cellHeight / 2, screenWidth - 20, currentY + appearance.cellHeight / 2 + 5);

            g2.setColor(appearance.mainFontColor);
            Font mainFont = new Font("Helvetica", Font.PLAIN, appearance.mainFontSize);
            g2.setFont(mainFont);
            g2.drawString("Test String", leftPadding, currentY + g2.getFontMetrics(mainFont).getHeight());

            g2.setColor(appearance.subFontColor);
            Font subFont = new Font("Helvetica", Font.PLAIN, appearance.subFontSize);
            g2.setFont(subFont);
            g2.drawString("Test Sub String", leftPadding, currentY + g2.getFontMetrics(mainFont).getHeight() + 5 + g2.getFontMetrics(subFont).getHeight());

            g2.setColor(Color.white);
            g2.drawLine(0, currentY + appearance.cellHeight, screenWidth, currentY + appearance.cellHeight);
            return currentY + appearance.cellHeight;
        }

        private int paintCell(Graphics2D g2, int currentY, String text, String subText) {
            g2.setColor(Color.white);
            g2.drawLine(0, currentY - 1, screenWidth, currentY - 1);
            g2.setColor(appearance.cellColor);
            g2.fillRect(0, currentY, screenWidth, appearance.cellHeight);
            g2.setColor(Color.white);
            g2.drawLine(screenWidth - 20, currentY + appearance.cellHeight / 2 - 5, screenWidth - 15, currentY + appearance.cellHeight / 2);
            g2.drawLine(screenWidth - 15, currentY + appearance.cellHeight / 2, screenWidth - 20, currentY + appearance.cellHeight / 2 + 5);

            g2.setColor(appearance.mainFontColor);
            Font mainFont = new Font("Helvetica", Font.PLAIN, appearance.mainFontSize);
            g2.setFont(mainFont);
            g2.drawString(text, leftPadding, currentY + g2.getFontMetrics(mainFont).getHeight());

            g2.setColor(appearance.subFontColor);
            Font subFont = new Font("Helvetica", Font.PLAIN, appearance.subFontSize);
            g2.setFont(subFont);
            g2.drawString(subText, leftPadding, currentY + g2.getFontMetrics(mainFont).getHeight() + g2.getFontMetrics(subFont).getHeight());

            g2.setColor(Color.white);
            g2.drawLine(0, currentY + appearance.cellHeight, screenWidth, currentY + appearance.cellHeight);
            return currentY + appearance.cellHeight;
        }
    }

    /*class ColorButton extends JButton {
        String key;

        public ColorButton(String key) {
            super(getRGB(appearance.getColor(key)));
            this.key = key;
            setBackground(Color.white);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(true);
        }

        public void refresh() {
            setText(getRGB(appearance.getColor(key)));
        }
    }*/

    class ColorField extends JTextField {
        String key;
        int colorType;

        public ColorField(String key, int colorType) {
            super(getRGB(appearance.getColor(key), colorType));
            this.key = key;
            this.colorType = colorType;
            setPreferredSize(new Dimension(40, getPreferredSize().height));
        }
    }

    class ColorButtonPanel extends JPanel implements ActionListener, DocumentListener {
        String key;
        ColorField[] colorFields = new ColorField[4];
        JLabel colorLabel = new JLabel();

        public ColorButtonPanel(String key) {
            super(new GridBagLayout());
            this.key = key;
            for (int cf = 0; cf < colorFields.length; cf++) {
                colorFields[cf] = new ColorField(key, cf);
                colorFields[cf].getDocument().addDocumentListener(this);
                add(colorFields[cf], SwingUtils.makeConstraints(cf + "01110"));
            }
            colorLabel.setPreferredSize(new Dimension(25, 20));
            colorLabel.setOpaque(true);
            colorLabel.setBackground(appearance.getColor(key));
            colorLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showColorChooser();
                }
            });
            add(colorLabel, SwingUtils.makeConstraints("401110"));
        }

        void showColorChooser() {
            Color color = JColorChooser.showDialog(null, "Choose a Color", appearance.getColor(key));
            if (color != null) {
                appearance.setColor(key, color);
//                red.getDocument().removeDocumentListener(this);
//                red.setText(String.valueOf(appearance.getColor(key).getRed()));
//                red.getDocument().addDocumentListener(this);
//                colorLabel.setBackground(appearance.getColor(key));
                refresh();
                displayPanel.repaint();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showColorChooser();
        }

        public Color getColorFieldValue() {
            int redValue = 255;
            int greenValue = 255;
            int blueValue = 255;
            int alphaValue = 255;
            try {
                redValue = Integer.valueOf(colorFields[0].getText());
                greenValue = Integer.valueOf(colorFields[1].getText());
                blueValue = Integer.valueOf(colorFields[2].getText());
                alphaValue = Integer.valueOf(colorFields[3].getText());
                if (redValue < 0) redValue = 0;
                else if (redValue > 255) redValue = 255;
                if (greenValue < 0) greenValue = 0;
                else if (greenValue > 255) greenValue = 255;
                if (blueValue < 0) blueValue = 0;
                else if (blueValue > 255) blueValue = 255;
                if (alphaValue < 0) alphaValue = 0;
                else if (alphaValue > 255) alphaValue = 255;
            } catch (NumberFormatException e) {

            }
            Color color = new Color(redValue, greenValue, blueValue, alphaValue);
            return color;
        }

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            appearance.setColor(key, getColorFieldValue());
            colorLabel.setBackground(appearance.getColor(key));
            displayPanel.repaint();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            appearance.setColor(key, getColorFieldValue());
            colorLabel.setBackground(appearance.getColor(key));
            displayPanel.repaint();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            appearance.setColor(key, getColorFieldValue());
            colorLabel.setBackground(appearance.getColor(key));
            displayPanel.repaint();
        }

        public void refresh() {
            for (int cf = 0; cf < colorFields.length; cf++) {
                colorFields[cf].getDocument().removeDocumentListener(this);
                colorFields[cf].setText(getRGB(appearance.getColor(key), cf));
                colorFields[cf].getDocument().addDocumentListener(this);
            }
            colorLabel.setBackground(appearance.getColor(key));
        }
    }

    class ImageField extends JTextField implements DocumentListener {
        String key;

        public ImageField(String key) {
            super();
            this.key = key;
            setText(appearance.getImageFile(key));
            getDocument().addDocumentListener(this);
        }

        public void refresh() {
            getDocument().removeDocumentListener(this);
            setText(appearance.getImageFile(key));
            getDocument().addDocumentListener(this);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
//            System.out.println("insert " + key);
            appearance.setImageFile(key, getText());
            displayPanel.repaint();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
//            System.out.println("remove " + key);
            appearance.setImageFile(key, getText());
            displayPanel.repaint();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
//            System.out.println("change " + key);
            appearance.setImageFile(key, getText());
            displayPanel.repaint();
        }
    }

    class ImageFilePanel extends JPanel {
        String key;
        ImageField image;
        ImageButton removeImage;

        public ImageFilePanel(String keyInput) {
            super(new GridBagLayout());
            this.key = keyInput;
            image = new ImageField(keyInput);
            add(image, SwingUtils.makeConstraints("001110"));
            ImageButton find = new ImageButton("find.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String fileName = SwingDialog.imageDialog();
                    if (fileName != null) image.setText(fileName);
                }
            };
            find.setFocusable(false);
            add(find, SwingUtils.makeConstraints("101100"));
            removeImage = new ImageButton("minus.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    image.setText("");
                }
            };
            removeImage.setFocusable(false);
            add(removeImage, SwingUtils.makeConstraints("201100"));
        }

        public void refresh() {
            image.refresh();
        }
    }

    class SizePanel extends JPanel implements ActionListener {
        String key;
        JTextField textField = new JTextField();
        ImageButton upSize;
        ImageButton downSize;

        public SizePanel(String keyInput) {
            super(new GridBagLayout());
            this.key = keyInput;
            textField.setText(appearance.getSize(key) + "");
            textField.setBorder(BorderFactory.createEmptyBorder());
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            add(textField, SwingUtils.makeConstraints("001110"));
            upSize = new ImageButton("up.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    appearance.setSize(key, appearance.getSize(key) + 1);
                    textField.setText("" + appearance.getSize(key));
                    displayPanel.repaint();
                }
            };
            add(upSize, SwingUtils.makeConstraints("101100"));
            downSize = new ImageButton("down.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    appearance.setSize(key, appearance.getSize(key) - 1);
                    textField.setText("" + appearance.getSize(key));
                    displayPanel.repaint();
                }
            };
            add(downSize, SwingUtils.makeConstraints("201100"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JTextField) {
                appearance.setSize(key, Integer.parseInt(textField.getText()));
                displayPanel.repaint();
            }
        }

        public void refresh() {
            textField.setText("" + appearance.getSize(key));
        }
    }

    class StringField extends JTextArea implements DocumentListener {
        String key;

        public StringField(String keyInput) {
            super();
            this.key = keyInput;
            setText(appearance.getString(key));
            getDocument().addDocumentListener(this);
        }

        public StringField(String keyInput, int numRows) {
            super();
            setRows(numRows);
            this.key = keyInput;
            setText(appearance.getString(key));
            getDocument().addDocumentListener(this);
        }

        public void refresh() {
            setText(appearance.getString(key));
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            appearance.setString(key, getText());
            displayPanel.repaint();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            appearance.setString(key, getText());
            displayPanel.repaint();

        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            appearance.setString(key, getText());
            displayPanel.repaint();
        }
    }

}
