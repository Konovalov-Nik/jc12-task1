package itcources.cache.impl;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Nikita Konovalov
 */
public class CacheTransforemer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass clazz = null;
        try {
            clazz = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
        } catch (IOException e) {
            e.printStackTrace();
            return classfileBuffer;
        }
        for (CtMethod method : clazz.getDeclaredMethods()) {
            try {
                Cache annotation = (Cache) method.getAnnotation(Cache.class);
                if (annotation != null) {
                    String key = "".equals(annotation.key()) ? (clazz.getName() + "#" + method.getName() + method.getSignature()) : annotation.key();
                    CallCache.instrumentMethod(clazz, method, key, annotation.maxEntriesForKey(), annotation.ttl());

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return classfileBuffer;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            return clazz.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
            return classfileBuffer;

    }
}
