package view.frame;

import model.SearchResult;
import util.Config;
import util.SwingUtils;
import view.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 13/03/2014
 * Time: 13:17
 */
public class SearchResultFrame extends MyFrame {

    public SearchResultFrame(String keyword, final Vector<SearchResult> results) {
        super("Search Result for '" + keyword + "'");
        JLabel hint = new JLabel("Double click an entry to view its details.");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        add(hint, SwingUtils.makeConstraints("0" + 0 + "2110"));
        JLabel location = new JLabel("Location");
        location.setOpaque(true);
        location.setHorizontalAlignment(SwingConstants.CENTER);
        location.setBackground(Color.DARK_GRAY);
        location.setForeground(Color.WHITE);
        add(location, SwingUtils.makeConstraints("0" + 1 + "1110"));
        JLabel context = new JLabel("Conext");
        context.setOpaque(true);
        context.setHorizontalAlignment(SwingConstants.CENTER);
        context.setBackground(Color.DARK_GRAY);
        context.setForeground(Color.WHITE);
        add(context, SwingUtils.makeConstraints("1" + 1 + "1110"));
        for (int r = 0; r < results.size(); r++) {
            JLabel label = new JLabel(Config.LESSON_NAME + " " + (results.get(r).lessonIndex + 1)
                    + (results.get(r).elementType >= 0 ? (" " + Config.getElementNames()[results.get(r).elementType]
                    + (" Group " + (results.get(r).groupIndex + 1)
                    + (results.get(r).elementIndex >= 0 ? (" Record " + (results.get(r).elementIndex + 1)) : ""))) : ""));
            JLabel labelContext = new JLabel(shortenContext(results.get(r).context, keyword));
            final int finalR = r;
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (results.get(finalR).elementIndex >= 0) {
                            Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex),
                                    results.get(finalR).elementType,
                                    results.get(finalR).groupIndex,
                                    results.get(finalR).elementIndex);
                        } else if (results.get(finalR).elementType >= 0)
                            Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex), results.get(finalR).elementType);
                        else Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex), 0);
                    }
                }
            });
            labelContext.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (results.get(finalR).elementIndex >= 0) {
                            Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex),
                                    results.get(finalR).elementType,
                                    results.get(finalR).groupIndex,
                                    results.get(finalR).elementIndex);
                        } else if (results.get(finalR).elementType >= 0)
                            Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex), results.get(finalR).elementType);
                        else Main.main.makeLessonFrame(Main.main.lessons.get(results.get(finalR).lessonIndex), 0);
                    }
                }
            });
            add(label, SwingUtils.makeConstraints("0," + (r + 2) + ",1,1,1,0"));
            add(labelContext, SwingUtils.makeConstraints("1," + (r + 2) + ",1,1,1,0"));
        }
        setLocation(MouseInfo.getPointerInfo().getLocation());
        pack();
        setVisible(true);
    }

    public String shortenContext(String context, String keyword) {
        int begin = context.indexOf(keyword) - 10 < 0 ? 0 : context.indexOf(keyword) - 10;
        int end = context.indexOf(keyword) + keyword.length() + 10 > context.length() ? context.length() : context.indexOf(keyword) + keyword.length() + 10;
        return (begin == 0 ? "" : "...") + context.substring(begin, end) + (end == context.length() ? "" : "...");
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        return false;
    }

    @Override
    protected boolean hotkeyAction(KeyEvent e) {
        return false;
    }
}
