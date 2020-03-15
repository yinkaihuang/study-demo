package cn.bucheng.simple;
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

/**
 * @author yinchong
 * @create 2020/2/26 19:22
 * @description
 */
public class ThreadTest3 {
    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();
        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printA();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printB();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printC();
            }
        }).start();
    }

    static class PrintABC {
        int flag = 0;


        public synchronized void printA() {
            while (true) {

                while (flag != 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("A");
                flag = 1;
                this.notifyAll();
            }

        }


        public synchronized void printB() {
            while (true) {

                while (flag != 1) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("B");
                flag = 2;
                this.notifyAll();
            }

        }


        public synchronized void printC() {
            while (true) {

                while (flag != 2) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("C");
                flag = 0;
                this.notifyAll();
            }

        }

    }
}
