package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:59
 */
public class Grammar extends Element implements Serializable {

    protected String title = "";
    protected String html = "";

    public Grammar() {
        super();
    }

    public Grammar(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return Element.GRAMMAR;
    }

    @Override
    public String getName() {
        return Element.TEXT_GRAMMAR;
    }

    @Override
    public Element makeInstance() {
        return new Grammar(-1);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"title", "html"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"Title", "HTML"};
    }

    @Override
    public boolean isRaw() {
        if (super.isRaw()) return true;
        if ((html.equals("<p></p>") || html.equals("<p>\n      \n    </p>"))
                && title.equals("")) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + html.hashCode();
        return result;
    }
}
