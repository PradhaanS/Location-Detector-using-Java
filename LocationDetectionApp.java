import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationDetectionApp extends Application {

    private static final String API_KEY = "c1df73bdb440be272bf0ca4740adced6";

    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Location Detection");
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        TextField ipTextField = new TextField();
        ipTextField.setPromptText("Enter IP Address");

        Button detectButton = new Button("Detect");
        detectButton.setOnAction(e -> detectLocation(ipTextField.getText()));

        resultLabel = new Label();

        vbox.getChildren().addAll(ipTextField, detectButton, resultLabel);
        primaryStage.setScene(new Scene(vbox, 300, 200));
        primaryStage.show();
    }

    private void detectLocation(String ipAddress) {
        try {
            URL url = new URL("http://api.ipstack.com/" + ipAddress + "?access_key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());

            String country = json.getString("country_name");
            String region = json.getString("region_name");
            String city = json.getString("city");
            double latitude = json.getDouble("latitude");
            double longitude = json.getDouble("longitude");

            String result = "Location: " + country + ", " + region + ", " + city + "\n";
            result += "Latitude: " + latitude + "\n";
            result += "Longitude: " + longitude;

            resultLabel.setText(result);
        } catch (Exception e) {
            resultLabel.setText("Error occurred: " + e.getMessage());
        }
    }
}
