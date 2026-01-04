package chapters.ch14.environments.pong_graphics;

import lombok.With;
import java.awt.*;

/***
 * Panel properties
 */

public record PanelProperties(
    @With  Color color,
    @With int widthPanel,
    int heightPanel
)
{

}
