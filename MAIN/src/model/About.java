package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:59
 */
public class About extends Element implements Serializable {

    protected String title = "";
    protected String info = "";
    protected String url = "";
    protected String linkType = "";

    public About() {
        super();
    }

    public About(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return Element.ABOUT;
    }

    @Override
    public String getName() {
        return Element.TEXT_ABOUT;
    }

    @Override
    public Element makeInstance() {
        return new About(-1);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + info.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + linkType.hashCode();
        return result;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"title", "info", "url", "linkType"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"Title", "Info", "URL", "LinkType"};
    }
}
