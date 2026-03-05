package core.animation;

import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public record GraphicsDto(
        List<List<LineSegment>> lines,
        List<double[][]> grids,
        List<Object[][]> tableData,
        boolean isFail,
        int animationDelay
) {



    public static GraphicsDto dtoStep(List<List<LineSegment>> lineData,
                                       List<Object[][]> tableData,
                                       int animationDelay,
                                       boolean isFail) {
        return GraphicsDto.builder()
                .lines(lineData)
                .tableData(tableData)
                .isFail(isFail)
                .animationDelay(animationDelay)
                .build();
    }

    public static GraphicsDto dtoEpisode(List<double[][]> grids, Object[][] policyGrid) {
        return GraphicsDto.builder()
                .grids(grids)
                .tableData(Collections.singletonList(policyGrid))
                .animationDelay(0)
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
