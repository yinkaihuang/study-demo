package cn.bucheng.asm.update.test1;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.bucheng.asm.update.MyClassLoader;
import org.objectweb.asm.*;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author yinchong
 * @create 2020/3/15 9:21
 * @description
 */
public class ASMTest {
    public static void main(String[] args) throws Exception {
        String fullName = Demo.class.getName();
        String fullNameType = fullName.replace(".", "/");
        ClassReader reader = new ClassReader(fullNameType);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (!name.equals("getDemoInfo")) {
                    return methodVisitor;
                }
                return new MethodVisitor(ASM7, methodVisitor) {
                    @Override
                    public void visitCode() {
//                        Label label0 = new Label();
//                        methodVisitor.visitLabel(label0);
//                        methodVisitor.visitLineNumber(8, label0);
//                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "beforeMethod", "()V", false);
//                        Label label1 = new Label();
//                        methodVisitor.visitLabel(label1);
//                        methodVisitor.visitLineNumber(9, label1);
//                        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                        methodVisitor.visitLdcInsn("getDemoInfo\u88ab\u8c03\u7528...");
//                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//                        Label label2 = new Label();
//                        methodVisitor.visitLabel(label2);
//                        methodVisitor.visitLineNumber(10, label2);
//                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "afterMethod", "()V", false);
//                        Label label3 = new Label();
//                        methodVisitor.visitLabel(label3);
//                        methodVisitor.visitLineNumber(11, label3);
//                        methodVisitor.visitInsn(RETURN);
//                        Label label4 = new Label();
//                        methodVisitor.visitLabel(label4);
//                        methodVisitor.visitLocalVariable("this", "Lcn/bucheng/asm/update/test1/Demo;", null, label0, label4, 0);
//                        methodVisitor.visitMaxs(2, 1);
//                        methodVisitor.visitEnd();

                        Label label0 = new Label();
                        methodVisitor.visitLabel(label0);
                        methodVisitor.visitLineNumber(8, label0);
                        methodVisitor.visitVarInsn(ALOAD, 1);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "beforeMethod", "(Ljava/lang/String;)V", false);
                        Label label1 = new Label();
                        methodVisitor.visitLabel(label1);
                        methodVisitor.visitLineNumber(9, label1);
                        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
                        methodVisitor.visitInsn(DUP);
                        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                        methodVisitor.visitLdcInsn("getDemoInfo\u88ab\u8c03\u7528...:");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                        methodVisitor.visitVarInsn(ALOAD, 1);
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                        Label label2 = new Label();
                        methodVisitor.visitLabel(label2);
                        methodVisitor.visitLineNumber(10, label2);
                        methodVisitor.visitVarInsn(ALOAD, 1);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "afterMethod", "(Ljava/lang/String;)V", false);
                        Label label3 = new Label();
                        methodVisitor.visitLabel(label3);
                        methodVisitor.visitLineNumber(11, label3);
                        methodVisitor.visitInsn(RETURN);
                        Label label4 = new Label();
                        methodVisitor.visitLabel(label4);
                        methodVisitor.visitLocalVariable("this", "Lcn/bucheng/asm/update/test1/Demo;", null, label0, label4, 0);
                        methodVisitor.visitLocalVariable("name", "Ljava/lang/String;", null, label0, label4, 1);
                        methodVisitor.visitMaxs(10, 10);
                        methodVisitor.visitEnd();
                    }
                };
            }
        };

        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        byte[] bytes = writer.toByteArray();

        MyClassLoader classLoader = new MyClassLoader();
        Class<?> cls = classLoader.definePublicClass(fullName, bytes, 0);
        Object o = cls.newInstance();
        Method getDemoInfo = cls.getMethod("getDemoInfo",String.class);
        getDemoInfo.invoke(o,"yinchong");

    }
}
