package agh.ics.oop.animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Genotype {
    private final List<Integer> genes;

    public Genotype() {
        this.genes = (new Random()).ints(32, 0, 8).sorted().boxed().collect(Collectors.toList());
    }

    public Genotype(Genotype genotype1, Genotype genotype2, int firstPartSize) {
        this.genes = new ArrayList<>(genotype2.genes);

        int lowerBound = 0;
        int upperBound = firstPartSize;
        if ((new Random()).nextBoolean()) {
            lowerBound = 32 - firstPartSize;
            upperBound = 32;
        }

        for (int i = lowerBound; i < upperBound; i++) {
            this.genes.set(i, genotype1.genes.get(i));
        }
        Collections.sort(this.genes);
    }

    public Genotype(Genotype genotype) {
        this.genes = new ArrayList<>(genotype.genes);
    }

    public int randomGene() {
        return this.genes.get((new Random()).nextInt(32));
    }

    @Override
    public int hashCode() {
        return this.genes.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Genotype that = (Genotype) other;
        return this.genes.equals(that.genes);
    }

    @Override
    public String toString() {
        return this.genes.toString();
    }
}
