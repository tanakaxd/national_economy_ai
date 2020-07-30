package NE.player;

public class Worker {

    private boolean isWorkable;

    public Worker() {
        this.isWorkable = false;
    }

    public Worker(boolean isWorkable) {
        this.isWorkable = isWorkable;
    }

    public boolean isWorkable() {
        return isWorkable;
    }

    public void setWorkable(boolean isWorkable) {
        this.isWorkable = isWorkable;
    }

}