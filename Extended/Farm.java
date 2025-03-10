package simulator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.ArrayList;

public class Farm {
    private final int rows;
    private final int cols;
    private final int nDogs;
    private final int nSheep;
    private final ConcurrentHashMap<Pair<Integer, Integer>, AtomicReference<Object>> grid;
    private final ArrayList<Dog> dogs;
    private final ArrayList<Sheep> sheep;

    public Farm() {
        this(14, 14);
    }

    public Farm(int rows, int cols) {
        this(rows, cols, 5, 10);
    }

    public Farm(int rows, int cols, int nDogs, int nSheep) {
        if (((rows - 2) % 3 != 0) || ((cols - 2) % 3 != 0)) {
            throw new IllegalArgumentException("Rows and columns must be a multiple of 3 plus 2.");
        }
        if (nSheep <= 0 || nDogs < 0) {
            throw new IllegalArgumentException("Invalid number of dogs or sheep.");
        }

        int zoneWidth = (cols - 2) / 3;
        int zoneHeight = (rows - 2) / 3;
        int zoneSize = zoneWidth * zoneHeight;

        if (nDogs > zoneSize * 4) {
            throw new IllegalArgumentException("The number of dogs exceeds half the available space.");
        }
        if (nSheep > zoneSize) {
            throw new IllegalArgumentException("The number of sheep exceeds the available space in zone 5.");
        }

        this.rows = rows;
        this.cols = cols;
        this.nDogs = nDogs;
        this.nSheep = nSheep;
        this.grid = new ConcurrentHashMap<>();
        this.dogs = new ArrayList<>();
        this.sheep = new ArrayList<>();

        initGrid();
        initDogs();
        initSheep();
    }

    public Object getCell(int row, int col) {
        return grid.get(new Pair<>(row, col)).get();
    }

    public void setCell(int row, int col, Object cell) {
        grid.get(new Pair<>(row, col)).set(cell);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getnDogs() {
        return nDogs;
    }

    public int getnSheep() {
        return nSheep;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public ArrayList<Sheep> getSheep() {
        return sheep;
    }

    private void initGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    grid.put(new Pair<>(i, j), new AtomicReference<>(new WallCell()));
                } else {
                    grid.put(new Pair<>(i, j), new AtomicReference<>(new EmptyCell()));
                }
            }
        }
        placeGate(0, cols, "h");
        placeGate(rows - 1, cols, "h");
        placeGate(0, rows, "v");
        placeGate(cols - 1, rows, "v");
    }

    private void placeGate(int fixed, int range, String side) {
        int gatePosition = ThreadLocalRandom.current().nextInt(1, range - 1);
        if (side.equals("h")) {
            setCell(fixed, gatePosition, new GateCell());
        } else if (side.equals("v")) {
            setCell(gatePosition, fixed, new GateCell());
        }
    }

    private void initDogs() {
        for (int i = 1; i <= nDogs; i++) {
            Dog d = new Dog(String.valueOf(i));
            dogs.add(d);
            placeAnimalInZone(d, getDogZone());
        }
    }

    private void initSheep() {
        for (char name = 'A'; name < 'A' + nSheep; name++) {
            Sheep s = new Sheep(String.valueOf(name));
            sheep.add(s);
            placeAnimalInZone(s, 5); 
        }
    }

    private int getDogZone() {
        int zone;
        do {
            zone = ThreadLocalRandom.current().nextInt(1, 10); // Zones 1 to 9
        } while (zone == 5); 
        return zone;
    }

    private void placeAnimalInZone(Animal animal, int zone) {
        int zoneWidth = (cols - 2) / 3;
        int zoneHeight = (rows - 2) / 3;

        int startRow = ((zone - 1) / 3) * zoneHeight + 1;
        int startCol = ((zone - 1) % 3) * zoneWidth + 1;

        int row, col;
        do {
            row = ThreadLocalRandom.current().nextInt(startRow, startRow + zoneHeight);
            col = ThreadLocalRandom.current().nextInt(startCol, startCol + zoneWidth);
        } while (!(getCell(row, col) instanceof EmptyCell));

        setCell(row, col, animal);
        animal.setRow(row);
        animal.setCol(col);
    }

    public int getZone(int newRow, int newCol) {
        int zoneWidth = (cols - 2) / 3;
        int zoneHeight = (rows - 2) / 3;

        int rowZone = (newRow - 1) / zoneHeight;
        int colZone = (newCol - 1) / zoneWidth;

        return rowZone * 3 + colZone + 1;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(getCell(i, j) + " ");
            }
            System.out.println();
        }
    }
}
