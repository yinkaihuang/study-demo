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
 * @create 2020/2/26 19:28
 * @description
 */
public class SortListTest {

    public static void main(String[] args) {
        int[] temp = new int[]{10, 5, 7, 2, 6, 4, 9, 1, 11, 23, 0, 5};
//        aescArray(temp, 0, temp.length - 1);
        sortArray(temp);
        System.out.println(temp);
    }


    public static void sortArray(int[] temp) {
        int length = temp.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = length - 1; j > i; j--) {
                int data = temp[j];
                if (temp[j - 1] > data) {
                    temp[j] = temp[j - 1];
                    temp[j - 1] = data;
                }
            }
        }
    }


    public static void aescArray(int[] temp, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }

        int normal = temp[startIndex];
        int rightIndex = endIndex;
        int leftIndex = startIndex;
        for (; ; ) {
            while (temp[rightIndex] >= normal && rightIndex > leftIndex) {
                rightIndex--;
            }
            temp[leftIndex] = temp[rightIndex];
            while (temp[leftIndex] < normal && leftIndex < rightIndex) {
                leftIndex++;
            }
            temp[rightIndex] = temp[leftIndex];
            if (rightIndex <= leftIndex) {
                temp[rightIndex] = normal;
                break;
            }
        }

        aescArray(temp, startIndex, rightIndex - 1);
        aescArray(temp, rightIndex + 1, endIndex);
    }


}
