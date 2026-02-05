package chapters.ch14.pong_animation;

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
