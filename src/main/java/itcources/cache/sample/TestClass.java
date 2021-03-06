package itcources.cache.sample;

import itcources.cache.impl.Cache;

import java.util.Random;

/**
 * @author Nikita Konovalov
 */
public class TestClass {
    private static int call = 0;
    //private static int call = 0;

    @Cache(maxEntriesForKey = 1, ttl = 100)
    public short getX(Object x) throws Exception {
//        throw new Exception("Hi!!");
      return Byte.valueOf(x.toString());
    }

    @Cache(maxEntriesForKey = 1, ttl = 1000)
    public double addTenMillionDoubles(int param) {
        double res = 0;
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < 10000000; i++) {
            res += random.nextDouble()/(i+1);
        }
        return res;
    }

    @Cache(maxEntriesForKey = 1000, ttl = 1000)
    public void print(String message) {
        System.out.println("call# " + call + " message " + message);
        call++;
    }
}
