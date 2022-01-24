import javafx.scene.paint.Paint;

import java.awt.*;
import java.io.Serializable;

public class SketchItSaveFormat implements Serializable {
    enum ShapeType {Line, Rectange, Circle};

    ShapeType shapeType;
    Double [] PolygonPoints;
    String strokeColor;
    String fillColor;

    SketchItSaveFormat(ShapeType shapeType, Double [] PolygonPoints, String strokeColor, String fillColor) {
        this.shapeType = shapeType;
        this.PolygonPoints = PolygonPoints;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }
}
