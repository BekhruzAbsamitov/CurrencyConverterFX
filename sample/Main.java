package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controller.CurrencyConverterController;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        this.primaryStage.setTitle("Currency Converter");
        this.primaryStage.getIcons().add(new Image("sample/recourses/images/converter.png"));

        showMainSceneOverview();

    }

    public void showMainSceneOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CurrencyConverter.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();

            CurrencyConverterController controller = loader.getController();
            controller.getData();

            Image image = new Image("sample/recourses/images/refresh.png");
            ImageView view = new ImageView(image);
            view.setFitHeight(27);
            view.setFitWidth(27);

            controller.refreshButton.setGraphic(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
