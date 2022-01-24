import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

class CanvasView extends Pane implements IView {
	private Button button = new Button("?");
	private Model model; // reference to the model

	final double BUTTON_MIN_SIZE = 25;
	final double CANVAS_PREF_HEIGHT = 550;
	final double CANVAS_PREF_WIDTH = 950;
	final double BUTTON_MAX_ = 100;

	CanvasView(Model model, Controller controller) {
		// keep track of the model
		this.model = model;
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setBorder(Utility.createBorder());

		this.setPrefSize(CANVAS_PREF_WIDTH, CANVAS_PREF_HEIGHT);
		this.setMaxSize(CANVAS_PREF_WIDTH + 300, CANVAS_PREF_HEIGHT + 300);
		this.setMinSize(CANVAS_PREF_WIDTH - 450, CANVAS_PREF_HEIGHT - 450);

		Utility.clipChildren(this);

		// register the controller as a handler for this view
		controller.DrawOnCanvas(this);

		// register with the model when we're ready to start receiving data
		model.addView(this);
	}

	// When notified by the model that things have changed,
	// update to display the new value
	public void updateView() {
		System.out.println("View: updateView");
		Model.DrawingTool currentTool = model.getCurrentDrawTool();

		if (currentTool == Model.DrawingTool.LoadFile) {
			this.getChildren().clear();
			this.getChildren().addAll(model.getAllShapes());
			model.SetDrawingTool(Model.DrawingTool.Select);
		}
		else if (currentTool == Model.DrawingTool.Eraser) {
			if (this.getChildren().contains(model.getCurrentShape())) {
				this.getChildren().remove(model.getCurrentShape());
			}
		}
		else if (currentTool == Model.DrawingTool.Select) {
			this.getChildren().add(model.getCurrentShape());

		}
		else if (currentTool == Model.DrawingTool.Fill) {
		}
		else {
			if (!this.getChildren().contains(model.getCurrentShape())) {
				this.getChildren().add(model.getCurrentShape());
			}
		}
	}
}
