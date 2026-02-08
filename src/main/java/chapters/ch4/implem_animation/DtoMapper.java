package chapters.ch4.implem_animation;

import core.animation.GraphicsDto;
import core.animation.GridFactory;
import core.animation.LineSegment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoMapper {
    static final int NCELLS = 100;

    public static DtoMapper create() {
        return new DtoMapper();
    }

    public GraphicsDto produce(double cosShift, double sinShift) {
        GraphicsDto dto = new GraphicsDto(
                getLines(sinShift, cosShift),
                getGrids(sinShift, cosShift),
                getTables(cosShift, sinShift),
                false);
        return dto;
    }

    private static List<List<LineSegment>> getLines(double sinShift, double cosShift) {
        return List.of(List.of(LineSegment.black(sinShift*10,cosShift*10,10,10)));
    }

    private static List<double[][]> getGrids(double sinShift, double cosShift) {
        return List.of(
                GridFactory.sincosXyz(NCELLS, NCELLS, sinShift, 0.5),
                GridFactory.sincosXyz(NCELLS, NCELLS, 0.5, cosShift));
    }

    private static List<Object[][]> getTables(double cosShift, double sinShift) {
        return List.of(getPolicyTableData(), getTermTableData(cosShift, sinShift));
    }


    private static Object[][] getPolicyTableData() {
        return new Object[][]{
                {"N", "N", "N", "S", "N", "N", "N", "N"},
                {"N", "S", "N", "N", "W", "N", "E", "N"}
        };
    }

    private static Object[][] getTermTableData(double cosShift, double sinShift) {
        return new Object[][]{
                {"cosShift", cosShift},
                {"sinShift", sinShift}
        };
    }



}
