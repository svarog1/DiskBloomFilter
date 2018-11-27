/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskbloomfilter;

//import com.google.common.hash;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author santi
 */
public class DiskBloomFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        for (int i = 0; i < 100; i++) {
            System.out.println("Start");

            //Einlesen der zu testenden wörter. 
            List<String> words = new ArrayList<>();
            {
                File file = new File("E:\\FH\\Algd2\\DiskBloomFilter\\src\\diskbloomfilter\\words.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                int count = 0;
                while ((st = br.readLine()) != null) {
                    words.add(st);
                    count++;
                }
            }

            System.out.println("Words " + words.size());
            double errorPossibility=0.01;
            System.out.println("errorPossibility " + errorPossibility);
            //Hinzufügen der Wörter in unseren Bloom Filter
            BloomFilter b = new BloomFilter(words.size(), errorPossibility);
            for (String word : words) {
                b.addElement(word);
            }

            //Einlesen unserer Test wörter
            List<String> testWords = new ArrayList<>();
            {
                File file = new File("E:\\FH\\Algd2\\DiskBloomFilter\\src\\diskbloomfilter\\testWords.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    testWords.add(st);
                }
            }

            //contains
            {
                int contains = 0;
                int notContains = 0;
                int wrongContains = 0;
                for (String testWord : testWords) {
                    if (b.containElement(testWord)) {
                        contains++;
                        if (!words.contains(testWord)) {
                            wrongContains++;
                        }
                    } else {
                        notContains++;
                    }
                }
                System.out.println("Contains=" + contains);
                System.out.println("notContains=" + notContains);
                System.out.println("wrongContains=" + wrongContains);

            }
            System.out.println("End");
        }
    }
}
