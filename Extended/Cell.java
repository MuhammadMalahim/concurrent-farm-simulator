package simulator;

public abstract class Cell {
    protected String text;
    Cell(String text){
        this.text = text;
    }
    
    @Override
    public String toString(){
        return text;
    }
}
