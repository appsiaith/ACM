package view.frame;

import model.Element;
import model.Lesson;
import util.Config;
import util.SwingUtils;
import view.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 25/02/2014
 * Time: 16:52
 */
public class StatisticFrame extends MyFrame {

    public StatisticFrame() {
        super("Project Statistics");
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        buffer.append("Project name: <b>" + Config.PROJECT_NAME + "</b><br>");
        buffer.append("Total number of " + Config.LESSON_NAME + "s: <b>" + Main.main.lessons.size() + "</b><br>");
        if (Main.main.lessons.size() > 0) {
            buffer.append("<table align=\"center\" style=\"border-collapse: collapse;\">\n" +
                    "  <tr>\n" +
                    "    <td></td>\n" +
                    "    <td colspan=2 align=center bgcolor=\"#EDC421\" style=\"border-bottom:1pt solid black;\">" + Config.PATTERN_NAME + "</td>\n" +
                    "    <td colspan=2 align=center bgcolor=\"#EDC421\" style=\"border-bottom:1pt solid black;\">" + Config.QUESTION_NAME + "</td>\n" +
                    "    <td colspan=2 align=center bgcolor=\"#EDC421\" style=\"border-bottom:1pt solid black;\">" + Config.GRAMMAR_NAME + "</td>\n" +
                    "    <td colspan=2 align=center bgcolor=\"#EDC421\" style=\"border-bottom:1pt solid black;\">" + Config.VOCABULARY_NAME + "</td>\n" +
                    "    <td colspan=2 align=center bgcolor=\"#EDC421\" style=\"border-bottom:1pt solid black;\">" + Config.DIALOG_NAME + "</td>\n" +
                    "  </tr>\n");
            buffer.append("  <tr style=\"border-bottom:1pt solid black;\">\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;border-right:1pt solid black;\">" + Config.LESSON_NAME + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Group" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Total" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Group" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Total" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Group" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Total" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Group" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Total" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Group" + "</td>\n" +
                    "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-bottom:1pt solid black;\">" + "Total" + "</td>\n" +
                    "  </tr>\n");
            for (int l = 0; l < Main.main.lessons.size(); l++) {
                Lesson lesson = Main.main.lessons.get(l);
                buffer.append("<tr>\n" +
                        "    <td align=center bgcolor=\"#F5F5DC\" style=\"border-right:1pt solid black;\">" + (l + 1) + "</td>" +
                        "    <td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.PATTERN).size() + "</td><td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.PATTERN).sum() + "</td>" +
                        "    <td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.QUESTION).size() + "</td><td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.QUESTION).sum() + "</td>" +
                        "    <td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.GRAMMAR).size() + "</td><td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.GRAMMAR).sum() + "</td>" +
                        "    <td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.VOCABULARY).size() + "</td><td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.VOCABULARY).sum() + "</td>" +
                        "    <td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.DIALOG).size() + "</td><td align=center bgcolor=\"white\">" + lesson.getElementGroups(Element.DIALOG).sum() + "</td>");
                buffer.append("</tr>\n");
            }
            buffer.append("</table>");
        }
        buffer.append("</body></html>");
        add(new JLabel(buffer.toString()), SwingUtils.makeConstraints("001111"));
        pack();
        Dimension dimension = getPreferredSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2 - dimension.getWidth() / 2), (int) (screenSize.getHeight() / 2 - dimension.getHeight() / 2));
        setVisible(true);
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        if (e == null || (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            Main.main.refresh();
            Main.main.statisticFrame = null;
            dispose();
            Main.main.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    protected boolean hotkeyAction(KeyEvent e) {
        return false;
    }
}
