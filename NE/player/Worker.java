package NE.player;

public class Worker {

    private boolean alreadyWorked;

    public Worker() {
        this.alreadyWorked = true;
    }

    public Worker(boolean alreadyWorked) {
        this.alreadyWorked = alreadyWorked;
    }

    public boolean isAlreadyWorked() {
        return alreadyWorked;
    }

    public void setAlreadyWorked(boolean alreadyWorked) {
        this.alreadyWorked = alreadyWorked;
    }

}