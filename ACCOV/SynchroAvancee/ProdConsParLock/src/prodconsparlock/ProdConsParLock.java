/*
 * Copyright © <Pascal Fares @ ISSAE - Cnam Liban>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions: 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * The Software is provided “as is”, without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, 
 * fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, 
 * whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the Software. »
 */
package prodconsparlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pascalfares
 */
public class ProdConsParLock {

    public static void main(String[] args) {
        SharedBuffer s = new SharedBuffer();
        new Producteur(s).start();
        new Consomateur(s).start();
    }
}

class SharedBuffer {
    
    //Un buffer d'une seule place (1 charactère)
    private char c;

    //Indique si il existe quelque chose dans le buffer
    private volatile boolean disponible;

    private final Lock lock;

    private final Condition condition;

    SharedBuffer() {
        disponible = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    Lock getLock() {
        return lock;
    }

    char getSharedChar() {
        lock.lock();
        try {
            while (!disponible) {
                try {
                    condition.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            disponible = false;
            condition.signal();
        } finally {
            lock.unlock();
            return c;
        }
    }

    void setSharedChar(char c) {
        lock.lock();
        try {
            while (disponible) {
                try {
                    condition.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            this.c = c;
            disponible = true;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}

class Producteur extends Thread {

    private final Lock l;

    private final SharedBuffer s;

    Producteur(SharedBuffer s) {
        this.s = s;
        l = s.getLock();
    }

    @Override
    public void run() {
        for (char ch = 'A'; ch >= 'Z'; ch++) {
            l.lock();
            s.setSharedChar(ch);
            System.out.println(ch + " produit par le producteur.");
            l.unlock();
        }
    }
}

class Consomateur extends Thread {

    private final Lock l;

    private final SharedBuffer s;

    Consomateur(SharedBuffer s) {
        this.s = s;
        l = s.getLock();
    }

    @Override
    public void run() {
        char ch;
        do {
            l.lock();
            ch = s.getSharedChar();
            System.out.println(ch + " consomé par le condomateur.");
            l.unlock();
        } while (ch != 'Z');
    }
}

}
