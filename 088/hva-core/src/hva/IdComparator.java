package hva;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

public class IdComparator implements Comparator<String>, Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;
    
    @Override
    public int compare(String id1, String id2) {
        int cmp = id1.compareToIgnoreCase(id2);
        if (cmp == 0) {
            return id1.compareTo(id2);
        }
        return cmp;
    }
}