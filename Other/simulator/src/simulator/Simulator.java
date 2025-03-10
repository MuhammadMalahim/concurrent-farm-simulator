package simulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulator {
    private final Farm farm;
    private final AtomicBoolean simulationRunning;
    private final ExecutorService executor;

    public Simulator() {
        this(new Farm());
        
    }
    
    public Simulator(Farm farm) {
        this.farm = farm;
        this.simulationRunning = new AtomicBoolean(false);
        this.executor = Executors.newFixedThreadPool(farm.getnDogs() + farm.getnSheep());
    }
    
    public Farm getFarm() {
        return farm;
    }

    public boolean isSimulationRunning() {
        return simulationRunning.get();
    }

    public void stopSimulation(){
        try {
            simulationRunning.set(false);
            System.out.println("Simulation ended.");
            farm.print();
            executor.shutdown();
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public void startSimulation() {
        System.out.println("Starting simulation...");
        farm.print();
        simulationRunning.set(true);
        
        for (Dog d : farm.getDogs()) {
            d.setSimulator(this);
            executor.execute(d);
        }
        
        for (Sheep s : farm.getSheep()) {
            s.setSimulator(this);
            executor.execute(s);
        }
        
        farm.print();

        try {
            while (isSimulationRunning()) {
                farm.print();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
