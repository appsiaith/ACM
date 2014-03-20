package view.detail;

import model.Question;
import util.Config;
import util.ProgramSettings;
import util.SwingUtils;
import view.components.*;
import view.panel.ElementGroupPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 27/11/2013
 * Time: 13:41
 */
public class QuestionDetailPanel extends DetailPanel<Question> {
    private Vector<Vector<JComponent>> componentGroups;

    public QuestionDetailPanel(final ElementGroupPanel panel) {
        super(panel);
    }

    @Override
    void addComponents() {
        componentGroups = new Vector<Vector<JComponent>>(6);
        JPanel topPanel = new JPanel(new GridBagLayout());
        int row = 0;
        Vector<JComponent> titleGroup = new Vector<JComponent>();
        addTextComponents(titleGroup, "title", topPanel, ++row);
        componentGroups.add(0, titleGroup);

        Vector<JComponent> pictureGroup = new Vector<JComponent>();
//        JPanel picturePanel = new JPanel(new GridBagLayout());
        JPanel audioPanel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(Config.decorate("picture"));
        audioPanel.add(label, SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
        pictureGroup.add(label);
        final Picture picture = new Picture(element.getPicture());
        add(picture, SwingUtils.makeConstraints("1," + 1 + ",1,1,0,0"));
        pictureGroup.add(picture);
        final ElementTextField pictureText = new ElementTextField("picture", element);
        audioPanel.add(pictureText, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
        pictureGroup.add(pictureText);
        ImageButton pictureButton = new ImageButton("find.png", 20) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String fileName = SwingDialog.imageDialog();
                if (fileName != null) {
                    pictureText.setText(fileName);
                    picture.setFileName(fileName);
                }
            }
        };
        pictureButton.setFocusable(false);
        audioPanel.add(pictureButton, SwingUtils.makeConstraints("2," + row + ",1,1,0,1"));
        pictureGroup.add(pictureButton);
        ImageButton removeImage = new ImageButton("minus.png", 20) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                pictureText.setText("");
                picture.setFileName("");
            }
        };
        removeImage.setFocusable(false);
        audioPanel.add(removeImage, SwingUtils.makeConstraints("3," + row + ",1,1,0,0"));
        pictureGroup.add(removeImage);
        componentGroups.add(1, pictureGroup);

        Vector<JComponent> questionGroup = new Vector<JComponent>();
        addTextComponents(questionGroup, "questionSouth", topPanel, ++row);
        addTextComponents(questionGroup, "questionNorth", topPanel, ++row);
        componentGroups.add(2, questionGroup);

        Vector<JComponent> answerGroup = new Vector<JComponent>();
        addTextComponents(answerGroup, "answerSouth", topPanel, ++row);
        addTextComponents(answerGroup, "answerNorth", topPanel, ++row);
        componentGroups.add(3, answerGroup);

        add(topPanel, SwingUtils.makeConstraints("0" + 0 + "2111"));

        row = 1;
        Vector<JComponent> questionAudioGroup = new Vector<JComponent>();
        addAudioComponents(questionAudioGroup, "questionAudioSouth", audioPanel, ++row);
        addAudioComponents(questionAudioGroup, "questionAudioNorth", audioPanel, ++row);
        componentGroups.add(4, questionAudioGroup);
        Vector<JComponent> answerAudioGroup = new Vector<JComponent>();
        addAudioComponents(answerAudioGroup, "answerAudioSouth", audioPanel, ++row);
        addAudioComponents(answerAudioGroup, "answerAudioNorth", audioPanel, ++row);
        componentGroups.add(5, answerAudioGroup);
        audioPanel.setPreferredSize(new Dimension(audioPanel.getPreferredSize().width, ProgramSettings.IMG_HEIGHT));
        add(audioPanel, SwingUtils.makeConstraints("0," + 1 + ",1,1,1,1"));

        enableComponents();
    }

    private void addAudioComponents(Vector<JComponent> componentGroup, String fieldName, JPanel audioPanel, int row) {
        JLabel label = new JLabel(Config.decorate(fieldName));
        audioPanel.add(label, SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
        componentGroup.add(label);
        final ElementTextField audio = new ElementTextField(fieldName, element);
        audioPanel.add(audio, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
        componentGroup.add(audio);
        ImageButton findButton = new ImageButton("find.png", 20) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String fileName = SwingDialog.audioDialog();
                if (fileName != null) audio.setText(fileName);
            }
        };
        findButton.setFocusable(false);
        audioPanel.add(findButton, SwingUtils.makeConstraints("2," + row + ",1,1,0,1"));
        componentGroup.add(findButton);
        ImageButton playButton = new ImageButton("play.png", 20) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    new MP3(audio.getText()).play();
                } catch (IOException e) {
                    SwingDialog.error(e.getMessage(), "Play Audio");
                }
            }
        };
        playButton.setFocusable(false);
        audioPanel.add(playButton, SwingUtils.makeConstraints("3," + row + ",1,1,0,1"));
        componentGroup.add(playButton);
    }

    private void addTextComponents(Vector<JComponent> componentGroup, String fieldName, JPanel topPanel, int row) {
        JLabel label = new JLabel(Config.decorate(fieldName));
        topPanel.add(label, SwingUtils.makeConstraints("0," + row + ",1,1,0,0.5"));
        componentGroup.add(label);
        ElementTextArea text = new ElementTextArea(fieldName, element, 5);
        topPanel.add(text, SwingUtils.makeConstraints("1," + row + ",1,1,1,0.5"));
        componentGroup.add(text);
    }

    /*private void addTextField(Vector<JComponent> componentGroup, String fieldName, JPanel topPanel, int row) {
        JLabel label = new JLabel(Config.decorate(fieldName));
        topPanel.add(label, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
        componentGroup.add(label);
        ElementTextField text = new ElementTextField(fieldName, element);
        topPanel.add(text, SwingUtils.makeConstraints("1" + row + "1110"));
        componentGroup.add(text);
    }*/

    @Override
    void updateComponents() {
        enableComponents();
        for (int b = 0; b < componentGroups.size(); b++)
            for (int c = 0; c < componentGroups.get(b).size(); c++)
                if (componentGroups.get(b).get(c) instanceof ElementComponent)
                    ((ElementComponent) componentGroups.get(b).get(c)).setModel(element);
                else if (componentGroups.get(b).get(c) instanceof Picture)
                    ((Picture) componentGroups.get(b).get(c)).setFileName(element.getField("picture"));
    }

    private void enableComponents() {
        String binaryType = Integer.toBinaryString(elementGroupPanel.getElementGroup().getMemberType());
        while (binaryType.length() < 6) binaryType = "0" + binaryType;
        for (int b = 0; b < binaryType.length(); b++)
            for (int c = 0; c < componentGroups.get(b).size(); c++)
                componentGroups.get(b).get(c).setEnabled(binaryType.charAt(b) == '0');
    }

    class Picture extends JComponent {
        String fileName;
        Image image;

        public Picture(String fileName) {
            super();
            setFocusable(false);
            setPreferredSize(new Dimension(ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT));
            setMinimumSize(new Dimension(ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT));
            try {
                this.image = SwingUtils.resize(fileName, ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.fileName = fileName;
        }

        protected void paintComponent(Graphics graphics) {
            Graphics g = graphics.create();
            if (isEnabled()) {
                if (image != null) {
                    int width = (getWidth() - ProgramSettings.IMG_WIDTH) / 2;
                    int height = (getHeight() - ProgramSettings.IMG_HEIGHT) / 2;
                    g.drawImage(image, width < 0 ? 0 : width, height < 0 ? 0 : height, this);
                }
            } else {
                g.setColor(getBackground());
                g.fillRect(0, 0, ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT);
            }
            g.dispose();
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
            try {
                this.image = SwingUtils.resize(fileName, ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }
}
