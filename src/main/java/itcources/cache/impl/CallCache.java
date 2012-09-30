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
    private static HashMap <String, LRUCache<Long, CallInfo>> cache  = new HashMap<String, LRUCache<Long, CallInfo>>();
    public static void instrumentMethod(final CtClass clazz, CtMethod method, String key, int maxEntriesForKey, long ttl) throws NotFoundException {

        cache.put(key, new LRUCache<Long, CallInfo>(maxEntriesForKey));

        try {

            CtMethod copy = CtNewMethod.copy(method, clazz, null);
            String newName = method.getName()+ "$__$";
            copy.setName(newName);
            copy.setModifiers(Modifier.PUBLIC);
            clazz.addMethod(copy);

            method.setBody("{return ($r)itcources.cache.impl.CallCache.processCall($class, \"" + newName +"\", $0, $sig, $args, \""+ key +"\", (long)" + ttl +");}");

        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, LRUCache<Long, CallInfo>> getCache() {
        return cache;
    }

    public static Object processCall(Class clazz, String methodName, Object obj, Class[] types,  Object[] args, String key, long ttl) {
        //System.out.println("call");
        try {

            Method inv = clazz.getDeclaredMethod(methodName, types);
            long time = System.currentTimeMillis();
            long hash = 0;
            for (Object arg : args) {
                hash = hash * 13 + arg.hashCode();
            }
            CallInfo callInfo = cache.get(key).get(hash);
            if (callInfo != null && time - callInfo.getCallTimestamp() <= ttl) {
                //System.out.println("Hit");
                return callInfo.getResult();
            } else {
                Object res = inv.invoke(obj, args);
                time = System.currentTimeMillis();
                cache.get(key).put(hash, new CallInfo(time, res));
                return res;

            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
