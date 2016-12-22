/*
 * File: Triplet.java
 * Author: Fredrik Johansson
 * Date: 2016-12-22
 */
package view;

public class Triplet<T1, T2, T3> {

    private T1 first;
    private T2 second;
    private T3 third;

    public Triplet(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }
}
