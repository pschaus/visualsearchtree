package org.uclouvain.visualsearchtree.tree;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class ExampleTestFxTest {

    @Start
    void onStart(Stage stage) {
        stage.setScene(new ExampleTextFx());
        stage.show();
    }

    @Test
    void should_contain_first_button(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#first-button").queryButton()).hasText("click me to change my name!");
    }

    @Test
    void should_click_on_first_button(FxRobot robot) {
        // when:
        robot.clickOn("#first-button");
        // then:
        Assertions.assertThat(robot.lookup("#first-button").queryButton()).hasText("clicked!");
    }

    @Test
    void should_click_on_second_button_once(FxRobot robot) {
        // when:
        robot.clickOn("#second-button");

        // then:
        Assertions.assertThat(robot.lookup("#first-button").queryButton()).hasText("click me to change my name!");
        Assertions.assertThat(robot.lookup("#second-button").queryButton()).hasText("1 clicks");
    }

    @Test
    void should_click_on_second_button_twice(FxRobot robot) {
        // when:
        robot.clickOn("#second-button");
        robot.clickOn("#second-button");

        // then:
        Assertions.assertThat(robot.lookup("#first-button").queryButton()).hasText("click me to change my name!");
        Assertions.assertThat(robot.lookup("#second-button").queryButton()).hasText("2 clicks");
    }

}