import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {

    private static int counter = 0;
    private static int period = 200;
    private boolean pause = false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        PDFParser parser = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        PDFTextStripper pdfStripper;

        String parsedText;
        String fileName = "ch18-LearningFromObservations.pdf";
        File file = new File(fileName);
        String text = "";
        try {
            parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
            text = parsedText.replaceAll("\n", "");
            text = text.replaceAll("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }

        String[] words = text.split(" ");
        BorderPane root = new BorderPane();
        Region region = new Region();
        Region region1 = new Region();
        Region region2 = new Region();
        Region region3 = new Region();

        root.setLeft(region);
        root.setRight(region1);
        root.setTop(region2);
        root.setBottom(region3);


        Scene mainScene = new Scene(root, 500, 200);

        Label wordLabel = new Label();
        wordLabel.setAlignment(Pos.CENTER);
        wordLabel.setPrefWidth(500);
        wordLabel.setFont(new Font(30));
        wordLabel.setStyle("-fx-text-fill: bold");
        wordLabel.setText("this");

        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                KeyCode kc = ke.getCode();
                if (kc.equals(KeyCode.ENTER)) {
                    pause = true;
                } else {
                    pause = false;
                }
            }
        });

        root.setCenter(wordLabel);

        Label wordCount = new Label();

        root.setTop(wordCount);

        primaryStage.setTitle("KT NEWS");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();




        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    if(pause == false){
                        String word = words[counter++].trim().toLowerCase();
                        wordLabel.setText(word);
                        wordCount.setText(counter + "/" + words.length);
                        if(word.length() >= 8){
                            pause = true;
                        }
                    }
                });
            }
        }, 0, period);

    }
}
