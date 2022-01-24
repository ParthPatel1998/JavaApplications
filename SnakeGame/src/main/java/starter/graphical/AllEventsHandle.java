package starter.graphical;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AllEventsHandle {
    static void inc() {
        GameVariables.i += 10;
    }
    static void SetAllEvents(Stage stage, Scene scene) {
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DIGIT1) {
                GameUtility.StartPlayAgain(stage,1);
            }
            else if (event.getCode() == KeyCode.DIGIT2) {
                GameUtility.StartPlayAgain(stage,2);
            }
            else if (event.getCode() == KeyCode.DIGIT3) {
                GameUtility.StartPlayAgain(stage,3);
            }
            else if (event.getCode() == KeyCode.R) {
                GameUtility.setScene(stage, GameVariables.SCENES.SCENE1);
            }
            else if (event.getCode() == KeyCode.Q) {
                GameUtility.setScene(stage, GameVariables.SCENES.SCENE5);
            }
        });
    }

    static void SetAllEventsPlay(Stage stage, Scene scene) {
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DIGIT1) {
                GameVariables.currentLevel = 1;
                GameVariables.currentTime = 30;
                GameUtility.SetFruitVisiblity(1);
            }
            else if (event.getCode() == KeyCode.DIGIT2) {
                GameVariables.currentLevel = 2;
                GameVariables.currentTime = 30;
                GameUtility.SetFruitVisiblity(2);
            }
            else if (event.getCode() == KeyCode.DIGIT3) {
                GameVariables.currentLevel = 3;
                GameVariables.currentTime = 30;
                GameUtility.SetFruitVisiblity(3);
            }
            else if (event.getCode() == KeyCode.R) {
                GameVariables.inPlay = false;
                GameVariables.quitGame = false;
                GameUtility.setScene(stage, GameVariables.SCENES.SCENE1);
            }
            else if (event.getCode() == KeyCode.Q) {
                GameVariables.inPlay = false;
                GameVariables.quitGame = true;
                GameUtility.setScene(stage, GameVariables.SCENES.SCENE5);
            }
            else if (event.getCode() == KeyCode.P) {
                if (!GameVariables.pause) {
                    GameVariables.animateTimer.stop();
                    GameVariables.pause = true;
                } else {
                    GameVariables.animateTimer.start();
                    GameVariables.pause = false;
                }
            }
            else if ((event.getCode() == KeyCode.UP) && (GameVariables.dir != GameVariables.DIR.DOWN)) {
                GameVariables.dy = -25;
                GameVariables.dx = 0;
                GameVariables.dir = GameVariables.DIR.UP;
            }
            else if ((event.getCode() == KeyCode.DOWN) && (GameVariables.dir != GameVariables.DIR.UP)) {
                GameVariables.dy = 25;
                GameVariables.dx = 0;
                GameVariables.dir = GameVariables.DIR.DOWN;
            }
            else if ((event.getCode() == KeyCode.RIGHT) && (GameVariables.dir != GameVariables.DIR.LEFT)) {
                GameVariables.dy = 0;
                GameVariables.dx = 25;
                GameVariables.dir = GameVariables.DIR.RIGHT;
            }
            else if ((event.getCode() == KeyCode.LEFT) && (GameVariables.dir != GameVariables.DIR.RIGHT)) {
                GameVariables.dy = 0;
                GameVariables.dx = -25;
                GameVariables.dir = GameVariables.DIR.LEFT;
            }
        });
    }

}
