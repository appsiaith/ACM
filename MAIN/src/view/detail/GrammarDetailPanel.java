package view.detail;

import model.Grammar;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;
import util.SwingUtils;
import view.components.ElementTextField;
import view.panel.ElementGroupPanel;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 27/11/2013
 * Time: 13:41
 */
public class GrammarDetailPanel extends DetailPanel<Grammar> {

    ElementTextField grammarHeading;
    HTMLEditorPane grammarText;

    public GrammarDetailPanel(final ElementGroupPanel elementPanel) {
        super(elementPanel);
    }

    @Override
    void addComponents() {
        add(new JLabel("Title:"), SwingUtils.makeConstraints("0,0,1,1,0,0"));
        grammarHeading = new ElementTextField("title", element);
        add(grammarHeading, SwingUtils.makeConstraints("1,0,1,1,1,0"));
        grammarText = new HTMLEditorPane("html", element);
        add(grammarText, SwingUtils.makeConstraints("0,1,2,1,1,1"));
    }

    @Override
    void updateComponents() {
        grammarHeading.setModel(element);
        grammarText.setModel(element);
    }
}
