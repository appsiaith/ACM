package view.detail;

import model.Dialogue;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;
import util.Config;
import util.SwingUtils;
import view.components.*;
import view.panel.ElementGroupPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 27/11/2013
 * Time: 13:41
 */
public class MonologueDetailPanel extends DetailPanel<Dialogue> {

    JTabbedPane tabs;// = new JTabbedPane();
//    JPanel background = new JPanel(new GridBagLayout());

    ElementTextField audioSouth;
    ElementTextField audioNorth;
    HTMLEditorPane south;
    HTMLEditorPane north;

    Vector<JLabel> labels;// = new Vector<JLabel>();
    Vector<ElementComponent> textFields;// = new Vector<JComponent>();

    int type = -1;

    public MonologueDetailPanel(ElementGroupPanel elementGroupPanel) {
        super(elementGroupPanel);
    }

    @Override
    void addComponents() {
        tabs = new JTabbedPane();
        labels = new Vector<JLabel>();
        textFields = new Vector<ElementComponent>();
        if (elementGroupPanel.getElementGroup().getMemberType() == 1) {
            type = 1;
            south = new HTMLEditorPane("south", element);
            tabs.addTab(Config.SOUTH_NAME, south);
            tabs.setMnemonicAt(0, KeyEvent.VK_0);

            north = new HTMLEditorPane("north", element);
            tabs.addTab(Config.NORTH_NAME, north);
            tabs.setMnemonicAt(1, KeyEvent.VK_1);

            tabs.setSelectedIndex(0);
            tabs.setTabPlacement(JTabbedPane.LEFT);
            tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

            add(tabs, SwingUtils.makeConstraints("004111"));

            add(new JLabel("Audio South:"), SwingUtils.makeConstraints("011100"));

            audioSouth = new ElementTextField("audioSouth", element);
            add(audioSouth, SwingUtils.makeConstraints("111110"));
            add(new ImageButton("find.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String fileName = SwingDialog.audioDialog();
                    if (fileName != null) audioSouth.setText(fileName);
                }
            }, SwingUtils.makeConstraints("211100"));
            add(new ImageButton("play.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        new MP3(audioSouth.getText()).play();
                    } catch (IOException e) {
                        SwingDialog.error(e.getMessage(), "Play Audio");
                    }
                }
            }, SwingUtils.makeConstraints("311100"));

            add(new JLabel("Audio North:"), SwingUtils.makeConstraints("021100"));

            audioNorth = new ElementTextField("audioNorth", element);
            add(audioNorth, SwingUtils.makeConstraints("121110"));
            add(new ImageButton("find.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String fileName = SwingDialog.audioDialog();
                    if (fileName != null) audioNorth.setText(fileName);
                }
            }, SwingUtils.makeConstraints("221100"));
            add(new ImageButton("play.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        new MP3(audioNorth.getText()).play();
                    } catch (IOException e) {
                        SwingDialog.error(e.getMessage(), "Play Audio");
                    }
                }
            }, SwingUtils.makeConstraints("321100"));

        } else {
            type = 0;
            String[] fieldNames = element.getFieldNames();
            for (int f = 0; f < fieldNames.length; f++) {
                JLabel label = new JLabel(Config.decorate(fieldNames[f]), SwingConstants.LEFT);
                add(label, SwingUtils.makeConstraints("0," + f + ",1,1,0,0"));
                labels.add(label);

                if (fieldNames[f].contains("audio")) {
                    final ElementTextField textField = new ElementTextField(fieldNames[f], element);
                    add(textField, SwingUtils.makeConstraints("1," + f + ",1,1,1,0"));
                    textFields.add(textField);
                    final int finalF = f;
                    add(new ImageButton("find.png", 20) {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            String fileName = SwingDialog.audioDialog();
                            if (fileName != null) textField.setText(fileName);
                        }
                    }, SwingUtils.makeConstraints("2," + f + ",1,1,0,0"));
                    add(new ImageButton("play.png", 20) {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            try {
                                new MP3(((ElementTextField) textFields.get(finalF)).getText()).play();
                            } catch (IOException e) {
                                SwingDialog.error(e.getMessage(), "Play Audio");
                            }
                        }
                    }, SwingUtils.makeConstraints("3," + f + ",1,1,0,0"));
                } else if (fieldNames[f].contains("speaker")) {
                    String[] wordClasses = {
                            "A",
                            "B"
                    };
                    ElementJComboBox wordClassList = new ElementJComboBox(fieldNames[f], element, wordClasses);
                    wordClassList.setEditable(true);
                    add(wordClassList, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                    textFields.add(wordClassList);
                } else {
                    ElementTextArea textField = new ElementTextArea(fieldNames[f], element, 5);
                    add(textField, SwingUtils.makeConstraints("1," + f + ",3,1,1,1"));
                    textFields.add(textField);
                }
            }
        }
    }

    @Override
    void updateComponents() {
        if (type == elementGroupPanel.getElementGroup().getMemberType()) {
            if (elementGroupPanel.getElementGroup().getMemberType() == 1) {
                south.setModel(element);
                north.setModel(element);
                audioSouth.setModel(element);
                audioNorth.setModel(element);
            } else
                for (int i = 0; i < textFields.size(); i++) textFields.get(i).setModel(element);
        } else {
            removeAll();
            if (elementGroupPanel.getElementGroup().getMemberType() == 1) {
                tabs.removeAll();
                type = 1;
                south = new HTMLEditorPane("south", element);
                tabs.addTab(Config.SOUTH_NAME, south);
                tabs.setMnemonicAt(0, KeyEvent.VK_0);

                north = new HTMLEditorPane("north", element);
                tabs.addTab(Config.NORTH_NAME, north);
                tabs.setMnemonicAt(1, KeyEvent.VK_1);

                tabs.setSelectedIndex(0);
                tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

                add(tabs, SwingUtils.makeConstraints("004111"));

                add(new JLabel("Audio South:"), SwingUtils.makeConstraints("011100"));

                audioSouth = new ElementTextField("audioSouth", element);
                add(audioSouth, SwingUtils.makeConstraints("111110"));
                ImageButton findButton = new ImageButton("find.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        String fileName = SwingDialog.audioDialog();
                        if (fileName != null) audioSouth.setText(fileName);
                    }
                };
                findButton.setFocusable(false);
                add(findButton, SwingUtils.makeConstraints("211100"));
                ImageButton playButton = new ImageButton("play.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            new MP3(audioSouth.getText()).play();
                        } catch (IOException e) {
                            SwingDialog.error(e.getMessage(), "Play Audio");
                        }
                    }
                };
                playButton.setFocusable(false);
                add(playButton, SwingUtils.makeConstraints("311100"));

                add(new JLabel("Audio North:"), SwingUtils.makeConstraints("021100"));

                audioNorth = new ElementTextField("audioNorth", element);
                add(audioNorth, SwingUtils.makeConstraints("121110"));
                add(new ImageButton("find.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        String fileName = SwingDialog.audioDialog();
                        if (fileName != null) audioNorth.setText(fileName);
                    }
                }, SwingUtils.makeConstraints("221100"));
                add(new ImageButton("play.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            new MP3(audioNorth.getText()).play();
                        } catch (IOException e) {
                            SwingDialog.error(e.getMessage(), "Play Audio");
                        }
                    }
                }, SwingUtils.makeConstraints("321100"));
            } else {
                type = 0;
                String[] fieldNames = element.getFieldNames();
                for (int f = 0; f < fieldNames.length; f++) {
                    JLabel label = new JLabel(Config.decorate(fieldNames[f]), SwingConstants.LEFT);
                    add(label, SwingUtils.makeConstraints("0," + f + ",1,1,0,0"));
                    labels.add(label);

                    if (fieldNames[f].contains("audio")) {
                        final ElementTextField textField = new ElementTextField(fieldNames[f], element);
                        add(textField, SwingUtils.makeConstraints("1," + f + ",1,1,1,0"));
                        textFields.add(textField);
                        final int finalF = f;
                        add(new ImageButton("find.png", 20) {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                String fileName = SwingDialog.audioDialog();
                                if (fileName != null) textField.setText(fileName);
                            }
                        }, SwingUtils.makeConstraints("2," + f + ",1,1,0,0"));
                        add(new ImageButton("play.png", 20) {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    new MP3(((ElementTextField) textFields.get(finalF)).getText()).play();
                                } catch (IOException e) {
                                    SwingDialog.error(e.getMessage(), "Play Audio");
                                }
                            }
                        }, SwingUtils.makeConstraints("3," + f + ",1,1,0,0"));
                    } else if (fieldNames[f].contains("speaker")) {
                        String[] wordClasses = {
                                "A",
                                "B"
                        };
                        ElementJComboBox wordClassList = new ElementJComboBox(fieldNames[f], element, wordClasses);
                        wordClassList.setEditable(true);
                        wordClassList.setBorder(BorderFactory.createEmptyBorder());
                        add(wordClassList, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                        textFields.add(wordClassList);
                    } else {
                        ElementTextArea textField = new ElementTextArea(fieldNames[f], element, 5);
                        add(textField, SwingUtils.makeConstraints("1," + f + ",3,1,1,1"));
                        textFields.add(textField);
                    }
                }
            }
        }
    }

}
