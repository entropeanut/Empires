package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }
    
    private final Object[] headers;
    private Alignment[] alignments;
    private final List<Object[]> rows = new ArrayList<>();

    public Table(Object ... headers) {
        this.headers = headers;
        this.alignments = new Alignment[headers.length];
        Arrays.fill(alignments, Alignment.LEFT);
    }
    
    public Table addRow(Object ... values) {
        rows.add(values);
        return this;
    }

    public Object[] getHeaders() {
        return headers;
    }

    public Alignment[] getColAlignments() {
        return alignments;
    }

    public Table setColAlignments(Alignment ... alignments) {
        this.alignments = alignments;
        return this;
    }
    
    public Table setColAlignment(int colIndex, Alignment alignment) {
        this.alignments[colIndex] = alignment;
        return this;
    }

    public List<Object[]> getRows() {
        return rows;
    }
}
