package algorithms.data_structure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Individual {


    public int[] gnome;
    public int fitness;

    public Individual(int[] nodes, int fitness) {
        this.gnome = nodes;
        this.fitness = fitness;
    }

    public String toString() {
        String result = "";
        for (Integer t : gnome) {
            result += t.toString();
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3};
        int[] b = a;
        a[1] = 5;
        System.out.println(b[1]);
    }

}
