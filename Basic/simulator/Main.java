package simulator;

public class Main {

    public static void main(String[] args) {
        final int ROW = 14;
        final int COL = 14;
        final int N_DOGS = 5;
        final int N_SHEEP = 10;
        
        try {
        Farm farm = new Farm(ROW, COL, N_DOGS, N_SHEEP);
        Simulator simulator = new Simulator(farm);
        
        simulator.startSimulation();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
