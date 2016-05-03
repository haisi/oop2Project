package ch.fhnw.presentation.toolbar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class ToolbarPresenter implements Initializable {

    @FXML
    Label addLbl;

    @FXML
    Label removeLbl;

    @FXML
    TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
