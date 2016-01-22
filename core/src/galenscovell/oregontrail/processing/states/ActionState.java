package galenscovell.oregontrail.processing.states;

import galenscovell.oregontrail.ui.screens.GameScreen;

public class ActionState implements State {
    private GameScreen gameScreen;

    public ActionState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void enter() {
        System.out.println("\tEntered Action State");
    }

    @Override
    public void exit() {
        System.out.println("\tLeft Action State");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void handleInput(float x, float y) {

    }

    @Override
    public void handleInterfaceEvent(int moveType) {

    }

    @Override
    public StateType getStateType() {
        return StateType.ACTION;
    }
}
