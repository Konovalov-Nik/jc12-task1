package itcources.cache.sample;

import itcources.cache.impl.CallCache;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Nikita Konovalov
 */
public class Main {
    public static void main(String[] args) throws NotFoundException {
        TestClass testClass = new TestClass();
        /*for (int i = 0; i < 1000000; i++) {
            System.out.println(testClass.addTenMillionDoubles(0));
        } */
        for (int i = 0; i < 2000000; i++) {
            testClass.print(String.valueOf(i%1000));
        }

    }
}
