package ch.fhnw.presentation.dashboard;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;

/**
 * @author hasan kara <hasan.kara@fhnw.ch>
 */
public class DashboardPresenter implements Initializable {

    @FXML
    BorderPane borderPane;

    @Inject
    Tower tower;

    @Inject
    private String prefix;

    @Inject
    private String happyEnding;

    @Inject
    private LocalDate date;

    private String theVeryEnd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //fetched from dashboard.properties
        this.theVeryEnd = rb.getString("theEnd");

        borderPane.setTop(new Label("Top pane: add here toolbar"));
        borderPane.setCenter(new Label("Center pane: add here splitpane view"));
    }

}
