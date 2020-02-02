package ru.otus.chernovsa.gc;

import java.util.HashSet;

public class ExpGc implements ExpGcMBean {
    private final int loopCounter;
    private volatile int size = 0;
//    private HashSet<Integer> integerHashSet;

    public ExpGc(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setSize(int size) {
        System.out.println( "new size:" + size );
        this.size = size;
//        integerHashSet = new HashSet<>(this.size*5);
    }

    void run() throws InterruptedException {
        for ( int idx = 0; idx < loopCounter; idx++ ) {
            int local = size;
            Object[] array = new Object[ local ];
            for ( int i = 0; i < local; i++ ) {
                array[ i ] = new String( new char[ 0 ] );
            }
//            Thread.sleep( 100 ); //Label_1
        }
//        for (int i = 0; i < this.size*5; i++) {
//            integerHashSet.add(Integer.valueOf(i));
//        }
    }
}
