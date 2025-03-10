package simulator;

public class EmptyCell extends Cell{ 
    
    public EmptyCell(){
        super(" ");
    }
    
    @Override
    public String toString(){
        return text;
    }
}
