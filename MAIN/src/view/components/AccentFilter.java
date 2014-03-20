package view.components;

import view.Main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 24/02/2014
 * Time: 13:11
 */

public class AccentFilter extends DocumentFilter {
    public static AccentFilter filter = new AccentFilter();
    private boolean enabled = true;

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (Main.main.activeAccent != null) {
            fb.insertString(offset, addAccent(string), attr);
        } else super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (Main.main.activeAccent != null) {
            fb.replace(offset, length, addAccent(text), attrs);
        } else super.replace(fb, offset, length, text, attrs);
    }

    private String addAccent(String input) {
        if (!enabled) return input;
        StringBuffer buffer = new StringBuffer();
        for (char c : input.toCharArray()) buffer.append(Main.main.doAccent(c));
        return buffer.toString();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
