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
 * @create 2020/2/26 20:22
 * @description
 */
public class Test {
    public static void main(String[] args) {
        int[] array = new int[]{1,3,2,0,6,5};
        int index = minIndex(array);
        int rootData = array[index];
        aescArray(array,0,index-1);
        aescArray(array,index+1,array.length-1);
        TreeBin bin = new TreeBin();
        bin.addRoot(rootData);
        for(int i=0;i<index;i++){
            bin.addLeft(array[i]);
        }

        for(int i=index+1;i<array.length-1;i++){
            bin.addRight(array[i]);
        }
    }

    //获取数组中最小数据索引位置
    public static int minIndex(int[] array) {
        int minIndex = 0;
        int minValue = array[minIndex];
        int length = array.length;
        for (int i = 1; i < length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    //快速排序
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

    static class TreeBin {
        private TreeNode root;

        public void addRoot(int data) {
            root = new TreeNode(data, null, null);
        }

        public void addLeft(int data) {
            TreeNode temp = root;
            TreeNode prev = root;
            int number = 0;
            while (temp != null) {
                prev = temp;
                if (number % 2 == 0) {
                    temp = temp.leftChild;
                } else {
                    temp = temp.rigthChild;
                }
                number++;
            }
            if (number % 2 == 0) {
                prev.rigthChild = new TreeNode(data, null, null);
            } else {
                prev.leftChild = new TreeNode(data, null, null);
            }
        }

        public void addRight(int data) {
            TreeNode temp = root;
            TreeNode prev = root;
            int number = 0;
            while (temp != null) {
                prev = temp;
                if (number % 2 == 0) {
                    temp = temp.rigthChild;
                } else {
                    temp = temp.leftChild;
                }
                number++;
            }
            if (number % 2 == 0) {
                prev.leftChild = new TreeNode(data, null, null);
            } else {
                prev.rigthChild = new TreeNode(data, null, null);
            }
        }
    }

    static class TreeNode {
        private int data;
        private TreeNode leftChild;
        private TreeNode rigthChild;

        public TreeNode(int data, TreeNode leftChild, TreeNode rigthChild) {
            this.data = data;
            this.leftChild = leftChild;
            this.rigthChild = rigthChild;
        }
    }
}
