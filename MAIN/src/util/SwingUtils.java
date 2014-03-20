package util;

import model.Lesson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import view.Main;
import view.components.SwingDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 03/12/13
 * Time: 23:06
 */
public class SwingUtils {

    /*public static BufferedImage resizeImage(String fileName) throws IOException {
        fileName = fileName.equals("") ? ProgramSettings.IMG_ROOT + "image.png" : fileName;
        File file = new File(fileName);
        if (!file.exists())
            file = new File((new File(Config.DATABASE_FILE).getParent() + File.separator + Config.IMAGE_FOLDER) + File.separator + fileName);
        if (!file.exists())
            file = new File(ProgramSettings.IMG_ROOT + "image.png");
        BufferedImage original = ImageIO.read(file);
        BufferedImage resizedImage = new BufferedImage(ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, ProgramSettings.IMG_WIDTH, ProgramSettings.IMG_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }*/

    /*public static BufferedImage resize(String fileName, int width, int height) throws IOException {
        fileName = fileName.equals("") ? ProgramSettings.IMG_ROOT + "image.png" : fileName;
        File file = new File(fileName);
        if (!file.exists())
            file = new File((new File(Config.DATABASE_FILE).getParent() + File.separator + Config.IMAGE_FOLDER) + File.separator + fileName);
        if (!file.exists())
            file = new File((new File(Config.DATABASE_FILE).getParent() + File.separator + Config.IMAGE_FOLDER) + File.separator + fileName + ".png");
        if (!file.exists())
            file = new File((new File(Config.DATABASE_FILE).getParent() + File.separator + Config.IMAGE_FOLDER) + File.separator + fileName + ".jpg");
        if (!file.exists())
            file = new File(ProgramSettings.IMG_ROOT + "image.png");
        BufferedImage original = ImageIO.read(file);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }*/

    public static Image resize(String fileName, int width, int height) throws IOException {
        if (fileName.trim().isEmpty())
            return ImageIO.read(SwingUtils.class.getClass().getResource(ProgramSettings.NO_IMAGE)).getScaledInstance(width, height, Image.SCALE_FAST);
        File file = new File(fileName);
//        if (!file.exists()) return null;
//        if (!file.exists()) return ImageIO.read(new File(ProgramSettings.NO_IMAGE)).getSubimage(0, 0, width, height);
        if (file.exists()) return resize(ImageIO.read(file), width, height);
        file = new File(Config.getImageFolder() + fileName);
        if (file.exists()) return resize(ImageIO.read(file), width, height);
        return ImageIO.read(SwingUtils.class.getClass().getResource(ProgramSettings.NO_IMAGE)).getScaledInstance(width, height, Image.SCALE_FAST);
    }

    public static Image resize(URL url, int width, int height) throws IOException {
        return ImageIO.read(url).getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static Image resize(Image original, int width, int height) throws IOException {
        return original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        /*BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;*/
    }

    public static Vector<String> optimiseFileNames() {
        Vector<File> deletionList = new Vector<File>();
        Vector<String> missingList = new Vector<String>();
        File tempFolder = new File(Config.getAudioFolder() + "temp");
        try {
            if (!tempFolder.exists()) FileUtils.forceMkdir(tempFolder);
            for (int lessonIndex = 0; lessonIndex < Main.main.lessons.size(); lessonIndex++) {
                Lesson lesson = Main.main.lessons.get(lessonIndex);
                for (int groupIndex = 0; groupIndex < lesson.getQuestions().size(); groupIndex++) {
                    for (int recordIndex = 0; recordIndex < lesson.getQuestions().get(groupIndex).size(); recordIndex++) {
                        lesson.getQuestions().get(groupIndex).get(recordIndex).setQuestionAudioSouth(rename(lesson.getQuestions().get(groupIndex).get(recordIndex).getQuestionAudioSouth(), lessonIndex, groupIndex, recordIndex, "e", "q", deletionList, missingList));
                        lesson.getQuestions().get(groupIndex).get(recordIndex).setAnswerAudioSouth(rename(lesson.getQuestions().get(groupIndex).get(recordIndex).getAnswerAudioSouth(), lessonIndex, groupIndex, recordIndex, "e", "a", deletionList, missingList));
                        lesson.getQuestions().get(groupIndex).get(recordIndex).setQuestionAudioNorth(rename(lesson.getQuestions().get(groupIndex).get(recordIndex).getQuestionAudioNorth(), lessonIndex, groupIndex, recordIndex, "e", "qn", deletionList, missingList));
                        lesson.getQuestions().get(groupIndex).get(recordIndex).setAnswerAudioNorth(rename(lesson.getQuestions().get(groupIndex).get(recordIndex).getAnswerAudioNorth(), lessonIndex, groupIndex, recordIndex, "e", "an", deletionList, missingList));
                    }
                }
                for (int g = 0; g < lesson.getDialogues().size(); g++) {
                    for (int e = 0; e < lesson.getDialogues().get(g).size(); e++) {
                        lesson.getDialogues().get(g).get(e).setAudioSouth(rename(lesson.getDialogues().get(g).get(e).getAudioSouth(), lessonIndex, g, e, "d", "", deletionList, missingList));
                        lesson.getDialogues().get(g).get(e).setAudioNorth(rename(lesson.getDialogues().get(g).get(e).getAudioNorth(), lessonIndex, g, e, "d", "n", deletionList, missingList));
                    }
                }
                for (int g = 0; g < lesson.getPatterns().size(); g++) {
                    for (int e = 0; e < lesson.getPatterns().get(g).size(); e++) {
                        lesson.getPatterns().get(g).get(e).setAudioSouth(rename(lesson.getPatterns().get(g).get(e).getAudioSouth(), lessonIndex, g, e, "p", "", deletionList, missingList));
                        lesson.getPatterns().get(g).get(e).setAudioNorth(rename(lesson.getPatterns().get(g).get(e).getAudioNorth(), lessonIndex, g, e, "p", "n", deletionList, missingList));
                    }
                }
                for (int g = 0; g < lesson.getVocabularies().size(); g++) {
                    for (int e = 0; e < lesson.getVocabularies().get(g).size(); e++) {
                        lesson.getVocabularies().get(g).get(e).setAudioSouth(rename(lesson.getVocabularies().get(g).get(e).getAudioSouth(), lessonIndex, g, e, "v", "", deletionList, missingList));
                        lesson.getVocabularies().get(g).get(e).setAudioNorth(rename(lesson.getVocabularies().get(g).get(e).getAudioNorth(), lessonIndex, g, e, "v", "n", deletionList, missingList));
                    }
                }
            }
            Iterator<File> iterator = deletionList.iterator();
            while (iterator.hasNext()) {
                File file = iterator.next();
                if (file.exists()) FileUtils.forceDelete(file);
                iterator.remove();
            }
            FileUtils.copyDirectory(tempFolder, new File(Config.getAudioFolder()));
            FileUtils.forceDelete(tempFolder);
        } catch (IOException e) {
            SwingDialog.error("Error occured", "Audio file rename");
        }
        return missingList;
    }

    public static String rename(String original, int lessonIndex, int groupIndex, int recordIndex, String prefix, String suffix, Vector<File> deletionList, Vector<String> missingList) throws IOException {
        if (original.isEmpty()) return original;
        String extension = FilenameUtils.getExtension(original);
        File originalFile = extension.isEmpty() ? new File(Config.getAudioFolder() + original + ".mp3") : new File(Config.getAudioFolder() + original);
        File originalFileAbsolute = extension.isEmpty() ? new File(original + ".mp3") : new File(original);
        if (originalFile.exists()) {
            String newName = (lessonIndex + 1) + "_" + prefix + (groupIndex + 1) + "_" + (recordIndex + 1) + suffix + (extension.isEmpty() ? ".mp3" : "." + extension);
            FileUtils.copyFile(originalFile, new File(Config.getAudioFolder() + "temp" + File.separator + newName));
            deletionList.add(originalFile);
            return newName;
        } else if (originalFileAbsolute.exists()) {
            String newName = (lessonIndex + 1) + "_" + prefix + (groupIndex + 1) + "_" + (recordIndex + 1) + suffix + (extension.isEmpty() ? ".mp3" : "." + extension);
            FileUtils.copyFile(originalFileAbsolute, new File(Config.getAudioFolder() + "temp" + File.separator + newName));
            return newName;
        } else {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Config.LESSON_NAME + " " + (lessonIndex + 1) + " ");
            if (prefix.equals("e")) buffer.append(Config.QUESTION_NAME);
            else if (prefix.equals("d")) buffer.append(Config.DIALOG_NAME);
            else if (prefix.equals("v")) buffer.append(Config.VOCABULARY_NAME);
            else if (prefix.equals("p")) buffer.append(Config.PATTERN_NAME);
            buffer.append(" Group " + (groupIndex + 1) + " Record " + (recordIndex + 1) + " Field ");
            if (suffix.equals("q")) buffer.append("QuestionAudio");
            else if (suffix.equals("qn")) buffer.append("QuestionAudioNorth");
            else if (suffix.equals("a")) buffer.append("AnswerAudio");
            else if (suffix.equals("an")) buffer.append("AnswerAudioNorth");
            else if (suffix.equals("")) buffer.append("AudioSouth");
            else if (suffix.equals("n")) buffer.append("AudioNorth");
            buffer.append(": " + original);
            missingList.add(buffer.toString());
        }
        return original;
    }

    public static String copyFile(File file, String folder) {
        if ((file.getParent() + File.separator).equals(folder)) return file.getName();
        File copyFile = new File(folder + file.getName());
        try {
            FileUtils.copyFile(file, copyFile);
            return copyFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static String copyAudio(File file) {
        if (file.getParentFile().getAbsolutePath().equals(Config.getAudioFolder())) return file.getName();
        File copyFile = new File(Config.getAudioFolder() + file.getName());
        try {
            FileUtils.copyFile(file, copyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyFile.getName();
    }

    public static String copyHTML(File file) {
        if (file.getParentFile().getAbsolutePath().equals(Config.getAudioFolder())) return file.getName();
        File copyFile = new File(Config.getHTMLFolder() + file.getName());
        try {
            FileUtils.copyFile(file, copyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyFile.getName();
    }

    public static String copyImage(File file) {
        if (file.getParentFile().getAbsolutePath().equals(Config.getImageFolder())) return file.getName();
        File copyFile = new File(Config.getImageFolder() + file.getName());
        try {
            FileUtils.copyFile(file, copyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyFile.getName();
    }*/

    public static void disableTabbingInTextAreas(Component component) {
        if (component instanceof Container && !(component instanceof JTextArea)) {
            for (final Component c : ((Container) component).getComponents())
                disableTabbingInTextAreas(c);
        } else if (component instanceof JTextArea) {
            final Component c = component;
            c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
            c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
        }
    }

    public static String setLayout(String hint, int numColumns) {
        String[] hintSplit = hint.split(",");
        int gridx = Integer.parseInt(hintSplit[0]);
        int gridy = Integer.parseInt(hintSplit[1]);
        int gridwidth = Integer.parseInt(hintSplit[2]);
        int gridheight = Integer.parseInt(hintSplit[3]);
        int weightx = Integer.parseInt(hintSplit[4]);
        int weighty = Integer.parseInt(hintSplit[5]);
        gridx++;
        weightx = 1;
        if (gridx >= numColumns) {
            gridx = 0;
            weightx = 0;
            gridy++;
        }
        return "" + gridx + "," + gridy + "," + gridwidth + "," + gridheight + "," + weightx + "," + weighty;
    }

    public static GridBagConstraints makeConstraints(String hint) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        if (!hint.contains(",")) {
            constraints.gridx = Integer.parseInt(hint.substring(0, 1));
            constraints.gridy = Integer.parseInt(hint.substring(1, 2));
            constraints.gridwidth = Integer.parseInt(hint.substring(2, 3));
            constraints.gridheight = Integer.parseInt(hint.substring(3, 4));
            constraints.weightx = Double.parseDouble(hint.substring(4, 5));
            constraints.weighty = Double.parseDouble(hint.substring(5, 6));
            constraints.fill = GridBagConstraints.BOTH;
            /*if (Integer.parseInt(hint.substring(4, 5)) > 0 && Integer.parseInt(hint.substring(5, 6)) > 0)
                constraints.fill = GridBagConstraints.BOTH;
            else if (Integer.parseInt(hint.substring(4, 5)) > 0)
                constraints.fill = GridBagConstraints.HORIZONTAL;
            else if (Integer.parseInt(hint.substring(5, 6)) > 0)
                constraints.fill = GridBagConstraints.VERTICAL;
            else constraints.fill = GridBagConstraints.NONE;*/
        } else {
            String[] hintSplit = hint.split(",");
            constraints.gridx = Integer.parseInt(hintSplit[0]);
            constraints.gridy = Integer.parseInt(hintSplit[1]);
            constraints.gridwidth = Integer.parseInt(hintSplit[2]);
            constraints.gridheight = Integer.parseInt(hintSplit[3]);
            constraints.weightx = Double.parseDouble(hintSplit[4]);
            constraints.weighty = Double.parseDouble(hintSplit[5]);
            constraints.fill = GridBagConstraints.BOTH;
            /*if (Double.parseDouble(hintSplit[4]) > 0 && Double.parseDouble(hintSplit[5]) > 0)
                constraints.fill = GridBagConstraints.BOTH;
            else if (Double.parseDouble(hintSplit[4]) > 0)
                constraints.fill = GridBagConstraints.HORIZONTAL;
            else if (Double.parseDouble(hintSplit[5]) > 0)
                constraints.fill = GridBagConstraints.VERTICAL;
            else constraints.fill = GridBagConstraints.NONE;*/
        }
        boolean[] margins = new boolean[]{false, false, false, false};
        constraints.insets = new Insets(
                margins[0] ? ProgramSettings.MARGIN * 2 : ProgramSettings.MARGIN, margins[1] ? ProgramSettings.MARGIN * 2 : ProgramSettings.MARGIN,
                margins[2] ? ProgramSettings.MARGIN * 2 : ProgramSettings.MARGIN, margins[3] ? ProgramSettings.MARGIN * 2 : ProgramSettings.MARGIN
        );
        return constraints;
    }
}
