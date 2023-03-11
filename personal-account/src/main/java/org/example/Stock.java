package org.example;

/**
 * @author Michael Gerasimov
 */
public class Stock {
    private final String name;
    private final int count;

    public Stock(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
