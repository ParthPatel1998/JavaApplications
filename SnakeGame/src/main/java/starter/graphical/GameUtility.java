package starter.graphical;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class GameUtility {

    static void setScene(Stage stage, GameVariables.SCENES scene) {
        switch(scene) {
            case SCENE1:
                stage.setScene(GameVariables.splashScreen);
                break;
            case SCENE2:
                stage.setScene(GameVariables.firstLevel);
                break;
            case SCENE3:
                stage.setScene(GameVariables.secondLevel);
                break;
            case SCENE4:
                stage.setScene(GameVariables.thirdLevel);
                break;
            case SCENE5:
                stage.setScene(GameVariables.gameOverScreen);
                break;
        }
    }

    static void StartPlayAgain(Stage stage, int level) {
        GameVariables.inPlay = true;
        GameVariables.dir = GameVariables.DIR.RIGHT;
        GameVariables.dx = 25;
        if (level == 1) {
            GameVariables.currentLevel = 1;
            SetFruitVisiblity(1);
        }
        else if (level == 2) {
            GameVariables.currentLevel = 2;
            SetFruitVisiblity(2);
        }
        else if (level == 3) {
            GameVariables.currentLevel = 3;
            SetFruitVisiblity(3);
        }
        setScene(stage, GameVariables.SCENES.SCENE2);
        GameVariables.animateTimer.start();
    }

    static void StopPlay(Stage stage, Pane pane, Snake snake, GameVariables.SCENES sceneNumber){
        GameVariables.dx = 0;
        GameVariables.dy = 0;
        GameVariables.finalScore = GameVariables.points;
        GameVariables.points = 0;
        GameVariables.inPlay = false;
        GameVariables.currentTime = 30;
        snake.SnakeReset(pane);
        GameVariables.animateTimer.stop();
        UpdateFinalScoreBoard();
        GameUtility.setScene(stage, sceneNumber);
    }

    static void UpdateFinalScoreBoard() {
        if (GameVariables.finalScore > GameVariables.highScore) GameVariables.highScore = GameVariables.finalScore;
        String timeS4 = "Score: " + String.valueOf(GameVariables.finalScore);
        GameVariables.gameOverScore.setText(timeS4);
        String timeS5 = "HighScore: " + String.valueOf(GameVariables.highScore);
        GameVariables.gameOverHighScore.setText(timeS5);
    }

    static void DrawFruit(Pane pane){
        if (GameVariables.fruits == null) return;
        for (int i = 0; i < GameVariables.fruits.size(); i++) {
            if (i > 4) GameVariables.fruits.get(i).GetFruit().setVisible(false);
            pane.getChildren().add(GameVariables.fruits.get(i).GetFruit());
        }
    }

    static void RegenrateFruitAt(int x, int y, Pane pane) {
        for (Fruit f : GameVariables.fruits) {
            if ((f.GetFruitX() == x) && (f.GetFruitY() == y)) {
                Random random = new Random();
                int randomX = (random.nextInt(926) + 25)/25;
                int randomY = (random.nextInt(501) + 50)/25;
                int fruitX = randomX*25;
                int fruitY = randomY*25;
                f.GetFruit().setX(fruitX);
                f.GetFruit().setY(fruitY);
                f.SetFruitX(fruitX);
                f.SetFruitY(fruitY);
                break;
            }
        }
    }

    static void SetFruitVisiblity(int level) {
        for (Fruit f : GameVariables.fruits) {
            f.GetFruit().setVisible(true);
        }
        if (level < 3) {
            for (int i = 10; i < GameVariables.fruits.size(); i++) {
                GameVariables.fruits.get(i).GetFruit().setVisible(false);
            }
        }
        if (level < 2){
            for (int i = 5; i < GameVariables.fruits.size(); i++) {
                GameVariables.fruits.get(i).GetFruit().setVisible(false);
            }
        }
    }
    static void UpdateTimer(Text timerText) {
        String timeS = "Timer: " + GameVariables.currentTime;
        timerText.setText(timeS);
    }

    static void UpdateLevel(Text levelText) {
        String score = "Level: " + GameVariables.currentLevel;
        levelText.setText(score);
    }

    static void UpdateScore(Text timerText) {
        String score = "Score: " + GameVariables.points;
        timerText.setText(score);
    }
}
