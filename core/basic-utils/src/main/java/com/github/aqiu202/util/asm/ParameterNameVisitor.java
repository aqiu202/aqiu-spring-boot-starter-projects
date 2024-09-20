package com.github.aqiu202.util.asm;


import com.github.aqiu202.util.ReflectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 ClassVisitor 用于方法参数的访问
 *
 * @author xuqiu
 */
public class ParameterNameVisitor extends ClassVisitor {

    private static final ClassLoader classLoader;

    static {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            try {
                cl = ClassLoader.getSystemClassLoader();
            } catch (Throwable ignored) {
            }
        }
        classLoader = cl;
    }

    private final List<String> parameterNames = new ArrayList<>();
    private final Method method;
    private final String methodDescriptor;

    public ParameterNameVisitor(Method method) {
        super(Opcodes.ASM9);
        this.method = method;
        this.methodDescriptor = ReflectionUtils.getMethodDescriptor(method);
    }

    protected List<String> getParameterNames() {
        return parameterNames;
    }

    public List<String> visitMethodParameterNames() {
        Method method = this.method;
        String className = method.getDeclaringClass().getName();
        // 读取字节码文件或者类的字节数组
        InputStream is = classLoader.getResourceAsStream(className.replace('.', '/') + ".class");
        if (is != null) {
            // 使用 ASM 解析类
            try {
                ClassReader classReader = new ClassReader(is);
                classReader.accept(this, ClassReader.SKIP_FRAMES);
                return this.getParameterNames();
            } catch (IOException e) {
                throw new RuntimeException("类字节码解析异常", e);
            }
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (StringUtils.equalsAny(name, "<init>", "<clinit>")) {
            return null;
        }
        int methodParameterCount = Type.getArgumentTypes(descriptor).length;
        if (methodParameterCount == 0 || !StringUtils.equals(descriptor, this.methodDescriptor)) {
            return null;
        }
        return new MethodVisitor(Opcodes.ASM9) {

            @Override
            public void visitLocalVariable(String name, String descriptor, String signature, org.objectweb.asm.Label start, org.objectweb.asm.Label end, int index) {
                if (this.isMethodParameter(index)) {
                    parameterNames.add(name);
                }
                super.visitLocalVariable(name, descriptor, signature, start, end, index);
            }

            private boolean isMethodParameter(int index) {
                return index > 0 && index <= methodParameterCount;
            }

        };
    }

}

