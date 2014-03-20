package model;

import database.Database;
import util.Config;
import util.Logging;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 29/11/2013
 * Time: 15:15
 */
public class ElementGroup<T extends Element> extends Vector<T> implements Serializable, FieldRetrievable {
    protected Class<T> elementClass;
    protected Integer id = null;
    protected String south = "";
    protected String north = "";
    protected String english = "";
    protected int memberType;

    public ElementGroup(Class<T> elementClass) {
        super();
        this.elementClass = elementClass;
        id = Database.next();
        if (Config.isAutoAppend()) addRaw(0);
    }

    public boolean checkRaw() {
        if (isEmpty()) {
            try {
                add(elementClass.newInstance());
                Logging.info("Added a blank object.");
                return true;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        boolean hasRaw = get(size() - 1).isRaw();
        if (!hasRaw) {
            try {
                add(elementClass.newInstance());
                Logging.info("Added a blank object.");
                return true;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ElementGroup(Class<T> elementClass, int id, int memberType, String south, String north, String english) {
        super();
        this.elementClass = elementClass;
        this.id = id;
        this.memberType = memberType;
        this.south = south == null ? "" : south;
        this.north = north == null ? "" : north;
        this.english = english == null ? "" : english;
    }

    public boolean setField(String key, String value) {
        try {
            Field field = this.getClass().getDeclaredField(key);
            if (int.class.isAssignableFrom(field.getType())) field.setInt(this, Integer.parseInt(value));
            else field.set(this, value);
            return true;
        } catch (NumberFormatException e) {
            Logging.error("NumberFormatException: key = " + key + ", value = " + value);
            setField(key, "0");
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getField(String key) {
        try {
            Field field = this.getClass().getDeclaredField(key);
            field.setAccessible(true);
            return field.get(this) == null ? "" : field.get(this).toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"english", "south", "north", "memberType"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"English", "South", "North", "MemberType"};
    }

    public int reorder(int[] fromIndices, int toIndex) {
        int fromReduction = 0;
        for (int f = 0; f < fromIndices.length; f++) {
            if (toIndex <= fromIndices[f]) {
                add(toIndex, get(fromIndices[f]));
                remove(fromIndices[f] + 1);
                toIndex = toIndex + 1;
            } else {
                add(toIndex, get(fromIndices[f] - fromReduction));
                remove(fromIndices[f] - fromReduction);
                fromReduction = fromReduction + 1;
            }

        }
        return toIndex - fromIndices.length;
    }

    public int transferTo(ElementGroup group, int[] fromIndices, int toIndex) {
        Vector<T> items = new Vector<T>();
        for (int i = 0; i < fromIndices.length; i++)
            items.add(get(fromIndices[i]));
        removeAll(items);
        group.addAll(toIndex, items);
        return toIndex;
    }

    @Override
    public synchronized boolean add(T element) {
        return super.add(element);
    }

    public void addRaw(int index) {
        T element = null;
        try {
            element = elementClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        T t = super.remove(index);
        if (isEmpty() && Config.isAutoAppend()) addRaw(0);
        return t;
    }

    public String getSouth() {
        return south;
    }

    public void setSouth(String south) {
        this.south = south;
    }

    public String getNorth() {
        return north;
    }

    public void setNorth(String north) {
        this.north = north;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Class<T> getElementClass() {
        return elementClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ElementGroup that = (ElementGroup) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + elementClass.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + south.hashCode();
        result = 31 * result + north.hashCode();
        result = 31 * result + english.hashCode();
        result = 31 * result + memberType;
        return result;
    }

    public boolean isRaw() {
        return (isEmpty() && south.equals("") && north.equals("") && english.equals(""));
    }
}
