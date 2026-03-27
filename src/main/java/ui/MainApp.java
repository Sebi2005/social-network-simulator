package ui;
import domain.User;
import repo.*;
import service.SocialService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        var userRepo = new UserRepo();
        var topicRepo = new TopicRepo();
        var postRepo  = new PostRepo();

        var service = new SocialService(userRepo, topicRepo, postRepo);

        for (User u : userRepo.findAll()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user.fxml"));
            Scene scene = new Scene(loader.load());

            UserController ctrl = loader.getController();
            ctrl.init(service, u);

            Stage stage = new Stage();
            stage.setTitle("User: " + u.name());
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args){ launch(args); }
}
