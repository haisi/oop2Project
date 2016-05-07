package ch.fhnw.presentation.movieEditor;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
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
            final Color color;

            switch (item) {
                case 0: color = Color.RED; break;
                case 6: color = Color.GREEN; break;
                case 12: color = Color.BLUE; break;
                case 16: color = Color.BLUEVIOLET; break;
                case 18: color = Color.YELLOW; break;
                default: color = Color.BLACK;
            }

            rectangle.setFill(color);
            setGraphic(rectangle);
        }
    }
}
