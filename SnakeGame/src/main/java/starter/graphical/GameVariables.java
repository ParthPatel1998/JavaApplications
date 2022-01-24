package starter.graphical;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.Vector;

public class GameVariables {
    enum SCENES {SCENE1, SCENE2, SCENE3, SCENE4,SCENE5};
    enum DIR {UP, DOWN, RIGHT, LEFT};
    static Scene splashScreen, firstLevel, secondLevel, thirdLevel, gameOverScreen;
    static AnimationTimer animateTimer;
    static Timer timeTimer;
    static Vector<Fruit> fruits = new Vector<Fruit>();
    static Text gameOverScore;
    static Text gameOverHighScore;
    static int i = 10;
    static int right_border = 950;
    static int left_border = 25;
    static int top_border = 50;
    static int bottom_border = 575;
    static boolean inPlay = false;
    static boolean quitGame = false;
    static int currentLevel = 1;
    static int points = 0;
    static  int finalScore = 0;
    static int highScore = 0;
    static boolean game_over = true;
    static int dx = 0, dy = 0;
    static DIR dir = DIR.RIGHT;
    static boolean pause = false;
    static int currentTime = 30;
}
