package com.rwin.collection;

public class Node<E> {

    private double x;
    private double y;
    private double w;
    private double h;
    private Node<E> opt_parent;
    private Point<E> point;
    private NodeType nodetype = NodeType.EMPTY;
    private Node<E> nw;
    private Node<E> ne;
    private Node<E> sw;
    private Node<E> se;

    /**
     * Constructs a new quad tree node.
     * 
     * @param {double} x X-coordiate of node.
     * @param {double} y Y-coordinate of node.
     * @param {double} w Width of node.
     * @param {double} h Height of node.
     * @param {Node<E>} opt_parent Optional parent node.
     * @constructor
     */
    public Node(double x, double y, double w, double h, Node<E> opt_parent) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.opt_parent = opt_parent;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Node<E> getParent() {
        return opt_parent;
    }

    public void setParent(Node<E> opt_parent) {
        this.opt_parent = opt_parent;
    }

    public void setPoint(Point<E> point) {
        this.point = point;
    }

    public Point<E> getPoint() {
        return this.point;
    }

    public void setNodeType(NodeType nodetype) {
        this.nodetype = nodetype;
    }

    public NodeType getNodeType() {
        return this.nodetype;
    }

    public void setNw(Node<E> nw) {
        this.nw = nw;
    }

    public void setNe(Node<E> ne) {
        this.ne = ne;
    }

    public void setSw(Node<E> sw) {
        this.sw = sw;
    }

    public void setSe(Node<E> se) {
        this.se = se;
    }

    public Node<E> getNe() {
        return ne;
    }

    public Node<E> getNw() {
        return nw;
    }

    public Node<E> getSw() {
        return sw;
    }

    public Node<E> getSe() {
        return se;
    }
}
