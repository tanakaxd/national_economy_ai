package NE.player;

public class AIPlayer extends Player {

    private IThinkable brain;

    public AIPlayer(IThinkable brain) {
        this.brain = brain;
    }

    public IThinkable getBrain() {
        return brain;
    }

    public void setBrain(IThinkable brain) {
        this.brain = brain;
    }

}