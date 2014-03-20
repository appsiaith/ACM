package model;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 13/03/2014
 * Time: 13:27
 */

public class SearchResult {
    public int lessonIndex = -1;
    public int elementType = -1;
    public int groupIndex = -1;
    public int elementIndex = -1;
    public String context;

    public SearchResult(int lessonIndex, String context) {
        this.lessonIndex = lessonIndex;
        this.context = context;
    }

    public SearchResult(int lessonIndex, int elementType, int groupIndex, String context) {
        this.lessonIndex = lessonIndex;
        this.elementType = elementType;
        this.groupIndex = groupIndex;
        this.context = context;
    }

    public SearchResult(int lessonIndex, int elementType, int groupIndex, int elementIndex, String context) {
        this.lessonIndex = lessonIndex;
        this.elementType = elementType;
        this.groupIndex = groupIndex;
        this.elementIndex = elementIndex;
        this.context = context;
    }
}
