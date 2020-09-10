package application;
	



import javafx.scene.control.*;

import java.awt.Event;
import java.util.EventListener;
import javafx.util.*;

import javafx.event.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.show();
			Label greetingLabel = new Label("Hello World!");
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			root.setTop(greetingLabel);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Deb's First JavaFX program");
			primaryStage.setScene(scene);
			Label usernameLable = new Label("Username: ");
			usernameLable.setFont(Font.font("Arial", FontWeight.BOLD, 12));
			TextField uf = new TextField();
			uf.setText("What the hell is going on");
			Button sb = new Button("Submit");
			HBox hBox = new HBox();
			hBox.getChildren().addAll(sb, uf);
			root.setRight(hBox);
			root.setCenter(uf);
			sb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					sb.setText("Fuck!");
				}
			});
		
			
			
			
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

class MyHandler implements EventHandler<ActionEvent> {
	Button button;
	void MyButtonHandler(Button button) {this.button = button;}
	public void handle(ActionEvent e) {
		button.setText("Fuck!");
	}
}
