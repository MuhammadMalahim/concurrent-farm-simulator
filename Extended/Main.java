package simulator;

public class Main {
    public static void main(String[] args) {
        final int ROWS = 14;
        final int COLS = 14;
        final int NUM_DOGS = 5;
        final int NUM_SHEEP = 10;

        Farm farm = new Farm(ROWS, COLS, NUM_DOGS, NUM_SHEEP);

        Simulator simulator = new Simulator(farm);
        simulator.startSimulation();
    }
}
