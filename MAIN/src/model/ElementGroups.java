package model;

import database.Database;
import util.Config;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 05/12/2013
 * Time: 09:31
 */
public class ElementGroups<T extends Element> extends Vector<ElementGroup<T>> implements Serializable {
    Class<T> elementClass;
    Integer id;
    T instance;
    private int initialHashCode;

    public ElementGroups(Class<T> elementClass) {
        super();
        this.elementClass = elementClass;
        try {
            instance = elementClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        id = Database.next();
    }

    public ElementGroups fill() {
        if (!Config.isAutoAppend()) return this;
        if (isEmpty()) addRaw(0);
        for (int i = 0; i < size() - 1; i++)
            if (get(i).isEmpty()) get(i).addRaw(0);
        return this;
    }

    @Override
    public synchronized ElementGroup<T> get(int index) {
        return super.get(index);
    }

    public void addRaw(int index) {
        super.add(index, new ElementGroup<T>(elementClass));
    }

    @Override
    public synchronized ElementGroup<T> remove(int index) {
        ElementGroup<T> removed = super.remove(index);
        if (isEmpty() && Config.isAutoAppend()) addRaw(0);
        return removed;
    }

    public int sum() {
        int count = 0;
        for (int i = 0; i < size(); i++) count += get(i).size();
        return count;
    }

    public Class<T> getElementClass() {
        return elementClass;
    }

    public T getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ElementGroups that = (ElementGroups) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    public void setInitialHashCode(int initialHashCode) {
        this.initialHashCode = initialHashCode;
    }

    public int getInitialHashCode() {
        return initialHashCode;
    }
}
