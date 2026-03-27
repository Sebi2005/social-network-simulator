package ui;
import domain.*;
import observer.Observer;
import service.SocialService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
public class UserController implements Observer{
    private SocialService service;
    private User user;

    @FXML private ListView<String> feedList;
    @FXML private ListView<String> myPostsList;
    @FXML private ListView<String> subsList;

    @FXML private TextField postField;

    @FXML private TextField topicSearchField;
    @FXML private ListView<Topic> topicResultsList;

    @FXML private TextField editField;
    private int selectedPostId=-1;

    public void init(SocialService service, User user){
        this.service = service;
        this.user=user;
        service.addObserver(this);
        topicResultsList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Topic t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t==null ? null : t.name());
            }
        });
        refreshAll();


    }

    @Override
    public void update(){refreshAll();}

    private void refreshAll() {
        try {
            feedList.getItems().setAll(
                    service.getFeedFor(user).stream()
                            .map(p -> "["+p.ts()+"] userId="+p.userId()+": "+p.text())
                            .toList()
            );

            myPostsList.getItems().setAll(
                    service.getMyPosts(user).stream()
                            .map(p -> p.id()+": ["+p.ts()+"] "+p.text())
                            .toList()
            );

            subsList.getItems().setAll(
                    service.getMyTopics(user).stream().map(Topic::name).toList()
            );
        } catch (Exception e) {
            showErr(e);
        }




    }
    @FXML
    private void onAddPost() {
        try {
            service.addPost(user, postField.getText());
            postField.clear();
        } catch (Exception e) {
            showErr(e);
        }
    }

    @FXML
    private void onTopicSearchChanged(){
        try{
            var matches = service.searchTopics(topicSearchField.getText());
            topicResultsList.getItems().setAll(matches);
        } catch (Exception e) {showErr(e);}
    }

    @FXML
    private void onSubscribe(){
        Topic t = topicResultsList.getSelectionModel().getSelectedItem();
        if (t == null) return;
        try {
            service.subscribe(user,t);
        } catch (Exception e) {showErr(e);}
    }
    @FXML
    private void onSelectMyPost() {
        String s = myPostsList.getSelectionModel().getSelectedItem();
        if(s == null) return;

        selectedPostId = Integer.parseInt(s.substring(0, s.indexOf(':')));
        editField.setText(s.substring(s.indexOf("]")+2));
    }

    @FXML
    private void onUpdatePost(){
        if (selectedPostId < 0) return;
        try {
            service.updatePostText(selectedPostId, editField.getText());
        } catch (Exception e) {showErr(e);}
    }

    private void showErr(Exception e){
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
    }


}
