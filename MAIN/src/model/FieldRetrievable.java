package model;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 13/03/2014
 * Time: 08:57
 */
public interface FieldRetrievable {
    public abstract boolean setField(String key, String value);

    public abstract String getField(String key);

    public abstract String[] getFieldNames();

    public abstract String[] getFieldNamesShort();
}
