import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ArrayList<Image> imageShuffle = new ArrayList<>();
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<Image> images = new ArrayList<>();
    private BorderPane borderPane;

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        primaryStage.setMaximized(true);
        primaryStage.setTitle("Laboratoire 8");
        primaryStage.setResizable(false);

        for (int i = 0; i < 9; i++) {
            images.add(new Image("file:images/default.jpg"));
            imageShuffle.add(images.get(i));
            imageViews.add(new ImageView(images.get(i)));
            imageViews.get(i).setFitHeight(300);
            imageViews.get(i).setFitWidth(300);
            dragAndDropSetUp(imageViews.get(i));
        }
        changeImage("mario");
        menu();

        VBox vBox1 = new VBox(imageViews.get(0), imageViews.get(3), imageViews.get(6));
        vBox1.setAlignment(Pos.CENTER);
        VBox vBox2 = new VBox(imageViews.get(1), imageViews.get(4), imageViews.get(7));
        vBox2.setAlignment(Pos.CENTER);
        VBox vBox3 = new VBox(imageViews.get(2), imageViews.get(5), imageViews.get(8));
        vBox3.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(vBox1, vBox2, vBox3);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M && event.isControlDown()) {
                shuffle();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void dragAndDropSetUp(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent contenu = new ClipboardContent();
            contenu.putString("");
            dragboard.setContent(contenu);
        });
        imageView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
        });
        imageView.setOnDragDropped(event -> {
            ImageView imageViewSource = (ImageView) event.getGestureSource();
            ImageView imageViewTarget = (ImageView) event.getGestureTarget();
            Image imageTempSource = imageViewSource.getImage();
            imageViewSource.setImage(imageViewTarget.getImage());
            imageViewTarget.setImage(imageTempSource);
            double rotateTempSource = imageViewSource.getRotate();
            imageViewSource.setRotate(imageViewTarget.getRotate());
            imageViewTarget.setRotate(rotateTempSource);
            event.setDropCompleted(true);
        });
        imageView.setOnDragDone(event -> verifier());
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                switch ((int) imageView.getRotate()) {
                    case 0:
                        imageView.setRotate(90);
                        break;
                    case 90:
                        imageView.setRotate(180);
                        break;
                    case 180:
                        imageView.setRotate(270);
                        break;
                    case 270:
                        imageView.setRotate(0);
                        break;
                }
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                switch ((int) imageView.getRotate()) {
                    case 0:
                        imageView.setRotate(270);
                        break;
                    case 90:
                        imageView.setRotate(0);
                        break;
                    case 180:
                        imageView.setRotate(90);
                        break;
                    case 270:
                        imageView.setRotate(180);
                        break;
                }
            }
            verifier();
        });
    }

    private void shuffle() {
        Collections.shuffle(imageShuffle);
        for (int i = 0; i < imageShuffle.size(); i++) {
            imageViews.get(i).setImage(imageShuffle.get(i));
            int rnd = (int) (Math.random() * 4);
            switch (rnd) {
                case 0:
                    imageViews.get(i).setRotate(0);
                    break;
                case 1:
                    imageViews.get(i).setRotate(90);
                    break;
                case 2:
                    imageViews.get(i).setRotate(180);
                    break;
                case 3:
                    imageViews.get(i).setRotate(270);
                    break;
            }
        }
    }

    private void verifier() {
        boolean done = true;
        for (int i = 0; i < imageViews.size(); i++)
            if (done) {
                done = imageViews.get(i).getImage().equals(images.get(i));
                if (done && imageViews.get(i).getRotate() != 0)
                    done = false;
            }
        if (done) {
            Label label = new Label("Vous avez gagné!");
            HBox hb = new HBox(label);
            Dialog dialog = new Dialog();
            dialog.getDialogPane().setContent(hb);
            dialog.getDialogPane().getButtonTypes().add(
                    new ButtonType("Rejouer")
            );
            dialog.showAndWait();
            shuffle();
        }
    }

    private void menu() {
        Menu menuImporter = new Menu("Importer");
        MenuItem itemImporter = new MenuItem("Importer");
        menuImporter.getItems().addAll(itemImporter);
        MenuBar menuBar = new MenuBar(menuImporter);
        borderPane.setTop(menuBar);

        itemImporter.setOnAction(event -> {
            ChoiceDialog<String> alerte = new ChoiceDialog<String>("Mario", "Mario", "Mayuri", "Snoop", "Monkey", "AngryCat", "Lait", "DrPepper", "MadCat", "VerySad", "MonaLisa", "ButterCat", "Fish", "Giorno");
            alerte.setTitle("Sélection de puzzle");
            alerte.setHeaderText("Veuillez choisir");
            alerte.setContentText("Votre choix: ");
            try {
                changeImage(alerte.showAndWait().get().toLowerCase());
            }catch (Exception e){
            }
        });
    }

    private void changeImage(String resultat) {
        for (int i = 0; i < 9; i++) {
            images.set(i, new Image("file:images/" + resultat + " (" + (i + 1) + ")" + ".jpg"));
            imageViews.get(i).setImage(images.get(i));
            imageShuffle.set(i, images.get(i));
        }
        shuffle();
    }

}