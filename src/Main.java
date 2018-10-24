import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {
    private ArrayList<ImageView> imageView = new ArrayList<>();
    private ArrayList<Image> image = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        primaryStage.setHeight(600);
        primaryStage.setWidth(600);
        primaryStage.setTitle("Laboratoire 8");
        primaryStage.setResizable(true);

        for (int i=0;i<9;i++){
            image.add(new Image("file:images/mario" + i + ".jpg"));
            imageView.add(new ImageView(image.get(i)));
            dragAndDropSetUp(imageView.get(i));
            }
        VBox vBox1 = new VBox(imageView.get(0),imageView.get(3),imageView.get(6));
        vBox1.setAlignment(Pos.CENTER);
        VBox vBox2 = new VBox(imageView.get(1),imageView.get(4),imageView.get(7));
        vBox2.setAlignment(Pos.CENTER);
        VBox vBox3 = new VBox(imageView.get(2),imageView.get(5),imageView.get(8));
        vBox3.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(vBox1,vBox2,vBox3);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);

        shuffle(vBox1,vBox2,vBox3);

        scene.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.M) {
                shuffle(vBox1,vBox2,vBox3);
            }
        });

            primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void dragAndDropSetUp(ImageView imageView){
        imageView.setOnDragDetected(event -> {
            System.out.println("Starting Drag and Drop");
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent contenu = new ClipboardContent();
            contenu.putString("");
            dragboard.setContent(contenu);
        } );
        imageView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
        });
        imageView.setOnDragDropped(event -> {
            ImageView imageViewSource = (ImageView) event.getGestureSource();
            ImageView imageViewTarget = (ImageView) event.getGestureTarget();
            Image imageTempSource = imageViewSource.getImage();
            Image imageTempTarget = imageViewTarget.getImage();
            imageViewSource.setImage(imageTempTarget);
            imageViewTarget.setImage(imageTempSource);

        });
    }

    private void shuffle(VBox vBox1,VBox vBox2, VBox vBox3){
        Collections.shuffle(imageView);
        vBox1 = new VBox(imageView.get(0),imageView.get(3),imageView.get(6));
        vBox2 = new VBox(imageView.get(1),imageView.get(4),imageView.get(7));
        vBox3 = new VBox(imageView.get(2),imageView.get(5),imageView.get(8));
    }
}
