package core.animation;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record GraphicsDto(
        List<List<LineSegment>> lines,
        List<double[][]> grids,
        List<Object[][]> tableData,
        boolean isFail
) {

    public static GraphicsDto lines(List<List<LineSegment>> lines, boolean isFail) {
        return builder()
                .lines(lines)
                .grids(new ArrayList<>())
                .tableData(new ArrayList<>())
                .isFail(isFail)
                .build();
    }

    public static GraphicsDto grids(List<double[][]> grids, boolean isFail) {
        return builder()
                .lines(new ArrayList<>())
                .grids(grids)
                .tableData(new ArrayList<>())
                .isFail(isFail)
                .build();
    }

    public static GraphicsDto table(List<Object[][]> tables, boolean isFail) {
        return builder()
                .lines(new ArrayList<>())
                .grids(new ArrayList<>())
                .tableData(tables)
                .isFail(isFail)
                .build();
    }

    public List<LineSegment> getLines(int i) {
        return lines.get(i);
    }

    public double[][] getGrid(int i) {
        return grids.get(i);
    }

    public Object[][] getTable(int i) {
        return tableData.get(i);
    }

}
