package simulator;

import java.util.concurrent.ThreadLocalRandom;

public class Dog extends Animal {

    Dog(String name) {
        super(name);
    }

    @Override
    protected void move(Farm farm) {
        boolean allBlocked = true;
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1} 
        };

        for (int[] dir : directions) {
            if (farm.getCell(row + dir[0], col + dir[1]) instanceof EmptyCell) {
                allBlocked = false;
                break;
            }
        }
        
        if(allBlocked) return;

        int[] direction;
        int newRow, newCol;

        do {
            direction = generateRandomDirection();
            newRow = row + direction[0];
            newCol = col + direction[1];
        } while (!isValidMove(farm, newRow, newCol));

        farm.setCell(row, col, new EmptyCell());

        farm.setCell(newRow, newCol, this);
        this.row = newRow;
        this.col = newCol;
    }

    protected int[] generateRandomDirection() {
        int rowDir, colDir;

        do {
            rowDir = ThreadLocalRandom.current().nextInt(-1, 2); 
            colDir = ThreadLocalRandom.current().nextInt(-1, 2); 
        } while (Math.abs(rowDir) + Math.abs(colDir) != 1);

        return new int[]{rowDir, colDir};
    }

    private boolean isValidMove(Farm farm, int newRow, int newCol) {
        if (newRow < 0 || newRow >= farm.getRows() || newCol < 0 || newCol >= farm.getCols()) {
            return false;
        }

        if (farm.getZoneForPosition(newRow, newCol) == 5) {
            return false;
        }

        return farm.getCell(newRow, newCol) instanceof EmptyCell;
    }
    
}
