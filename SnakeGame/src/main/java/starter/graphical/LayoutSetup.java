package starter.graphical;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class LayoutSetup {
    static Image image = new Image("bg3.jpg", 976, 600, false, true);
    static BackgroundImage myBI= new BackgroundImage(image,
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    static void AddBackGround(Pane pane) {
        pane.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBackground(new Background(myBI));
    }

    static void AddSplashScreenText(Pane pane) {
        Text Intro = new Text();
        Intro.setFont(Font.font("Tahoma", FontPosture.REGULAR, 35));
        Intro.setX(50);
        Intro.setY(100);
        Intro.setFill(Color.WHITE);
        String timeS = "PARTH PATEL \n pp5patel \n";
        Intro.setText(timeS);

        Text Intro1 = new Text();
        Intro1.setFont(Font.font("Tahoma", FontPosture.REGULAR, 60));
        Intro1.setX(50);
        Intro1.setY(100);
        Intro1.setFill(Color.WHITE);
        String timeS1 = "EatFruit";
        Intro1.setText(timeS1);

        HBox hbox = new HBox(Intro1, Intro);
        HBox.setMargin(Intro, new Insets(50, 250, 50, 250));
        HBox.setMargin(Intro1, new Insets(50, 50, 50, 50));
        pane.getChildren().add(hbox);

        Text Intro2 = new Text();
        Intro2.setFont(Font.font("Tahoma", FontPosture.REGULAR, 15));
        Intro2.setX(50);
        Intro2.setY(200);
        Intro2.setFill(Color.WHITE);
        String timeS2 = "How to Play: \n" +
                "1. Press Play game or 1 to start. \n" +
                "2. Press R to return to SPLASH screen. \n" +
                "3. Press Q to Quit and go to the Game Over screen. \n" +
                "4. Press Arrow keys to move. \n" +
                "5. Press P to Pause and Unpause.\n" +
                "6. The game screen displays a snake, which is always in motion, and fruit randomly positioned " +
                "around the screen.\n" +
                "7. The direction of the snake can be controlled by the " +
                "arrows keys. The snake will move forward " +
                "unless LEFT or RIGHT \n    arrow keys are pressed, " +
                "which will cause the snake to turn in that direction " +
                "(relative to it's current path).\n" +
                "8. The objective of the snake is to eat the fruit. When the snake eats a piece of the fruit" +
                " disappears and is immediately replaced by \n   " +
                " another piece of fruit randomly po" +
                "sitioned. Every time the snake eats a piece of fruit, it gets one block longer.\n" +
                "9. A timer ticks down on each level. When the timer runs out, the next level is loaded, " +
                "with increasingly more fruit. \n" +
                "10. The snake can die by eating itself (when it collides with itself) or by hitting the" +
                " edge of the screen.";
        Intro2.setText(timeS2);
        pane.getChildren().add(Intro2);
    }

    static void AddGameOverText(Pane pane) {
        Text gameOver = new Text();
        gameOver.setFont(Font.font("Tahoma", FontPosture.REGULAR, 60));
        gameOver.setX(225);
        gameOver.setY(300);
        gameOver.setFill(Color.WHITE);
        String timeS3 = "GAME OVER";
        gameOver.setText(timeS3);
        pane.getChildren().add(gameOver);
    }

    static void AddGameOverScore(Pane pane) {
        GameVariables.gameOverScore = new Text();
        GameVariables.gameOverScore.setFont(Font.font("Tahoma", FontPosture.REGULAR, 35));
        GameVariables.gameOverScore.setX(225);
        GameVariables.gameOverScore.setY(400);
        GameVariables.gameOverScore.setFill(Color.WHITE);
        String timeS4 = "Score: " + String.valueOf(GameVariables.finalScore);
        GameVariables.gameOverScore.setText(timeS4);
        pane.getChildren().add(GameVariables.gameOverScore);

        GameVariables.gameOverHighScore = new Text();
        GameVariables.gameOverHighScore.setFont(Font.font("Tahoma", FontPosture.REGULAR, 40));
        GameVariables.gameOverHighScore.setX(225);
        GameVariables.gameOverHighScore.setY(350);
        GameVariables.gameOverHighScore.setFill(Color.WHITE);
        String timeS5 = "HighScore: " + String.valueOf(GameVariables.highScore);
        GameVariables.gameOverHighScore.setText(timeS5);
        pane.getChildren().add(GameVariables.gameOverHighScore);
    }


    static void AddSplashScreenButton(Stage stage, Pane pane, String displayText) {
        Image img = new Image("button.png", 100, 50, true, true);
        ImageView view = new ImageView(img);
        Text text = new Text(displayText);
        text.setFont(Font.font("Tahoma", FontPosture.REGULAR, 35));
        Button button = new Button(displayText);
        button.setTranslateX(450);
        button.setTranslateY(450);
        button.setMinSize(250, 75);
        button.setMaxSize(250, 75);
        button.setStyle("-fx-font-size:25");
        button.setGraphic(view);
        button.setOnAction(event -> {
            //GameVariables.game_over = false;
            GameUtility.StartPlayAgain(stage, 1);
        });
        pane.getChildren().add(button);
    }

    // Draws the Playing levels
    static void DrawPlayGrid(Pane pane) {
        int _x = 25;
        int _y = 50;
        int gridBoxSize = 25;
        int gridBoxesAmountX = 37;
        int gridBoxesAmountY = 21;
        Rectangle rectangle = new Rectangle(925, 525);
        Color c = Color.web("0x8ECC39",1.0);

        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);
        rectangle.setFill(c);
        rectangle.setX(_x);
        rectangle.setY(_y);
        pane.getChildren().add(rectangle);

        for (int i = 0; i < gridBoxesAmountY; i++) {
            for (int  j = 0; j < gridBoxesAmountX; j++) {
                Rectangle gridBox = new Rectangle(gridBoxSize, gridBoxSize);
                c = Color.web("0x8ECC39",1.0);
                if ((j + i) % 2 == 0) c = Color.web("0xA7D948",1.0);
                gridBox.setFill(c);
                gridBox.setX(_x + (j * gridBoxSize));
                gridBox.setY(_y + (i * gridBoxSize));
                pane.getChildren().add(gridBox);
            }
        }
    }

    static void GenerateInnitialFruitList() {
        int _x = 25;
        int _y = 50;
        int gridBoxSize = 25;
        int gridBoxexAmountX = 36;
        int gridBoxexAmountY = 20;
        Image image = new Image("apple2.png", 25, 25, true, true);
        List<Integer> fruitPos = new Vector<Integer>();
        fruitPos.addAll(Arrays.asList(1, 1, 1, gridBoxexAmountY-1 ,gridBoxexAmountX-1, gridBoxexAmountY-1,
                gridBoxexAmountX-1, 1, gridBoxexAmountX/2, gridBoxexAmountY/2));
        //GameVariables.fruits = new Vector<Fruit>();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < fruitPos.size(); i += 2) {
                int fruitX = _x + (gridBoxSize * fruitPos.get(i));
                int fruitY = _y + (gridBoxSize * fruitPos.get(i + 1));
                if (j == 1) fruitX = _x + (gridBoxSize * (fruitPos.get(i) + j));
                else if (j == 2) fruitX = _x + (gridBoxSize * (fruitPos.get(i) - 1));
                //imgView.relocate(fruitX, fruitY);
                ImageView imgView = new ImageView(image);
                imgView.setX(fruitX);
                imgView.setY(fruitY);
                Fruit f = new Fruit(fruitX, fruitY, imgView);
                GameVariables.fruits.add(f);
            }
        }
    }

    static Text AddTimerText(Pane pane) {
        Text timerText = new Text();
        Font font = Font.loadFont("file:src/main/resources/digital-7.ttf", 35);
        timerText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 35));
        timerText.setFont(font);
        timerText.setX(700);
        timerText.setY(35);
        timerText.setFill(Color.WHITE);
        String timeS = "Timer: " + GameVariables.currentTime;
        timerText.setText(timeS);
        return timerText;
    }

    static Text AddScoreText(Pane pane) {
        Text scoreText = new Text();
        Font font = Font.loadFont("file:src/main/resources/digital-7.ttf", 35);
        scoreText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 35));
        scoreText.setFont(font);
        scoreText.setX(100);
        scoreText.setY(35);
        scoreText.setFill(Color.WHITE);
        String timeS = "Score: " + GameVariables.points;
        scoreText.setText(timeS);
        return scoreText;
    }

    static Text AddLevelext(Pane pane) {
        Text levelText = new Text();
        Font font = Font.loadFont("file:src/main/resources/digital-7.ttf", 35);
        levelText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 35));
        levelText.setFont(font);
        levelText.setX(450);
        levelText.setY(35);
        levelText.setFill(Color.WHITE);
        String timeS = "Level: " + GameVariables.currentLevel;
        levelText.setText(timeS);
        return levelText;
    }

    static void LayoutSetupSplashScreen(Stage stage, Pane pane) {
        AddBackGround(pane);
        AddSplashScreenText(pane);
        AddSplashScreenButton(stage, pane, "Play Game");
    }

    static void LayoutSetupPlayScreen(Pane pane) {
        AddBackGround(pane);
        DrawPlayGrid(pane);
        GenerateInnitialFruitList();
    }

    static void LayoutSetupGameOverScreen(Stage stage, Pane pane) {
        AddBackGround(pane);
        AddGameOverText(pane);
        AddSplashScreenButton(stage, pane, "Play Again");
        AddGameOverScore(pane);
    }
}
