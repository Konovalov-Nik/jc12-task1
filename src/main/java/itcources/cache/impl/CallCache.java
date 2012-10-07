package itcources.cache.impl;

import javassist.*;
import javassist.compiler.MemberResolver;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Nikita Konovalov
 */
public class CallCache {
    private static final Logger LOG = LoggerFactory.getLogger(CallCache.class);

    private static HashMap<String, LRUCache<Long, CallInfo>> cache = new HashMap<String, LRUCache<Long, CallInfo>>();

    public static void instrumentMethod(final CtClass clazz, CtMethod method, String key, int maxEntriesForKey, long ttl) throws NotFoundException {
        LOG.info("Instrument called for " + clazz.getName() + "#" + method.getName());
        cache.put(key, new LRUCache<Long, CallInfo>(maxEntriesForKey));

        try {

            CtMethod copy = CtNewMethod.copy(method, clazz, null);
            String newName = method.getName() + "$__$";
            copy.setName(newName);
            clazz.addMethod(copy);
            LOG.info("Copy created for " + method.getName());

            method.getReturnType().isPrimitive();

            String body = "{" +
                    "long time = System.currentTimeMillis();" +
                    "long hash = itcources.cache.impl.CallCache.getHash($args);" +
                    "if (" + (!method.getReturnType().isPrimitive() && !method.getReturnType().equals(CtClass.voidType)) +") {" +
                        "Object value;" +
                        "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                            "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                            "if (e != null) {" +
                                "throw e;" +
                            "} else  {" +
                                "value  = itcources.cache.impl.CallCache.getValue(\"" + key + "\", hash);" +
                            "}" +
                        "} else {" +
                            "try {" +
                                "value = " + newName + "($$);" +
                                "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, value, null);" +
                            "} catch (Exception e) {" +
                                "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, value, e);" +
                                "throw e;" +
                            "}" +

                        "}" +
                        "return ($r)value;" +
                    "}" +
                    "if ("+ method.getReturnType().isPrimitive() +") {" +
                        "if (" + method.getReturnType().equals(CtClass.byteType) + ") {" +
                            "byte value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getByteValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Byte (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Byte (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                        "if (" + method.getReturnType().equals(CtClass.shortType) + ") {" +
                            "short value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getShortValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Short (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Short (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                         "if (" + method.getReturnType().equals(CtClass.intType) + ") {" +
                            "int value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getIntValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Integer (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Integer (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                         "if (" + method.getReturnType().equals(CtClass.longType) + ") {" +
                            "long value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getLongValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Long (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Long (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                        "if (" + method.getReturnType().equals(CtClass.floatType) + ") {" +
                            "float value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getFloatValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Float (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Float (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                        "if (" + method.getReturnType().equals(CtClass.doubleType) + ") {" +
                            "double value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getDoubleValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Double (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Double (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                        "if (" + method.getReturnType().equals(CtClass.booleanType) + ") {" +
                            "boolean value = 0;" +
                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                   "value  = itcources.cache.impl.CallCache.getBoolValue(\"" + key + "\", hash);" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    "value = " + newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Boolean (value), null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, new Boolean (value), e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return value;" +
                        "}" +
                        "if (" + method.getReturnType().equals(CtClass.voidType) + ") {" +

                            "if (itcources.cache.impl.CallCache.checkCache(\"" + key + "\", hash, time, (long)" + ttl + ")) {" +
                                "Exception e = itcources.cache.impl.CallCache.getException( \"" + key + "\", hash);" +
                                "if (e != null) {" +
                                    "throw e;" +
                                "} else  {" +
                                    "itcources.cache.impl.CallCache.getVoidValue(\"" + key + "\", hash);" +
                                   "return;" +
                                "}" +
                            "} else {" +
                                "try {" +
                                    newName + "($$);" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, null, null);" +
                                "} catch (Exception e) {" +
                                    "itcources.cache.impl.CallCache.saveValue(\"" + key + "\", hash, null, e);" +
                                    "throw e;" +
                                "}" +

                            "}" +
                            "return;" +
                        "}" +
                    "}" +
                    "throw new RuntimeException(\"Method returns not a primitive, reference or void. Impossible things happen!! And I have no idea, how to handle this.\");" +

                "}";

            method.setBody(body);
            LOG.info("New body assigned to method " + method.getName());

        } catch (Exception e) {
            LOG.error("Instrumentation failed", e);

        }
    }

    public static long getHash(Object[] args) {
        long result = 0;
        for (Object arg : args) {
            result = result * 13 + arg.hashCode();
        }
        LOG.trace("Hash calculated for " + args + ". It is " + result);
        return result;
    }

    public static boolean checkCache(String key, long hash, long time, long ttl) {
        CallInfo callInfo = cache.get(key).get(hash);
        if (callInfo != null && time - callInfo.getCallTimestamp() <= ttl) {
            LOG.debug("Cache HAS value actual for " + key);
            return true;
        } else {
            LOG.debug("Cache has NO value for " + key + " or its too old.");
            return false;
        }
    }

    public static Object getValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return cache.get(key).get(hash).getResult();
    }

    public static byte getByteValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Byte.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static short getShortValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Short.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static int getIntValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Integer.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static long getLongValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Long.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static float getFloatValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Float.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static double getDoubleValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Double.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static boolean getBoolValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key);
        return Boolean.valueOf(cache.get(key).get(hash).getResult().toString());
    }

    public static void getVoidValue(String key, long hash) {
        LOG.debug("Cached value taken for " + key + ". Actually method is void.");
    }

    public static Exception getException(String key, long hash) {
        LOG.debug("Cached exception taken for " + key);
        return cache.get(key).get(hash).getException();
    }
    public static void saveValue(String key, long hash, Object value, Exception e) {
        LOG.debug("Cache saved value for key:" + key + " with hash " + hash);
        cache.get(key).put(hash, new CallInfo(System.currentTimeMillis(), value, e));
    }

    public static HashMap<String, LRUCache<Long, CallInfo>> getCache() {
        return cache;
    }


}
