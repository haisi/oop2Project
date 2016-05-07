package ch.fhnw.presentation.movieEditor;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Customer ListCell used in the FSK-combobox.
 * Displays the corresponding FSK-logo.
 *
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class FskListCell extends ListCell<Integer> {

    private final Rectangle rectangle;
    {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        rectangle = new Rectangle(50, 50);
        setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
        } else {

            String imageUrl = getClass().getResource("/fsk_logos/FSK_" + item + "_logo.svg.png").toExternalForm();
            ImageView image = new ImageView(imageUrl);
            image.setPreserveRatio(true);

            setGraphic(image);
        }
    }
}
