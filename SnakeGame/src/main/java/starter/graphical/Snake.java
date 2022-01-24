package starter.graphical;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.List;
import java.util.Vector;

public class Snake {
    private Vector<Rectangle> snakeList;

    public Snake() {
        int snakeHeadX = 225;
        int snakeHeadY = 100;
        int gridBoxSize = 25;
        Color c = Color.web("0x4674EB",1.0);
        snakeList = new Vector<Rectangle>();

        Rectangle snakeHeadRec = new Rectangle(25, 25);
        snakeHeadRec.setFill(c);
        snakeHeadRec.setX(snakeHeadX);
        snakeHeadRec.setY(snakeHeadY);
        snakeHeadRec.setStroke(c);
        snakeList.add(snakeHeadRec);

        for (int i = 0; i < 3; i++) {
            int tailX = snakeHeadX - (gridBoxSize * (i + 1));
            int tailY = snakeHeadY;
            Rectangle snakeTailRec = new Rectangle(25, 25);
            snakeTailRec.setFill(c);
            snakeTailRec.setX(tailX);
            snakeTailRec.setY(tailY);
            snakeTailRec.setStroke(c);
            //snakeTailRec.setArcWidth(15.0);
            //snakeTailRec.setArcHeight(25.0);
            snakeList.add(snakeTailRec);
        }
    }

    public void DrawSnake(Pane pane) {
        for (Rectangle rectangle : snakeList) {
            pane.getChildren().add(rectangle);
        }
    }

    public int GetFrontX() {
        return (int) snakeList.get(0).getX();
    }

    public int GetFrontY() {
        return (int) snakeList.get(0).getY();
    }

    public void MoveSnake(int dx, int dy) {
        if ((dx == 0) && (dy == 0)) return;
        int nextX = (int) snakeList.get(0).getX();
        int nextY = (int) snakeList.get(0).getY();
        snakeList.get(0).setX(nextX + dx);
        snakeList.get(0).setY(nextY + dy);
        for (int i = 1; i < snakeList.size(); i++) {
            int tempX = nextX;
            int tempY = nextY;
            nextX = (int) snakeList.get(i).getX();
            nextY = (int) snakeList.get(i).getY();
            snakeList.get(i).setX(tempX);
            snakeList.get(i).setY(tempY);
        }
    }

    public void SnakeReset(Pane pane) {
        int snakeHeadX = 225;
        int snakeHeadY = 100;
        int gridBoxSize = 25;
        for (int i = 0; i < snakeList.size(); i++) {
            if (i < 4) {
                snakeList.get(i).setX(snakeHeadX - (gridBoxSize * i));
                snakeList.get(i).setY(snakeHeadY);
            } else {
                //snakeList.get(i).resize(0,0);
                pane.getChildren().remove(snakeList.get(i));
            }
        }
        List<Rectangle> tempList = snakeList.subList(0,4);
        snakeList = new Vector<Rectangle>();
        snakeList.addAll(tempList);
    }

    public boolean SnakeCollide() {
        int headX = (int) snakeList.get(0).getX();
        int headY = (int) snakeList.get(0).getY();
        for (int i = 1; i < snakeList.size(); i++) {
            int tailX = (int) snakeList.get(i).getX();
            int tailY = (int) snakeList.get(i).getY();
            if ((headX == tailX) && (headY == tailY)) return true;
        }
        return false;
    }

    public boolean SnakeEatsFruit() {
        int headX = (int) snakeList.get(0).getX();
        int headY = (int) snakeList.get(0).getY();
        for (Fruit f: GameVariables.fruits) {
            int fruitX = f.GetFruitX();
            int fruitY = f.GetFruitY();
            if ((headX == fruitX) && (headY == fruitY)) return true;
        }
        return false;
    }

    public void SnakeGetsBigger(Pane pane) {
        Rectangle snakeTailNew = new Rectangle(25, 25);
        Color c = Color.web("0x4674EB",1.0);
        snakeTailNew.setFill(c);
        int size = snakeList.size() - 1;
        int newX = 0;
        int newY = 0;
        int lastX = (int) snakeList.get(size).getX();
        int lastY = (int) snakeList.get(size).getY();
        int last2X = (int) snakeList.get(size-1).getX();
        int last2Y = (int) snakeList.get(size-1).getY();
        if (lastX > last2X) newX = lastX + 25;
        else if (lastX < last2X) newX = lastX - 25;
        if (lastY > last2Y) newY = lastY + 25;
        else if (lastY < last2Y) newY = lastY - 25;
        snakeTailNew.setX(newX);
        snakeTailNew.setY(newY);
        snakeTailNew.setStroke(c);
        snakeList.add(snakeTailNew);
        pane.getChildren().add(snakeTailNew);
    }
}
