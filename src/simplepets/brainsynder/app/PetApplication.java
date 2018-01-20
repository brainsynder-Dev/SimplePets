package simplepets.brainsynder.app;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;

public class PetApplication extends Application {
    @Getter
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    private void handleDisconnect() {
        window.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        String windowName = "SimplePets Info App";
        window.setTitle(windowName);
        window.setOnCloseRequest(e -> {
            e.consume();
            handleDisconnect();
        });
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMaxWidth(window.getMaxWidth());
        progressBar.setMinWidth(800);
        progressBar.setPrefWidth(800);
        progressBar.setPrefHeight(20);
        progressBar.setTooltip(new Tooltip("Loading Application..."));
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        browser.setContextMenuEnabled(false);
        createContextMenu(browser);
        BorderPane pane = new BorderPane();
        Worker<Void> worker = webEngine.getLoadWorker();
        progressBar.progressProperty().bind(worker.progressProperty());
        pane.setCenter(browser);
        pane.setBottom(progressBar);
        worker.stateProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue == Worker.State.RUNNING) || (newValue == Worker.State.READY) || (newValue == Worker.State.SCHEDULED)) {
                return;
            }

            pane.setBottom(null);
            if (newValue == Worker.State.FAILED) {
                webEngine.loadContent("<br/><br/><br/><br/><br/><br/><center><div><div align='center'>Oh no!!</div><div align='center'>It seems the Application could not connect to the Database. Please try again later.</div><hr/><div align='center'><p>Make sure you have Internet</p></div></div></center>");
            }
        });

        Scene scene = new Scene(pane, 800, 500);
        window.setScene(scene);
        window.setMaximized(true);
        window.show();
        webEngine.load("http://app.pluginwiki.tk/?version=3.9");
    }

    private void createContextMenu(WebView webView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(e -> {
            String text = String.valueOf(webView.getEngine().executeScript("(function getSelectionText() {\n"
                    + "    var text = \"\";\n"
                    + "    if (window.getSelection) {\n"
                    + "        text = window.getSelection().toString();\n"
                    + "    } else if (document.selection && document.selection.type != \"Control\") {\n"
                    + "        text = document.selection.createRange().text;\n"
                    + "    }\n"
                    + "    return text;\n"
                    + "})()"));
            if ((text == null) || text.isEmpty()) {
                webView.getEngine().executeScript("Materialize.toast('Could not find text to copy.', 1000, 'rounded');");
                return;
            }

            try {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(text);
                clipboard.setContent(content);
                webView.getEngine().executeScript("Materialize.toast('Successfully copied text.', 1000, 'rounded');");
            } catch (Exception ex) {
                webView.getEngine().executeScript("Materialize.toast('An Error occurred when trying to copy the text', 1000, 'rounded');");
            }
        });
        contextMenu.getItems().addAll(copy);

        webView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(webView, e.getScreenX(), e.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

    }
}
