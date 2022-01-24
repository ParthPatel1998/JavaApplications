package starter.graphical;


import javafx.scene.image.ImageView;

public class Fruit {
    int x;
    int y;
    ImageView fruit;

    Fruit(int _x, int _y, ImageView img) {
        x = _x;
        y = _y;
        fruit = img;
    }

    int GetFruitX() {
        return x;
    }
    int GetFruitY() {
        return y;
    }

    void SetFruitX(int _x) {
        x = _x;
    }

    void SetFruitY(int _y) {
        y = _y;
    }

    ImageView GetFruit() {
        return fruit;
    }


}
