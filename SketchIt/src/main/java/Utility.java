
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Vector;

public class Utility {

    static void clipChildren(Region region) {

        final Rectangle outputClip = new Rectangle();
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    static Border createBorder() {
        return new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(0), BorderStroke.THIN));
    }

    static void WriteShapetoFile(Shape shapes) {
        if (shapes instanceof Line) System.out.println("LINE");
        if (shapes instanceof Polygon) System.out.println("RECTANGLE");
        if (shapes instanceof Circle) System.out.println("CIRCLE");
        //for (Shape s: shapes) {
        //    if (s instanceof Line) System.out.println("LINE");
        //    if (s instanceof Rectangle) System.out.println("RECTANGLE");
        //    if (s instanceof Circle) System.out.println("CIRCLE");
        //}
    }
}
