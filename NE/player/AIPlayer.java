package NE.player;

public class AIPlayer extends Player {

    private IThinkable brain;

    public AIPlayer(IThinkable brain) {
        this.brain = brain;
    }

}