package cn.bucheng.asm.create;

import jdk.internal.org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.lang.reflect.Method;


public class DemoDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "com/taobao/film/Demo", null, "java/lang/Object", null);
        cw.visitSource("Demo.java", null);
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "HELLO_CONST", "Ljava/lang/String;", null,
                "Hello");
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, "CONST", "Ljava/lang/String;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(6, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/taobao/film/Demo;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(15, l0);
            mv.visitVarInsn(ALOAD, 0);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNULL, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ARRAYLENGTH);
            mv.visitInsn(ICONST_1);
            mv.visitJumpInsn(IF_ICMPNE, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(16, l2);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitFieldInsn(GETSTATIC, "com/taobao/film/Demo", "CONST", "Ljava/lang/String;");
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            mv.visitInsn(AASTORE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitLabel(l1);
            mv.visitLineNumber(18, l1);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l3, 0);
            mv.visitMaxs(7, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(8, l0);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(PUTSTATIC, "com/taobao/film/Demo", "CONST", "Ljava/lang/String;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(11, l1);
            mv.visitLdcInsn("Hello%s!");
            mv.visitFieldInsn(PUTSTATIC, "com/taobao/film/Demo", "CONST", "Ljava/lang/String;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(12, l2);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static class MyClassLoader extends ClassLoader {
        public MyClassLoader() {
            super();
        }

        public MyClassLoader(ClassLoader cl) {
            super(cl);
        }

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] bytes = dump();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Demo.class");
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        MyClassLoader classLoader = new MyClassLoader(Thread.currentThread().getContextClassLoader());
        Class<?> testClass = classLoader.defineClass("com.taobao.film.Demo",
            bytes);
        // invoke static
        Method staticMain = testClass.getMethod("main", String[].class);
        staticMain.invoke(null, new Object[] {new String[] {"zhupin"}});
    }
}