package com.rwin.collection;
public interface Func<E> {
    public void call(QuadTree<E> quadTree, Node<E> node);
}
