package itcources.cache.sample;

import itcources.cache.impl.CallCache;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikita Konovalov
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.debug("main started");
        TestClass testClass = new TestClass();
        LOG.debug("TestClass instance created");
        /*
        for (int i = 0; i < 1000000; i++) {
            System.out.println(testClass.addTenMillionDoubles(0));
        }*/
        for (int i = 0; i < 4000000; i++) {
            testClass.print(String.valueOf(1000));
        }

    }
}
