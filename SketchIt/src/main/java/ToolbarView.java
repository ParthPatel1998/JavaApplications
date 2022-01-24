import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.sound.sampled.Line;

class ToolbarView extends VBox implements IView {

	final double BUTTON_MIN_SIZE = 25;
	final double BUTTON_PREF_SIZE = 65;
	final double BUTTON_MAX_SIZE = 100;

	private TextArea text = new TextArea("");
	private Model model;

	ToolbarView(Model model, Controller controller) {
		this.model = model;

		// set label properties
		Image rect = new Image("recticon.png", 35, 35, false, false);
		Image circle = new Image("circle.png", 25, 25, false, true);
		Image fill = new Image("fill.png", 25, 25, false, false);
		Image line = new Image("line.png", 25, 25, false, false);
		Image erase = new Image("erase.png", 25, 25, false, false);
		Image select = new Image("select.png", 25, 25, true, true);

		Button button1 = new StandardButton(select);
		Button button2 = new StandardButton(erase);
		Button button3 = new StandardButton(line);
		Button button4 = new StandardButton(circle);
		Button button5 = new StandardButton(rect);
		Button button6 = new StandardButton(fill);

		GridPane shapeTools = new GridPane();

		shapeTools.add(button1, 0, 0);
		shapeTools.add(button2, 1, 0);
		shapeTools.add(button3, 0, 1);
		shapeTools.add(button4, 1, 1);
		shapeTools.add(button5, 0, 2);
		shapeTools.add(button6, 1, 2);
		shapeTools.setHgap(10);
		shapeTools.setVgap(15);
		shapeTools.setPadding(new Insets(25,10,25,10));
		shapeTools.setStyle("-fx-border-color: black");

		//Event is passed to controller
		button1.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Select);});
		button2.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Eraser);});
		button3.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Line);});
		button4.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Circle);});
		button5.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Rectangle);});
		button6.setOnMouseClicked(e -> {controller.ChangeToolBarButtons(Model.DrawingTool.Fill);});

		final ColorPicker colorPickerLine = new ColorPicker();
		colorPickerLine.setPrefSize(65, 65);
		colorPickerLine.setValue(Color.BLACK);
		final ColorPicker colorPickerFill = new ColorPicker();
		colorPickerFill.setPrefSize(65, 65);
		colorPickerFill.setValue(Color.TRANSPARENT);
		colorPickerLine.setStyle("-fx-color-rect-width: 35px; -fx-color-rect-height: 35px");
		colorPickerFill.setStyle("-fx-color-rect-width: 35px; -fx-color-rect-height: 35px");

		colorPickerLine.setOnAction(e -> {controller.ChangeLineColorChange(colorPickerLine.getValue());});
		colorPickerFill.setOnAction(e -> {controller.ChangeFillColorChange(colorPickerFill.getValue());});

		GridPane colorPickerTools = new GridPane();
		colorPickerTools.add(colorPickerLine, 0, 0);
		colorPickerTools.add(colorPickerFill, 1, 0);
		colorPickerTools.setHgap(10);
		colorPickerTools.setVgap(10);
		colorPickerTools.setPadding(new Insets(25,10,25,10));
		colorPickerTools.setStyle("-fx-border-color: #000000");

		Image image2 = new Image("line.png", 25, 25, true, false);
		Button button33 = new LineTypeStandardButton(image2);
		Button button43 = new LineTypeStandardButton(image2);
		Button button53 = new LineTypeStandardButton(image2);
		Button button63 = new LineTypeStandardButton(image2);
		Button button73 = new LineTypeStandardButton(image2);
		Button button83 = new LineTypeStandardButton(image2);
		Button button93 = new LineTypeStandardButton(image2);
		Button button103 = new LineTypeStandardButton(image2);

		//Event is passed to controller
		button33.setOnMouseClicked(e -> {controller.ChangeLineWidth( 2.0);});
		button43.setOnMouseClicked(e -> {controller.ChangeLineWidth(3.0);});
		button53.setOnMouseClicked(e -> {controller.ChangeLineWidth(5.0);});
		button63.setOnMouseClicked(e -> {controller.ChangeLineWidth(10.0);});
		button73.setOnMouseClicked(e -> {controller.ChangeLineType(1.0);});
		button83.setOnMouseClicked(e -> {controller.ChangeLineType(20.0);});
		button93.setOnMouseClicked(e -> {controller.ChangeLineType(30.0);});
		button103.setOnMouseClicked(e -> {controller.ChangeLineType(50.0);});

		GridPane LineSettingTools = new GridPane();
		LineSettingTools.add(button33, 0, 0);
		LineSettingTools.add(button43, 1, 0);
		LineSettingTools.add(button53, 2, 0);
		LineSettingTools.add(button63, 3, 0);
		LineSettingTools.add(button73, 0, 1);
		LineSettingTools.add(button83, 1, 1);
		LineSettingTools.add(button93, 2, 1);
		LineSettingTools.add(button103, 3, 1);

		LineSettingTools.setHgap(2);
		LineSettingTools.setVgap(5);
		LineSettingTools.setPadding(new Insets(25,10,25,10));
		LineSettingTools.setStyle("-fx-border-color: black");

		this.getChildren().add(shapeTools);
		this.getChildren().add(colorPickerTools);
		this.getChildren().add(LineSettingTools);

		//this.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setSpacing(10);
		this.setPadding(new Insets(2,2,2,2));
		this.setStyle("-fx-border-color: black");
		this.setPrefWidth(175);
		this.setMaxWidth(200);
		this.setMinWidth(100);

		// register the controller as a handler for this view
		//text.addEventHandler(MouseEvent.MOUSE_CLICKED, controller);
		// add label widget to the pane
		//this.getChildren().add(text);

		// register with the model when we're ready to start receiving data
		model.addView(this);
	}

	// When notified by the model that things have changed,
	// update to display the new value
	public void updateView() {
	}

	private class StandardButton extends Button {
		StandardButton(Image image) {
			// setText(caption); // call to super class already does this
			setVisible(true);
			setMinSize(BUTTON_MIN_SIZE, BUTTON_MIN_SIZE);
			setPrefSize(BUTTON_PREF_SIZE, BUTTON_PREF_SIZE);
			setMaxSize(BUTTON_MAX_SIZE, BUTTON_MAX_SIZE);
			setGraphic(new ImageView(image));
			//setPadding(new Insets(10));
		}
	}

	private class LineTypeStandardButton extends Button {
		LineTypeStandardButton(Image image) {
			// setText(caption); // call to super class already does this
			setVisible(true);
			setMinSize(12, 12);
			setPrefSize(35, 35);
			setMaxSize(35, 35);
			setGraphic(new ImageView(image));
			//setPadding(new Insets(10));
		}
	}
}
