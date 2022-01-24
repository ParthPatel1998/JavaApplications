import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Vector;

public class Controller {

	Model model;
	Stage stage;

	Controller(Model model, Stage stage) {
		this.model = model;
		this.stage = stage;
	}

	public void ChangeToolBarButtons(Model.DrawingTool drawingTool) {
		model.SetDrawingTool(drawingTool);
	}

	public void ChangeLineWidth(Double lineThickness) {
		model.setStrokeThickness(lineThickness);
	}

	public void ChangeLineType(Double lineType) {
		model.setDashOffset(lineType);
	}

	public void ChangeLineColorChange(Color color) { model.setCurrentLineColor(color); }

	public void ChangeFillColorChange(Color color) {
		model.setCurrentFillColor(color);
	}

	Line line;
	Rectangle rectangletest;
	Circle circle;
	Polygon rectangle;
	double innitX = 0;
	double innitY = 0;

	double lstartX;
	double lstartY;
	double lendX;
	double lendY;
	Double [] rectpoints = new Double[8];

	public void DrawOnCanvas(Pane pane) {
		pane.setOnMousePressed(this::innitShape);
		pane.setOnMouseDragged(this::drawShape);
		pane.setOnMouseDragReleased(e -> {});
	}

	void innitShape(MouseEvent e) {
		double mouseX = e.getX();
		double mouseY = e.getY();
		innitX = mouseX;
		innitY = mouseY;

		if (model.getCurrentDrawTool() == Model.DrawingTool.Line) {
			line = new Line();
			line.setStartX(mouseX);
			line.setStartY(mouseY);
			line.setEndX(mouseX);
			line.setEndY(mouseY);
			line.setStroke(model.getCurrentLineColor());
			line.setStrokeWidth(model.getStrokeThickness());
			if (model.getDashOffset() > 0) line.getStrokeDashArray().addAll(model.getDashOffset());
			model.addShape(line);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Rectangle) {
			rectangle = new Polygon();
			Double [] points = new Double[] {mouseX, mouseY, mouseX, mouseY, mouseX, mouseY, mouseX, mouseY};
			rectangle.getPoints().addAll(points);
			rectangle.setFill(model.getCurrentFillColor());
			rectangle.setStroke(model.getCurrentLineColor());
			rectangle.setStroke(model.getCurrentLineColor());
			rectangle.setStrokeWidth(model.getStrokeThickness());
			model.addShape(rectangle);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Circle) {
			circle = new Circle();
			circle.setCenterX(mouseX);
			circle.setCenterY(mouseY);
			circle.setRadius(0);
			circle.setFill(model.getCurrentFillColor());
			circle.setStroke(model.getCurrentLineColor());
			circle.setStroke(model.getCurrentLineColor());
			circle.setStrokeWidth(model.getStrokeThickness());
			model.addShape(circle);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Select) {
			rectangletest = new Rectangle(10,10,100,100);
			Translate translate = new Translate();
			translate.setX(100);
			rectangletest.getTransforms().add(translate);
			rectangletest.setFill(Color.BLACK);
			//model.addShape(rectangle);
			//if (rectangle.getTransforms()..contains(new Point2D(e.getX(), e.getY()))) System.out.println("adasd");

			Shape s = model.hitTestShape(mouseX, mouseY);
			model.setCurrentShape(s);
			innitX = e.getX();
			innitY = e.getY();
			if (s instanceof Line) {
				Line l = (Line) s;
				lstartX = l.getStartX();
				lstartY = l.getStartY();
				lendX = l.getEndX();
				lendY = l.getEndY();
			} else if (s instanceof Polygon) {
				Polygon p = (Polygon) s;
				for (int i = 0; i < p.getPoints().size(); i++) {
					rectpoints[i] = p.getPoints().get(i);
				}
			} else if (s instanceof Circle) {
				Circle c = (Circle) s;
				lstartX = c.getCenterX();
				lstartY = c.getCenterY();
			}
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Eraser) {
			Shape s = model.hitTestShape(mouseX, mouseY);
			if (s != null) {
				model.removeShape(s);
			}
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Fill) {
			Shape s = model.hitTestShape(mouseX, mouseY);
			if (s != null) s.setFill(model.getCurrentFillColor());
		}
	}

	void drawShape(MouseEvent e) {
		double endX = e.getX();
		double endY = e.getY();
		if (model.getCurrentDrawTool() == Model.DrawingTool.Line) {
			line.setEndX(endX);
			line.setEndY(endY);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Rectangle) {
			Double [] points = new Double[] {innitX, innitY, innitX, endY, endX, endY, endX, innitY};
			rectangle.getPoints().clear();
			rectangle.getPoints().addAll(points);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Circle) {
			double midpointX = (innitX + endX)/2;
			double midpointY = (innitY + endY)/2;
			double deltaX = Math.abs(midpointX - endX);
			double deltaY = Math.abs(midpointY - endY);
			double radius = Math.sqrt((deltaY * deltaY) + (deltaX * deltaX));
			circle.setCenterX(midpointX);
			circle.setCenterY(midpointY);
			circle.setRadius(radius);
		} else if (model.getCurrentDrawTool() == Model.DrawingTool.Select) {
			Shape s = model.getCurrentShape();
			if (s != null) {
				if (s instanceof Line) {
					Line l = (Line) s;
					double deltaX = endX - innitX;
					double deltaY = endY - innitY;
					l.setStartX(lstartX + deltaX);
					l.setStartY(lstartY + deltaY);
					l.setEndX(lendX + deltaX);
					l.setEndY(lendY + deltaY);
				} else if (s instanceof Polygon) {
					Polygon p = (Polygon) s;
					double deltaX = endX - innitX;
					double deltaY = endY - innitY;
					Double [] rectpoints2 = new Double[8];
					for(int i = 0; i < rectpoints.length; i++) {
						if (i % 2 == 0) rectpoints2[i] = rectpoints[i] + deltaX;
						else rectpoints2[i] = rectpoints[i] + deltaY;
					}
					p.getPoints().clear();
					p.getPoints().addAll(rectpoints2);
				} else if (s instanceof Circle) {
					double deltaX = endX - innitX;
					double deltaY = endY - innitY;
					Circle c = (Circle) s;
					c.setCenterX(lstartX + deltaX);
					c.setCenterY(lstartY + deltaY);
					//s.setTranslateX(endX - rectX);
					//s.setTranslateY(endY - rectY);
				}
			}
		}
	}

	public void SaveDrawing(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				SaveFile();
			}
		});
	}

	public void LoadDrawing(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				LoadFile();
			}
		});
	}

	public void AskSavePrompt(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				Label label = new Label("Do you want to save the current Drawing?");
				VBox dialogVbox = new VBox(label);
				VBox.setMargin(label, new Insets(10.0));
				Button buttonY = new Button("Yes");
				Button buttonN = new Button("No");

				buttonN.setOnMouseClicked(e -> {
					LoadFile();
					dialog.close();
				});

				buttonY.setOnMouseClicked(e -> {
					SaveFile();
					LoadFile();
					dialog.close();
				});

				HBox hbox = new HBox();
				hbox.getChildren().add(buttonY);
				hbox.getChildren().add(buttonN);
				hbox.setAlignment(Pos.CENTER);
				dialogVbox.getChildren().add(hbox);
				Scene dialogScene = new Scene(dialogVbox, 300, 75);
				dialog.setScene(dialogScene);
				dialog.show();
			}
		});
	}

	public void AskSavePromptNew(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				Label label = new Label("Do you want to save the current Drawing?");
				VBox dialogVbox = new VBox(label);
				VBox.setMargin(label, new Insets(10.0));
				Button buttonY = new Button("Yes");
				Button buttonN = new Button("No");

				buttonN.setOnMouseClicked(e -> {
					model.SetDrawingTool(Model.DrawingTool.LoadFile);
					model.clearAllShapes();
					dialog.close();
				});

				buttonY.setOnMouseClicked(e -> {
					SaveFile();
					model.SetDrawingTool(Model.DrawingTool.LoadFile);
					model.clearAllShapes();

					dialog.close();
				});

				HBox hbox = new HBox();
				hbox.getChildren().add(buttonY);
				hbox.getChildren().add(buttonN);
				hbox.setAlignment(Pos.CENTER);
				dialogVbox.getChildren().add(hbox);
				Scene dialogScene = new Scene(dialogVbox, 300, 75);
				dialog.setScene(dialogScene);
				dialog.show();
			}
		});
	}

	public void AboutProgram(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				Label label = new Label("About");
				label.setFont(Font.font(15));
				VBox dialogVbox = new VBox(label);
				VBox.setMargin(label, new Insets(10.0));
				dialogVbox.getChildren().add(new Text(" SketchIt \n Parth Patel \n pp5patel"));
				Scene dialogScene = new Scene(dialogVbox, 200, 100);
				dialog.setScene(dialogScene);
				dialog.show();
			}
		});
	}

	public void QuitProgram(MenuItem item) {
		item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				Label label = new Label("Do you want to save the current Drawing?");
				VBox dialogVbox = new VBox(label);
				VBox.setMargin(label, new Insets(10.0));
				Button buttonY = new Button("Yes");
				Button buttonN = new Button("No");

				buttonN.setOnMouseClicked(e -> {
					dialog.close();
					System.exit(0);
				});

				buttonY.setOnMouseClicked(e -> {
					SaveFile();
					dialog.close();
					System.exit(0);
				});

				HBox hbox = new HBox();
				hbox.getChildren().add(buttonY);
				hbox.getChildren().add(buttonN);
				hbox.setAlignment(Pos.CENTER);
				dialogVbox.getChildren().add(hbox);
				Scene dialogScene = new Scene(dialogVbox, 300, 75);
				dialog.setScene(dialogScene);
				dialog.show();
			}
		});
	}

	public void SaveFile() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Drawing");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
			fileChooser.setInitialFileName("MyDrawing.ser");

			File file = fileChooser.showSaveDialog(stage);
			if (file == null) return;
			FileOutputStream myWriter = new FileOutputStream(file);
			ObjectOutputStream encoder = new ObjectOutputStream (myWriter);

			Vector<Shape> allShapes = model.getAllShapes();
			ArrayList<SketchItSaveFormat> saveData = new ArrayList<SketchItSaveFormat>();

			for (Shape s : allShapes) {
				if (s instanceof Polygon) {
					Polygon p = (Polygon) s;
					Double [] points = p.getPoints().toArray(new Double[0]);
					SketchItSaveFormat sf = new SketchItSaveFormat(
							SketchItSaveFormat.ShapeType.Rectange,
							points,
							p.getStroke().toString(),
							p.getFill().toString()
					);
					saveData.add(sf);
				}
				else if (s instanceof Circle) {
					Circle c = (Circle) s;
					Double [] points = new Double[4];
					points[0] = c.getCenterX();
					points[1] = c.getCenterY();
					points[2] = c.getRadius();
					points[3] = c.getRadius();
					SketchItSaveFormat sf = new SketchItSaveFormat(
							SketchItSaveFormat.ShapeType.Circle,
							points,
							c.getStroke().toString(),
							c.getFill().toString()
					);
					saveData.add(sf);
				}
				else if (s instanceof Line) {
					Line l = (Line) s;
					Double [] points = new Double[4];
					points[0] = l.getStartX();
					points[1] = l.getStartY();
					points[2] = l.getEndX();
					points[3] = l.getEndY();
					SketchItSaveFormat sf = new SketchItSaveFormat(
							SketchItSaveFormat.ShapeType.Line,
							points,
							l.getStroke().toString(),
							null
					);
					saveData.add(sf);
				}
			}

			encoder.writeObject(saveData);
			encoder.close();
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void LoadFile() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Load Drawing");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
			fileChooser.setInitialFileName("MyDrawing.ser");

			File file = fileChooser.showOpenDialog(stage);
			if (file == null) return;

			FileInputStream myWriter = new FileInputStream(file);
			ObjectInputStream decoder = new ObjectInputStream (myWriter);

			ArrayList<SketchItSaveFormat> loadData = (ArrayList<SketchItSaveFormat>) decoder.readObject();
			Vector<Shape> allShapes = new Vector<Shape>();

			for (SketchItSaveFormat sf : loadData) {
				if (sf.shapeType == SketchItSaveFormat.ShapeType.Rectange) {
					Polygon s = new Polygon();
					Double [] points = sf.PolygonPoints;
					s.getPoints().addAll(points);
					s.setStroke(Paint.valueOf(sf.strokeColor));
					s.setFill(Paint.valueOf(sf.fillColor));
					allShapes.add(s);
				}
				else if (sf.shapeType == SketchItSaveFormat.ShapeType.Circle) {
					Circle s = new Circle();
					Double [] points = sf.PolygonPoints;
					s.setCenterX(points[0]);
					s.setCenterY(points[1]);
					s.setRadius(points[2]);
					s.setStroke(Paint.valueOf(sf.strokeColor));
					s.setFill(Paint.valueOf(sf.fillColor));
					allShapes.add(s);
				} else if (sf.shapeType == SketchItSaveFormat.ShapeType.Line) {
					Line s = new Line();
					Double [] points = sf.PolygonPoints;
					s.setStartX(points[0]);
					s.setStartY(points[1]);
					s.setEndX(points[2]);
					s.setEndY(points[3]);
					s.setStroke(Paint.valueOf(sf.strokeColor));
					allShapes.add(s);
				}
			}

			model.SetDrawingTool(Model.DrawingTool.LoadFile);
			model.setAllShapes(allShapes);

			decoder.close();
			myWriter.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
