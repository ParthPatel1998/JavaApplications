package starter.graphical;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.util.*;

import static java.lang.Math.nextUp;
import static java.lang.Math.random;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // scene one
        Pane root1 = new Pane();
        GameVariables.splashScreen = new Scene(root1, 975, 600);
        LayoutSetup.LayoutSetupSplashScreen(stage, root1);
        AllEventsHandle.SetAllEvents(stage, GameVariables.splashScreen);

        // scene two
        Pane root2 = new Pane();
        GameVariables.firstLevel = new Scene(root2, 975, 600);
        LayoutSetup.LayoutSetupPlayScreen(root2);
        AllEventsHandle.SetAllEventsPlay(stage, GameVariables.firstLevel);

        // Timer
        GameVariables.timeTimer = new Timer();
        TimerTask Task1 = new TimerTask() {
            public void run() {
                if (GameVariables.inPlay) GameVariables.currentTime--;
            }
        };
        GameVariables.timeTimer.schedule(Task1, 0, 1000);

        Text timerText = LayoutSetup.AddTimerText(root2);
        Text scoreText = LayoutSetup.AddScoreText(root2);
        Text levelText = LayoutSetup.AddLevelext(root2);
        Vector<Text> gameTexts = new Vector<Text>();
        gameTexts.add(scoreText);
        gameTexts.add(levelText);
        gameTexts.add(timerText);
        Snake snake = new Snake();

        GameVariables.animateTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= (100_000_000/GameVariables.currentLevel)) {  //15_000_0000
                    HandleAnimation(stage, root2, snake, gameTexts, now, lastUpdate);
                    lastUpdate = now;
                }
            }
        };

        //GameVariables.animtetimer.start();
        GameUtility.DrawFruit(root2);
        snake.DrawSnake(root2);
        root2.getChildren().addAll(gameTexts);

        // scene five
        Pane root3 = new Pane();
        GameVariables.gameOverScreen = new Scene(root3, 975, 600);
        LayoutSetup.LayoutSetupGameOverScreen(stage, root3);
        AllEventsHandle.SetAllEvents(stage, GameVariables.gameOverScreen);

        // Stage
        stage.setTitle("EatFruit Game");
        stage.setScene(GameVariables.splashScreen);
        stage.setResizable(false);
        stage.show();
        System.out.println("Start");
    }

    @Override
    public void stop() throws Exception {
        GameVariables.timeTimer.cancel();
    }

    void HandleAnimation(Stage stage, Pane pane, Snake snake, Vector<Text> gameTexts, long now, long lastupdate) {
        if (GameVariables.currentTime == 0) {
            GameVariables.currentLevel++;
            GameVariables.currentTime = 30;
            if (GameVariables.currentLevel == 2) {
                GameUtility.SetFruitVisiblity(GameVariables.currentLevel);
            }
            if (GameVariables.currentLevel == 3) {
                GameUtility.SetFruitVisiblity(GameVariables.currentLevel);
                pane.getChildren().remove(gameTexts.get(2));
            }
        }
        if (GameVariables.currentLevel == 3) {
            pane.getChildren().remove(gameTexts.get(2));
        } else if (!pane.getChildren().contains(gameTexts.get(2))) {
            pane.getChildren().add(gameTexts.get(2));
        }
        if (GameVariables.inPlay) {
            moveSnake(stage, snake, pane);
            GameUtility.UpdateLevel(gameTexts.get(1));
            GameUtility.UpdateTimer(gameTexts.get(2));
            GameUtility.UpdateScore(gameTexts.get(0));
        } else {
            if (GameVariables.quitGame) {
                // Q is pressed
                GameUtility.StopPlay(stage, pane, snake, GameVariables.SCENES.SCENE5);
            } else {
                // R is pressed
                GameUtility.StopPlay(stage, pane, snake, GameVariables.SCENES.SCENE1);
            }
        }
    }

    void moveSnake(Stage stage, Snake snake, Pane pane) {
        boolean hitBorder = ((snake.GetFrontX() + 25 > GameVariables.right_border) ||
                (snake.GetFrontX() < GameVariables.left_border) ||
                (snake.GetFrontY() + 25 > GameVariables.bottom_border) ||
                (snake.GetFrontY() < GameVariables.top_border));

        if (hitBorder || snake.SnakeCollide()) {
            GameUtility.StopPlay(stage, pane, snake, GameVariables.SCENES.SCENE5);
            return;
        }
        if (snake.SnakeEatsFruit()) {
            GameVariables.points++;
            GameUtility.RegenrateFruitAt(snake.GetFrontX(), snake.GetFrontY(), pane);
            snake.SnakeGetsBigger(pane);
        }
        snake.MoveSnake(GameVariables.dx, GameVariables.dy);
    }
}