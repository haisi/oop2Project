package ch.fhnw.presentation.dashboard;

import javax.annotation.PostConstruct;

/**
 *
 * @author hasan kara <hasan.kara@fhnw.ch>
 */
public class Tower {

    @PostConstruct
    public void init() {
        System.out.println("Tower.init()");
    }

    public String readyToTakeoff() {
        System.out.println("Ready to take-off");
        return "ok from tower";
    }
}
