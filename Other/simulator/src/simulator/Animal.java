package simulator;

public abstract class Animal implements Runnable{
    protected final String name;
    protected Simulator simulator;
    protected int row;
    protected int col;
    private static final int MOVE_DELAY_MS = 200;

    public void setRow(int row) {
        this.row = row;
    }
    
    public void setCol(int col) {
        this.col = col;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
    
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    @Override
    public void run() {
        while (simulator.isSimulationRunning()) {
            Farm farm = simulator.getFarm();
            synchronized (farm) {
                move(farm);
            }
            try {
                Thread.sleep(MOVE_DELAY_MS); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    
    Animal(String name){
        this.name = name;
    }
    
    protected abstract void move(Farm farm); 
    
    @Override
    public String toString(){
        return name;
    }
}
