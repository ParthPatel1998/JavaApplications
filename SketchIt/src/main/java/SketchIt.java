import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SketchIt extends Application {

    static double WINDOW_WIDTH = 1150;
    static double WINDOW_HEIGHT = 625;

    @Override
    public void start(Stage stage) throws Exception {
        // create and initialize the Model to hold our Data
        Model model = new Model();

        // create the Controller, and tell it about Model
        // the controller will handle input and pass requests to the model
        Controller controller = new Controller(model, stage);

        // create each view, and tell them about model and controller
        // the views will register themselves with these components
        // and handle displaying the data from the model
        // All Components
        MenuBar menuBar = new MenuBarView(model, controller);
        VBox toolbar = new ToolbarView(model, controller);
        Pane canvas = new CanvasView(model, controller);

        // Setup Main Window
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setTop(menuBar);
        root.setLeft(toolbar);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane.setMargin(toolbar, new Insets(2));
        BorderPane.setMargin(canvas, new Insets(5));

        canvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                stage.getScene().setCursor(model.getCursor());
            }
        });
        canvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                stage.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("SketchIt");
        stage.setScene(scene);
        stage.show();
        System.out.println("Start");
    }
}

