import java.util.*;
import java.util.logging.*;

public class WarshallAlgorithm {
    private static Logger log = Logger.getLogger(WarshallAlgorithm.class.getName());
    private Graph g;
    private Vector<Graph.Memento> history;
    private int curState;
    private boolean isCompleted;

    public WarshallAlgorithm(String edges) {
        setGraphData(edges);
    }

    public void setGraphData(String data) {
        log.fine("Starting parsing string to create Graph");
        isCompleted = false;
        curState = 0;
        history = new Vector<Graph.Memento>();
        Scanner s = new Scanner(data);
        s.useDelimiter("\n");
        String token;
        StringBuilder alp = new StringBuilder();
        Vector<Graph.Edge> edges = new Vector<Graph.Edge>();
        int i;
        char source, destination;
        while(s.hasNext(" *[a-z] +[a-z] *")) {
            token = s.next(" *[a-z] +[a-z] *");
            log.fine(String.format("Parsing token: %s", token));
            i = 0;
            while(Character.isSpaceChar(token.charAt(i)))
                ++i;
            source = token.charAt(i);
            if(alp.indexOf(String.valueOf(source)) == -1)
                alp.append(source);
            i++;
            while(Character.isSpaceChar(token.charAt(i)))
                ++i;
            destination = token.charAt(i);
            if(alp.indexOf(String.valueOf(destination)) == -1)
                alp.append(destination);
            edges.add(new Graph.Edge(source, destination));
        }
        if(s.hasNext()) {
            IllegalArgumentException e = new IllegalArgumentException("Incorrect input data");
            throw e;
        }
        g = new Graph(alp.toString().toCharArray(), edges);
        history.add(g.save());
    }

    public void transitiveClosure() {
        g.transitiveClosure();
    }

    public void stepUp() {
        log.fine("Going to next step");
        if(curState + 1 < history.size()) {
             g.restore(history.elementAt(curState + 1));
        }
        else {
            if(isCompleted && curState + 1 == history.size())
                return;
            isCompleted = g.stepTransitiveClosure();
            if(isCompleted)
                return;
            history.add(g.save());
        }
        ++curState;
    }

    public void stepDown() {
        log.fine("Going to previous step");
        if(curState == 0)
            return;
        g.restore(history.elementAt(curState - 1));
        --curState;
    }

    public void toStart() {
        log.fine("Going to starting step");
        g.restore(history.elementAt(0));
        curState = 0;
    }

    public void toFinalResult() {
        log.fine("Going to final result");
       g.restore(history.elementAt(history.size()-1));
       if(!isCompleted) {
           while(!(isCompleted = g.stepTransitiveClosure()))
               history.add(g.save());
       }
       curState = history.size() - 1;
    }

    public boolean completed() {
        return isCompleted;
    }

    public int verticesCount() {
        return g.verticesCount();
    }

    public char getVertex(int Index) {
        return g.getVertex(Index);
    }

    public Graph.Edge[] getEdges() {
        return g.getEdges();
    }

    public BoolMatrix getMatrix() {
        return g.getMatrix();
    }

    public String toString() {
        return g.toString();
    }
}
        
            
