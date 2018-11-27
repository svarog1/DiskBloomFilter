/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskbloomfilter;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author santi
 */
public class BloomFilter {

    private final int numberHashFunctions;
    private final int arrayLength;
    private final int expectetElements; //die in der Datenstruktur gespeichert werden
    private final double errorPossibility;
    private List<HashFunction> hashFunctions = new ArrayList<>();
    private boolean[] elemente;

    public BloomFilter(int expectetElements, double errorPossibility) {
        this.expectetElements = expectetElements;
        this.errorPossibility = errorPossibility;
        this.arrayLength = calcArrayLength();
        this.numberHashFunctions = calcNumberHashFunctions();
        elemente = new boolean[arrayLength];
        generateHaschFunctions();
        initElemente();

    }

    private int calcNumberHashFunctions() {
        double t1=((double)arrayLength / (double)expectetElements);
        double t2=Math.log(2.0);
        double t3= t1*t2;
        double t = ((arrayLength / expectetElements) * Math.log(2.0));
        int val=(int) Math.round(t);
        if (val<1) {
            val=1;
        }
        return val;
    }

    private int calcArrayLength() {
        double mlep=Math.log((double)errorPossibility);
        double ml2=Math.log(2.0) * Math.log(2.0);
        int result=(int)Math.round(-(expectetElements*mlep)/ml2);
        return result;
    
    }

    private void generateHaschFunctions() {
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < this.numberHashFunctions; i++) {
            hashFunctions.add(Hashing.murmur3_128(r.nextInt()));
        }
    }

    private void initElemente() {
        for (int i = 0; i < arrayLength; i++) {
            elemente[i] = false;
        }
    }

    public void addElement(String text) {
        for (int i = 0; i < numberHashFunctions; i++) {
            HashFunction hf=hashFunctions.get(i);
            long hashL=hf.hashString(text,  Charset.defaultCharset()).asLong();
            int elementPosition=mod(hashL);
            elemente[elementPosition]=true;
        }
    }
    
    public boolean containElement(String text){
        for (int i = 0; i < numberHashFunctions; i++) {
            HashFunction hf=hashFunctions.get(i);
            long hashL=hf.hashString(text,  Charset.defaultCharset()).asLong();
            int elementPosition=mod(hashL);
            if ( !elemente[elementPosition]) {
                return false;
            }
        }
        return true;
    }

    public int mod(long value) {
        int temp = (int) ((value % arrayLength + arrayLength) % arrayLength);
        return (int)temp;
    }

}
