package ch.fhnw.presentation.dashboard;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ch.fhnw.business.movie.service.MovieService;
import ch.fhnw.presentation.moviesTable.MoviesTablePresenter;
import ch.fhnw.presentation.moviesTable.MoviesTableView;
import ch.fhnw.presentation.toolbar.ToolbarPresenter;
import ch.fhnw.presentation.toolbar.ToolbarView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

/**
 * @author hasan kara <hasan.kara@fhnw.ch>
 */
public class DashboardPresenter implements Initializable {

    @FXML
    BorderPane borderPane;

    @FXML
    SplitPane splitPane;

    @Inject
    MovieService movieService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        borderPane.setTop(new Label("Top pane: add here toolbar"));

        setToolbar();

        MoviesTableView moviesTableView = new MoviesTableView();
        MoviesTablePresenter moviesTablePresenter = (MoviesTablePresenter) moviesTableView.getPresenter();
        splitPane.getItems().add(moviesTableView.getView());

        AnchorPane rechts = new AnchorPane(new Label("Rechts"));
        rechts.setPrefHeight(300);
        rechts.setPrefWidth(300);
        splitPane.getItems().add(rechts);

    }

    private void setToolbar() {
        ToolbarView toolbarView = new ToolbarView();
        ToolbarPresenter toolbarPresenter = (ToolbarPresenter) toolbarView.getPresenter();
        // TODO use toolbarPresenter to bind textfield-value to table
        // TODO add listener-object to toolbarPresenter for remove and add item
        borderPane.setTop(toolbarView.getView());
    }

}
