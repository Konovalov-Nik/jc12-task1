package itcources.cache.impl;

import java.lang.instrument.Instrumentation;

/**
 * @author Nikita Konovalov
 */
public class Agent {
    public static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
        instrumentation.addTransformer(new CacheTransforemer());
    }

    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
    }
}
