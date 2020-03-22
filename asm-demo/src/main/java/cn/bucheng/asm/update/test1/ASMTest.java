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
                        methodVisitor.visitVarInsn(ALOAD, 1);
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "beforeMethod", "(Ljava/lang/String;)V", false);
                        super.visitCode();
                    }

                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == ARETURN || opcode == RETURN) {
                            methodVisitor.visitVarInsn(ALOAD, 1);
                            methodVisitor.visitMethodInsn(INVOKESTATIC, "cn/bucheng/asm/update/test1/Logger", "afterMethod", "(Ljava/lang/String;)V", false);
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        };

        reader.accept(visitor, ClassReader.EXPAND_FRAMES);
        byte[] bytes = writer.toByteArray();

        MyClassLoader classLoader = new MyClassLoader();
        Class<?> cls = classLoader.definePublicClass(fullName, bytes, 0);
        Object o = cls.newInstance();
        Method getDemoInfo = cls.getMethod("getDemoInfo", String.class);
        getDemoInfo.invoke(o, "yinchong");

    }
}
