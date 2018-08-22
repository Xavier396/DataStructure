package com.hongdeyan.list;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * 自己实现ArrayList
 */
public class EgdwArrayList<E> {

    private Object[] elements;

    private int size;
    // 记录修改次数
    private int modCount;
    // 默认初始大小
    private static final int DEFAULT_SIZE = 10;
    // 默认扩大大小
    private static final double DEFAULT_GROW_SIZE = 1.5;
    // 默认回收空间大小
    private static final double DEFAULT_RELEASE_SIZE = 4;

    /**
     * 构造函数传入初始elment大小
     *
     * @param size 初始大小
     */
    public EgdwArrayList(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size can't small 0");
        }
        elements = new Object[size];
    }

    public EgdwArrayList() {
        //如果不设置的话就默认为10
        this(EgdwArrayList.DEFAULT_SIZE);
    }

    /**
     * 获取当前的size大小
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 返回当前的容量大小
     *
     * @return 当前容量大小
     */
    public long getOpcaity() {
        return this.size;
    }

    /**
     * 判断是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 根据index值获取相应的值
     *
     * @param index
     * @return 返回的对象
     */
    public E get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index can not small 0");
        }
        if (index > size) {
            throw new IllegalArgumentException("out of EgdwArraylist size!");
        }
        return (E) elements[index];
    }

    /**
     * 添加元素
     *
     * @param element 元素
     * @return 添加进入时候的索引
     */
    public int add(E element) {
        ensureExplicitCapacity();
        modCount++;
        return addLast(element);
    }

    /**
     * 添加元素到数组最后
     *
     * @param element 添加的元素
     * @return 返回添加进入时候的索引值
     */
    public int addLast(E element) {
        ensureExplicitCapacity();
        elements[size++] = element;
        modCount++;
        return size - 1;
    }

    /**
     * 判断容量是否不够用
     */
    private void ensureExplicitCapacity() {
        int spaceSize = elements.length - size;
        if (spaceSize == 0) {
            //说明数据已经满了或快满了.需要扩大
            grow();
        }
    }

    /**
     * 判断数组是否空闲太多.进行回收
     */
    private void ensureExplicitCapacityRelease() {
        if (size > 0 && elements.length / size > EgdwArrayList.DEFAULT_RELEASE_SIZE) {
            //如果小于某个值说明空间浪费太多.
            decline();
        }
    }

    /**
     * 减少容量
     */
    private void decline() {
        Object[] objs = new Object[elements.length / 2];
        for (int i = 0; i < size; i++) {
            objs[i] = elements[i];
            elements[i] = null;
        }
        elements = objs;
    }

    /**
     * 增加容量
     */
    private void grow() {
        Object[] arrs = new Object[(int) (size * EgdwArrayList.DEFAULT_GROW_SIZE)];
        for (int i = 0; i < elements.length; i++) {
            arrs[i] = elements[i];
            //手动清空,让GC去清空数据
            elements[i] = null;
        }
        elements = null;
        elements = arrs;
        modCount++;
    }

    /**
     * 添加到指定索引
     *
     * @param element 数据
     * @param index   置顶索引
     * @return 是否成功
     */
    public boolean add(E element, int index) {
        ensureExplicitCapacity();
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index small 0 or index big elements size");
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
        modCount++;
        return true;
    }

    /**
     * 设置指定索引的值
     *
     * @param element 数据
     * @param index   指定索引
     * @return
     */
    public boolean set(E element, int index) {
        if (index > size) {
            throw new IllegalArgumentException("index too big");
        }
        elements[index] = element;
        modCount++;
        return true;
    }

    /**
     * 弹出末尾最后一位数据
     *
     * @return
     */
    public boolean pop() {
        if (size > 0) {
            System.out.println("size > 0");
            elements[(size--) - 1] = null;
        }
        modCount++;
        ensureExplicitCapacityRelease();
        return true;
    }

    /**
     * 移除某个地方相同的数据
     *
     * @param element 数据
     * @return 是否完成
     */
    public E remove(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return removeIndex(i);
            }
        }
        return null;
    }


    /**
     * 移除指定索引位的数据
     *
     * @param index 索引
     * @return 是否完成
     */
    public E removeIndex(int index) {
        if (index > size || index < 0) {
            throw new IllegalArgumentException("index too small or too big");
        }
        Object temp = elements[index];
        for (int i = index; i < size; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size] = null;
        size--;
        modCount++;
        ensureExplicitCapacityRelease();
        return (E) temp;
    }


    public E removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException("no element!");
        }
        E element = (E) elements[size - 1];
        elements[size-1] = null;
        size--;
        modCount++;
        ensureExplicitCapacityRelease();
        return element;
    }


    /**
     * 是否包含某个元素
     *
     * @param element 元素
     * @return 是否
     */
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 清空arrylist数据
     */
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * 返回指定元素的当前索引
     *
     * @param element 元素
     * @return 当前索引
     */
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EgdwArrayList[");
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(elements[i]);
            } else {
                sb.append(elements[i]).append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public Iterator<E> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<E> {

        private int itrCursor;
        private int itrSize;


        @Override
        public boolean hasNext() {
            if (itrSize < size) {
                return true;
            }
            return false;
        }

        @Override
        public E next() {
            return (E) elements[itrSize++];
        }

        @Override
        public void remove() {
            EgdwArrayList.this.removeIndex(--itrSize);
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {

        }
    }
}
