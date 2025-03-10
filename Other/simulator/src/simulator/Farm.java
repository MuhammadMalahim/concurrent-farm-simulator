package simulator;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Farm {

    private final int rows;
    private final int cols;
    private final int nDogs;
    private final int nSheep;
    private final ArrayList<ArrayList<Object>> grid;
    private final ArrayList<Dog> dogs;
    private final ArrayList<Sheep> sheep;

    public synchronized Object getCell(int row, int col) {
        return grid.get(row).get(col);
    }

    public synchronized void setCell(int row, int col, Object cell) {
        grid.get(row).set(col, cell);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public ArrayList<Sheep> getSheep() {
        return sheep;
    }

    public int getnDogs() {
        return nDogs;
    }

    public int getnSheep() {
        return nSheep;
    }

    private int getZone(Object animal) {
        int zone;

        if (animal instanceof Dog) {
            do {
                zone = ThreadLocalRandom.current().nextInt(1, 9);
            } while (zone == 5);
        } else {
            zone = 5;
        }

        return zone;
    }

    public int getZoneForPosition(int row, int col) {
        int zoneWidth = (cols - 2) / 3;
        int zoneHeight = (rows - 2) / 3;

        int rowZone = (row - 1) / zoneHeight;
        int colZone = (col - 1) / zoneWidth;

        return rowZone * 3 + colZone + 1;
    }

    Farm() {
        this(14, 14);
    }

    Farm(int rows, int cols) {
        this(rows, cols, 5, 10);
    }

    Farm(int rows, int cols, int nDogs, int nSheep) {
        if (((rows - 2) % 3 != 0) || ((cols - 2) % 3 != 0)) {
            throw new IllegalArgumentException("Rows and columns must be multiple of 3 plus 2.");
        } else if ( nSheep == 0 ) {
            throw new IllegalArgumentException("There are no sheep at all.");
        } else if ( nSheep < 0  || nDogs < 0) {
            throw new IllegalArgumentException("Number of dogs or sheep can not be negative.");
        }

        int zoneWidth = (cols - 2) / 3;
        int zoneHeight = (rows - 2) / 3;
        int zoneSize = zoneWidth * zoneHeight;

        if (nDogs > zoneSize) {
            throw new IllegalArgumentException("The number of dogs exceeds the zone size.");
        }
        if (nSheep > zoneSize) {
            throw new IllegalArgumentException("The number of sheep exceeds the available space in zone 5.");
        }

        this.rows = rows;
        this.cols = cols;
        this.nDogs = nDogs;
        this.nSheep = nSheep;
        this.grid = new ArrayList<>();
        this.dogs = new ArrayList<>();
        this.sheep = new ArrayList<>();
        initGrid();
        initDogs();
        initSheep();
    }

    private void initGrid() {
        for (int i = 0; i < rows; i++) {
            ArrayList<Object> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    row.add(new WallCell());
                } else {
                    row.add(new EmptyCell());
                }
            }
            grid.add(row);
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
            placeAnimalInZone(d, getZone(d));

        }
    }

    private void initSheep() {
        for (char name = 'A'; name <= 'A' + nSheep - 1; name++) {
            Sheep s = new Sheep(String.valueOf(name));
            sheep.add(s);
            placeAnimalInZone(s, getZone(s));
        }
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
        animal.setCol(col);
        animal.setRow(row);
    }

    public synchronized void print() {
        for (ArrayList<Object> row : grid) {
            for (Object cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

}
