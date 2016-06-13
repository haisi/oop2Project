package ch.fhnw.presentation;

import ch.fhnw.presentation.dashboard.DashboardPresenter;
import ch.fhnw.presentation.dashboard.DashboardView;
import com.airhacks.afterburner.injection.Injector;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.hamcrest.MatcherAssert.assertThat;
import org.loadui.testfx.GuiTest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Simple example integration test.
 *
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
@Category(App.class)
public class AppTest extends GuiTest {

    private DashboardPresenter dashboardPresenter;

    @Test
    public void add_new_movie_to_table() throws InterruptedException {

        int previousSize = dashboardPresenter.getMoviesTablePresenter().getData().size();

        click("#add");

        int newSize = dashboardPresenter.getMoviesTablePresenter().getData().size();

        assertThat("Add new movie to table", previousSize == newSize - 1);
    }

    @Override
    protected Parent getRootNode() {
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("primaryStage", stage);
        customProperties.put("defaultLocal", Locale.getDefault());
        Injector.setConfigurationSource(customProperties::get);

        System.setProperty("happyEnding", " Enjoy the flight!");
        DashboardView appView = new DashboardView();
        dashboardPresenter = (DashboardPresenter) appView.getPresenter();

        return appView.getView();
    }

}