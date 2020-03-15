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
 * @create 2020/2/26 20:03
 * @description
 */
public class TreeNodeTest {

    public static void main(String[] args) {

    }


    static class TreeBin{
        private TreeNode root;

        public void addNode(int data){
            if(root==null){
                root = new TreeNode(data,null,null);
            }else{
                TreeNode head = root;
                TreeNode prev = root;
                while(head!=null){

                }
            }

        }
    }


    static class TreeNode{
        private int data;
        private TreeNode leftNode;
        private TreeNode rightNode;

        public TreeNode(int data, TreeNode leftNode, TreeNode rightNode) {
            this.data = data;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public TreeNode getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(TreeNode leftNode) {
            this.leftNode = leftNode;
        }

        public TreeNode getRightNode() {
            return rightNode;
        }

        public void setRightNode(TreeNode rightNode) {
            this.rightNode = rightNode;
        }
    }
}
