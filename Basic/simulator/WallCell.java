package simulator;

public class WallCell extends Cell{
    public WallCell(){
        super("#");
    }
    
    @Override
    public String toString(){
        return text;
    }
}
