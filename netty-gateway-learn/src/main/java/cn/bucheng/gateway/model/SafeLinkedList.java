package cn.bucheng.gateway.model;


import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author yinchong
 * @create 2020/4/26 20:45
 * @description
 */
public class SafeLinkedList<T> extends LinkedList<T> {

    @Override
    public T removeFirst() {
        try {
            return super.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
