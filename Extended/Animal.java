package simulator;

public abstract class Animal implements Runnable {
    protected final String name;
    protected Simulator simulator;
    protected int row;
    protected int col;
    private static final int MOVE_DELAY_MS = 200;

    public Animal(String name) {
        this.name = name;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
    @Override
    public void run() {
        while (simulator.isSimulationRunning()) {
            try {
                move(simulator.getFarm());
                Thread.sleep(MOVE_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected abstract void move(Farm farm);

    @Override
    public String toString() {
        return name;
    }
}
