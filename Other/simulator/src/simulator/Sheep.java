package simulator;

import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends Animal {

    Sheep(String name) {
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
            if (farm.getCell(row + dir[0], col + dir[1]) instanceof EmptyCell ||
                farm.getCell(row + dir[0], col + dir[1]) instanceof GateCell) {
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

        var isGateCell = farm.getCell(newRow, newCol) instanceof GateCell;

        farm.setCell(newRow, newCol, this);
        this.row = newRow;
        this.col = newCol;

        if (isGateCell) {
            System.out.println(name + " has escaped through the gate!");
            simulator.stopSimulation();
        }

    }

    private int[] generateRandomDirection() {
        int rowDir, colDir;

        do {
            rowDir = ThreadLocalRandom.current().nextInt(-1, 2);
            colDir = ThreadLocalRandom.current().nextInt(-1, 2);
        } while (Math.abs(rowDir) + Math.abs(colDir) != 1);

        return new int[]{rowDir, colDir};
    }

    protected boolean isValidMove(Farm farm, int newRow, int newCol) {

        if (newRow < 0 || newRow >= farm.getRows() || newCol < 0 || newCol >= farm.getCols()) {
            return false;
        }

        Object targetCell = farm.getCell(newRow, newCol);
        if (!(targetCell instanceof EmptyCell || targetCell instanceof GateCell)) {
            return false;
        }

        boolean dogAbove = isDogNear(farm, row - 1, col);
        boolean dogBelow = isDogNear(farm, row + 1, col);
        boolean dogLeft = isDogNear(farm, row, col - 1);
        boolean dogRight = isDogNear(farm, row, col + 1);

        int rowDir = newRow - row;
        int colDir = newCol - col;

        if ((dogAbove && rowDir < 0) || (dogBelow && rowDir > 0)) {
            return false;
        }

        if ((dogLeft && colDir < 0) || (dogRight && colDir > 0)) {
            return false;
        }

        return rowDir != 0 || colDir != 0;
    }

    private boolean isDogNear(Farm farm, int checkRow, int checkCol) {

        if (checkRow < 0 || checkRow >= farm.getRows() || checkCol < 0 || checkCol >= farm.getCols()) {
            return false;
        }

        return farm.getCell(checkRow, checkCol) instanceof Dog;
    }

}
