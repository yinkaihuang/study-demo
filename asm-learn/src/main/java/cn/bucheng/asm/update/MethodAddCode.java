package cn.bucheng.asm.update;


import cn.bucheng.asm.update.test1.Logger;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;


/**
 * 为方法的前后添加一些内容
 *
 * @date 2018/12/28 23:28
 */

public class MethodAddCode {

    public static class AddCodeVisitor extends ClassVisitor {

        public AddCodeVisitor(int i, ClassVisitor cw) {
            super(i, cw);
        }


        boolean flag = false;

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            if (name.equals("log")) {
                flag = true;
            }
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public void visitEnd() {
            if (!flag) {
                FieldVisitor fv = super.visitField(ACC_PRIVATE, "log",
                        Type.getDescriptor(Logger.class), null,
                        new Logger());
                fv.visitEnd();
            }
            cv.visitEnd();
        }

        /**
         * 访问到方法时被调用
         *
         * @param access
         * @param name
         * @param descriptor
         * @param signature
         * @param exceptions
         * @return
         */
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            //不代理构造函数
            if (!"<init>".equals(name)) {
                MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
                return new AddCodeMethodVisitor_3(this.api, mv);
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }


    /**
     * 方式三:通过调用现有的class文件
     */
    public static class AddCodeMethodVisitor_3 extends MethodVisitor {

        public AddCodeMethodVisitor_3(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }
        /**
         * 方法的开始,即刚进入方法里面
         */
        @Override
        public void visitCode() {
            mv.visitFieldInsn(GETFIELD, "cn/bucheng/asm/update/Demo", "log", "Lcn/bucheng/asm/update/Log;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "cn/bucheng/asm/update/Log", "enterMethod", "()V", false);
            super.visitCode();
        }


        @Override
        public void visitInsn(int opcode) {
            if (opcode == ARETURN || opcode == RETURN) {
                mv.visitFieldInsn(GETFIELD, "cn/bucheng/asm/update/Demo", "log", "Lcn/bucheng/asm/update/Log;");
                mv.visitMethodInsn(INVOKEVIRTUAL, "cn/bucheng/asm/update/Log", "leaveMethod", "()V", false);
            }
            super.visitInsn(opcode);
        }


        @Override
        public void visitEnd() {
            mv.visitMaxs(6, 6);
            super.visitEnd();
        }
    }


    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        test2();
    }

    private static void test2() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String fullName = Demo.class.getName();
        String fullNameType = fullName.replace(".", "/");
        ClassReader cr = new ClassReader(fullNameType);
        ClassWriter cw = new ClassWriter(0);
        AddCodeVisitor cv = new AddCodeVisitor(ASM6, cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        byte[] bytes = cw.toByteArray();
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> cls = classLoader.definePublicClass(fullName, bytes, 0);

        Object o = cls.newInstance();
        Method getDemoInfo = cls.getMethod("getDemoInfo");
        getDemoInfo.invoke(o);
    }

}
