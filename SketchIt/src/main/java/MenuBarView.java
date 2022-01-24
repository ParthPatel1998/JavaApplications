import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarView extends MenuBar {
    MenuBarView(Model model, Controller controller) {
        Menu menu1 = new Menu("File");

        MenuItem menuItem11 = new MenuItem("New");
        menuItem11.setStyle("-fx-padding: 0 75 0 0");
        controller.AskSavePromptNew(menuItem11);
        menu1.getItems().add(menuItem11);

        MenuItem menuItem12 = new MenuItem("Load");
        //controller.AskSavePrompt(menuItem12);
        //controller.LoadDrawing(menuItem12);
        controller.AskSavePrompt(menuItem12);
        menu1.getItems().add(menuItem12);

        MenuItem menuItem13 = new MenuItem("Save");
        controller.SaveDrawing(menuItem13);
        menu1.getItems().add(menuItem13);

        MenuItem menuItem14 = new MenuItem("Quit");
        controller.QuitProgram(menuItem14);
        menu1.getItems().add(menuItem14);


        Menu menu2 = new Menu("Edit");

        Menu menu3 = new Menu("Help");
        MenuItem menuItem31 = new MenuItem("About");
        controller.AboutProgram(menuItem31);
        menuItem31.setStyle("-fx-padding: 0 75 0 0");
        menu3.getItems().add(menuItem31);

        this.getMenus().add(menu1);
        this.getMenus().add(menu2);
        this.getMenus().add(menu3);
    }
}
