
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Model {

	enum DrawingTool {
		Select, Eraser, Line, Circle, Rectangle,
		Fill, LineColor, FillColor, LineWidth, LineType,
		LoadFile
	};

	// The Data in the model
	private DrawingTool currentDrawingTool = DrawingTool.Select;
	private Color currentLineColor = Color.BLACK;
	private Color currentFillColor = Color.TRANSPARENT;
	private Vector<Shape> shapes = new Vector<Shape>();
	private Shape currentShape;
	private Cursor cursor;
	private double dashOffset = 1.0;
	private double strokeThickness = 2.0;

	// all views of this model
	private ArrayList<IView> views = new ArrayList<IView>();
	
	// method that the views can use to register themselves with the Model
	// once added, they are told to update and get state from the Model
	public void addView(IView view) {
		views.add(view);
	}

	public void removeView(IView view) {
		// remove view here
	}

	// Getters
	// simple accessor method to fetch the current state
	public DrawingTool getCurrentDrawTool() {
		return currentDrawingTool;
	}
	public Shape getCurrentShape() {
		return currentShape;
	}
	public Vector<Shape> getAllShapes() {
		return shapes;
	}
	public Cursor getCursor() {
		return cursor;
	}
	public double getDashOffset() {
		return dashOffset;
	}
	public double getStrokeThickness() {
		return strokeThickness;
	}
	public Color getCurrentLineColor() {
		return currentLineColor;
	}
	public Color getCurrentFillColor() {
		return currentFillColor;
	}

	// Setters
	// Methods that the Controller uses to tell the Model to change state
	public void setCurrentShape(Shape shape) {
		this.currentShape = shape;
	}
	public void setCurrentLineColor(Color color) {
		this.currentLineColor = color;
	}
	public void setCurrentFillColor(Color color) {
		this.currentFillColor = color;
	}
	public void setDashOffset(double dashOffset) {
		this.dashOffset = dashOffset;
	}
	public void setStrokeThickness(double strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public void SetDrawingTool(DrawingTool currentDrawingTool) {
		if (currentDrawingTool == DrawingTool.Select) {
			cursor = Cursor.DEFAULT;
		}
		else if (currentDrawingTool == DrawingTool.Fill) {
			Image image = new Image("fill.png");
			cursor = new ImageCursor(image, image.getWidth() / 2, image.getHeight() /2);
		}
		else if (currentDrawingTool == DrawingTool.Line ||
				currentDrawingTool == DrawingTool.Circle ||
				currentDrawingTool == DrawingTool.Rectangle ) {
			cursor = Cursor.CROSSHAIR;
		}
		else if (currentDrawingTool == DrawingTool.Eraser) {
			Image image = new Image("erase.png");
			cursor = new ImageCursor(image, image.getWidth() / 2, image.getHeight() /2);
		}
		else {
			cursor = Cursor.DEFAULT;
		}
		this.currentDrawingTool = currentDrawingTool;
	}

	// Change
	public void addShape(Shape shape) {
		this.shapes.add(shape);
		this.currentShape = shape;
		notifyObservers();
	}

	public void removeShape(Shape shape) {
		this.currentShape = shape;
		this.shapes.remove(shape);
		notifyObservers();
	}

	public Shape hitTestShape(double pointX, double pointY) {
		for (int i = shapes.size() - 1; i >= 0; i-- ) {
			if (shapes.get(i).contains(pointX, pointY)) {
				System.out.println("hit");
				return shapes.get(i);
			}
		}
		return null;
	}

	public void setAllShapes(Vector<Shape> shapes){
		this.shapes = shapes;
		notifyObservers();
	}

	public void clearAllShapes(){
		this.shapes.clear();
		notifyObservers();
	}

	// the model uses this method to notify all of the Views that the data has changed
	// the expectation is that the Views will refresh themselves to display new data when appropriate
	private void notifyObservers() {
		for (IView view : this.views) {
			//System.out.println("Model: notify View");
			view.updateView();
		}
	}
}
