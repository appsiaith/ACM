package model;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.apache.commons.io.FilenameUtils;
import util.AlphanumComparator;
import util.Config;
import view.Main;
import view.components.SwingDialog;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 14/03/2014
 * Time: 11:02
 */
public class ImportData<T extends Element> extends Vector<Vector<String>> {
    private String fileName;
    private int elementType;
    private String elementName;
    private Class<T> elementClass;

    public ImportData(Class<T> elementClass, int elementType, String elementName, String fileName, String encoding, String delimiter) {
        this.elementClass = elementClass;
        this.elementType = elementType;
        this.elementName = elementName;
        this.fileName = fileName;
        if (fileName == null) readClipboard(delimiter);
        else {
            File file = new File(fileName);
            if (FilenameUtils.getExtension(fileName).equals("xls") || FilenameUtils.getExtension(fileName).equals("xlsx"))
                readExcel(file, encoding);
            else readText(file, encoding, delimiter);
        }
    }

    public void readExcel(File file, String encoding) {
        try {
            WorkbookSettings settings = new WorkbookSettings();
            settings.setEncoding(encoding);
            Workbook w = Workbook.getWorkbook(file, settings);
            Sheet sheet = w.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                boolean empty = true;
                for (int j = 0; j < sheet.getColumns(); j++)
                    empty &= sheet.getCell(j, i).getContents().trim().isEmpty();
                if (empty) continue;
                Vector<String> data = new Vector<String>(sheet.getColumns());
                for (int j = 0; j < sheet.getColumns(); j++) data.add(sheet.getCell(j, i).getContents().trim());
                add(data);
            }
        } catch (IOException e) {
            SwingDialog.error(e.getMessage(), "Import from Excel");
        } catch (BiffException e) {
            SwingDialog.error(e.getMessage(), "Import from Excel");
        }
    }

    public void readText(File file, String encoding, String delimiter) {
        try {
            Scanner scanner = new Scanner(new FileInputStream(file), encoding);
            Vector<String> lines = new Vector<String>();
            int maxColumn = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                maxColumn = maxColumn < line.split(delimiter).length
                        ? line.split(delimiter).length : maxColumn;
                lines.add(line);
            }
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(delimiter);
                boolean empty = true;
                for (int s = 0; s < split.length; s++) empty &= split[s].trim().isEmpty();
                if (empty) continue;
                Vector<String> data = new Vector<String>(maxColumn);
                for (int s = 0; s < maxColumn; s++)
                    if (s < split.length) data.add(split[s].trim());
                    else data.add("");
                add(data);
            }
        } catch (FileNotFoundException e) {
            SwingDialog.error(e.getMessage(), "Import from Text");
        }
    }

    public void readClipboard(String delimiter) {
        try {
            Scanner scanner = new Scanner((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
            Vector<String> lines = new Vector<String>();
            int maxColumn = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                maxColumn = maxColumn < line.split(delimiter).length
                        ? line.split(delimiter).length : maxColumn;
                lines.add(line);
            }
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(delimiter);
                boolean empty = true;
                for (int s = 0; s < split.length; s++) empty &= split[s].trim().isEmpty();
                if (empty) continue;
                Vector<String> data = new Vector<String>(maxColumn);
                for (int s = 0; s < maxColumn; s++)
                    if (s < split.length) data.add(split[s].trim());
                    else data.add("");
                add(data);
            }
        } catch (FileNotFoundException e) {
            SwingDialog.error(e.getMessage(), "Import from Text");
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getHeader() {
        return isEmpty() ? null : get(0);
    }

    public void extractLessonIds(int lessonIndex, boolean hasHeader, Vector<String> candidateLessonIds) {
        if (lessonIndex < 0) {
            if (!candidateLessonIds.contains("N/A")) candidateLessonIds.add("N/A");
        } else {
            for (int i = hasHeader ? 1 : 0; i < size(); i++)
                if (!candidateLessonIds.contains(get(i).get(lessonIndex)))
                    candidateLessonIds.add(get(i).get(lessonIndex));
        }
    }

    public String process(Vector<String> headers, final HashMap<String, Lesson> mapping, boolean hasHeader, Vector<String> candidateLessonIds) {
        //remove header line
        if (hasHeader) remove(0);
        //clean null headers & corresponding data entries
        Vector<Integer> nullIndices = new Vector<Integer>();
        for (int h = 0; h < headers.size(); h++) if (headers.get(h) == null) nullIndices.add(h);
        for (int n = 0; n < nullIndices.size(); n++) {
            //for (int i = 0; i < size(); i++) candidateLessonIds.add(get(i).remove(nullIndices.get(n) - n));
            for (int i = 0; i < size(); i++) get(i).remove(nullIndices.get(n) - n);
            headers.remove(nullIndices.get(n) - n);
        }
        int lessonIndex = -1;
        for (int h = 0; h < headers.size(); h++)
            if (headers.get(h).equals(Config.LESSON_NAME.toLowerCase() + "Id")) lessonIndex = h;
        TreeMap<String, Vector<Vector<String>>> lessonDatas = divideByIndex(this, lessonIndex);
        if (lessonIndex >= 0) headers.remove(lessonIndex);
        int grouping = -1;
        for (int h = 0; h < headers.size(); h++)
            if (headers.get(h).equals("grouping")) grouping = h;
        TreeMap<String, TreeMap<String, Vector<Vector<String>>>> lessonGroupDatas = new TreeMap<String, TreeMap<String, Vector<Vector<String>>>>(new AlphanumComparator());
        Iterator it = lessonDatas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            TreeMap<String, Vector<Vector<String>>> lessonGroupData = divideByIndex((Vector<Vector<String>>) pairs.getValue(), grouping);
            lessonGroupDatas.put((String) pairs.getKey(), lessonGroupData);
            it.remove();
        }
        if (grouping >= 0) headers.remove(grouping);
        return importData(lessonGroupDatas, headers, mapping);
    }

    public String process(Vector<String> headers, boolean hasHeader, int lessonIndex) {
        //remove header line
        if (hasHeader) remove(0);
        //clean null headers & corresponding data entries
        Vector<Integer> nullIndices = new Vector<Integer>();
        for (int h = 0; h < headers.size(); h++) if (headers.get(h) == null) nullIndices.add(h);
        for (int n = 0; n < nullIndices.size(); n++) {
            for (int i = 0; i < size(); i++) get(i).remove(nullIndices.get(n) - n);
            headers.remove(nullIndices.get(n) - n);
        }
        int grouping = -1;
        for (int h = 0; h < headers.size(); h++)
            if (headers.get(h).equals("grouping")) grouping = h;
        TreeMap<String, Vector<Vector<String>>> dividedData = divideByIndex(this, grouping);
        if (grouping >= 0) headers.remove(grouping);
        return importData(dividedData, headers, lessonIndex);
    }

    public TreeMap<String, Vector<Vector<String>>> divideByIndex(final Vector<Vector<String>> input, final int index) {
        TreeMap<String, Vector<Vector<String>>> dividedData = new TreeMap<String, Vector<Vector<String>>>(new AlphanumComparator());
        if (index >= 0) {
            //has lesson
            for (int i = 0; i < input.size(); i++) {
                Vector<String> newData = (Vector<String>) input.get(i).clone();
                newData.remove(index);
                if (dividedData.containsKey(input.get(i).get(index)))
                    dividedData.get(input.get(i).get(index)).add(newData);
                else {
                    Vector<Vector<String>> lessonData = new Vector<Vector<String>>();
                    lessonData.add(newData);
                    dividedData.put(input.get(i).get(index), lessonData);
                }
            }
        } else {
            //no lesson (-> one lesson)
            for (int i = 0; i < input.size(); i++) {
                Vector<String> newData = (Vector<String>) input.get(i).clone();
                if (dividedData.containsKey("N/A"))
                    dividedData.get("N/A").add(newData);
                else {
                    Vector<Vector<String>> lessonData = new Vector<Vector<String>>();
                    lessonData.add(newData);
                    dividedData.put("N/A", lessonData);
                }
            }
        }
        return dividedData;
    }

    public String importData(final TreeMap<String, TreeMap<String, Vector<Vector<String>>>> data, final Vector<String> header, final HashMap<String, Lesson> mapping) {
        StringBuffer buffer = new StringBuffer();
        Iterator lessonIterator = data.entrySet().iterator();
        int groupCount = 0;
        int elementCount = 0;
        while (lessonIterator.hasNext()) {
            //this is a lesson-level data
            Map.Entry lessonPair = (Map.Entry) lessonIterator.next();
            TreeMap<String, Vector<Vector<String>>> groupData = (TreeMap<String, Vector<Vector<String>>>) lessonPair.getValue();
            Iterator groupTterator = groupData.entrySet().iterator();
            while (groupTterator.hasNext()) {
                //this is a group-level data
                Map.Entry groupPair = (Map.Entry) groupTterator.next();
                Vector<Vector<String>> elementData = (Vector<Vector<String>>) groupPair.getValue();
                ElementGroup<T> group = new ElementGroup<T>(elementClass);
                for (int e = 0; e < elementData.size(); e++) {
                    group.add(importElement(header, elementData.get(e)));
                    elementCount++;
                }
                mapping.get(lessonPair.getKey()).getElementGroups(elementType).add(group);
                groupCount++;
            }
        }
        buffer.append("From file '" + fileName + "':\n");
        buffer.append("\trecord type " + elementName + ", " + groupCount + " groups, " + elementCount + " records.");
        return buffer.toString();
    }

    public String importData(final TreeMap<String, Vector<Vector<String>>> data, final Vector<String> header, final int lessonIndex) {
        StringBuffer buffer = new StringBuffer();
        int elementCount = 0;
        int groupCount = 0;
        Iterator groupTterator = data.entrySet().iterator();
        while (groupTterator.hasNext()) {
            Map.Entry groupPair = (Map.Entry) groupTterator.next();
            Vector<Vector<String>> elementData = (Vector<Vector<String>>) groupPair.getValue();
            ElementGroup<T> group = new ElementGroup<T>(elementClass);
            for (int e = 0; e < elementData.size(); e++) {
                group.add(importElement(header, elementData.get(e)));
                elementCount++;
            }
            Main.main.lessons.get(lessonIndex).getElementGroups(elementType).add(group);
            groupCount++;
        }
        buffer.append("From clipboard:\n");
        buffer.append("\trecord type " + elementName + ", " + groupCount + " groups, " + elementCount + " records.");
        return buffer.toString();
    }

    public T importElement(final Vector<String> headers, final Vector<String> data) {
        T model = (T) Element.ELEMENT_INSTANCES[elementType].makeInstance();
        for (int h = 0; h < headers.size(); h++) model.setField(headers.get(h), data.get(h));
        return model;
    }
}
