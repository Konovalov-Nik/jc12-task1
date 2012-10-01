package itcources.cache.impl;

import javassist.*;
import javassist.compiler.MemberResolver;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Nikita Konovalov
 */
public class CallCache {
    private static HashMap<String, LRUCache<Long, CallInfo>> cache = new HashMap<String, LRUCache<Long, CallInfo>>();

    public static void instrumentMethod(final CtClass clazz, CtMethod method, String key, int maxEntriesForKey, long ttl) throws NotFoundException {

        cache.put(key, new LRUCache<Long, CallInfo>(maxEntriesForKey));

        try {

            CtMethod copy = CtNewMethod.copy(method, clazz, null);
            String newName = method.getName() + "$__$";
            copy.setName(newName);
            copy.setModifiers(Modifier.PUBLIC);
            clazz.addMethod(copy);

            method.setBody("{" +
                    "long time = System.currentTimeMillis();" +
                    "long hash = itcources.cache.impl.CallCache.getHash($args);" +
                    "if (itcources.cache.impl.CallCache.checkCache(\""+ key +"\", hash, time, (long)" + ttl +")) {" +
                    "return itcources.cache.impl.CallCache.getValue(\"" + key + "\", hash);" +
                    "} else {" +
                    "Object value = " + newName +"($$);" +
                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, value);" +
                    "return value;" +
                    "}" +
                    "}");

        } catch (CannotCompileException e) {
            e.printStackTrace();

        }
    }

    public static long getHash(Object[] args) {
        long result = 0;
        for (Object arg : args) {
            result = result * 13 + arg.hashCode();
        }
        return result;
    }

    public static boolean checkCache(String key, long hash, long time, long ttl) {
        CallInfo callInfo = cache.get(key).get(hash);
        return (callInfo != null && time - callInfo.getCallTimestamp() <= ttl);
    }

    public static Object getValue(String key, long hash) {
        return cache.get(key).get(hash).getResult();
    }
    public static void saveValue(String key, long hash, Object value) {
        cache.get(key).put(hash, new CallInfo(hash, value));
    }

    public static HashMap<String, LRUCache<Long, CallInfo>> getCache() {
        return cache;
    }


}
