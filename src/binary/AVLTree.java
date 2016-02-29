package binary;

import java.util.LinkedList;
import java.util.Queue;

public class AVLTree<E extends Comparable<E>> {

    private int size = 0;
    private Node<E> root = null;

    public Node<E> find(E value) {
        return find(value, root);
    }

    private Node<E> find(E value, Node<E> node) {
        if (node != null) {
            E currValue = node.value;
            if (value.compareTo(currValue) == 0)
                return node;
            if (value.compareTo(currValue) < 0)
                return find(value, node.left);
            else if (value.compareTo(currValue) > 0)
                return find(value, node.right);
        }
        return null;
    }

    public void add(E value) {
        add(value, root);
    }

    private Node<E> add(E value, Node<E> node) {
        if (size == 0) {
            root = new Node<E>(value);
            size++;
            return root;
        }

        Node<E> newNode = new Node<E>(value);

        if (value.compareTo(node.getValue()) < 0) {
            if (!node.hasLeft()) {
                node.setLeft(newNode);
                newNode.setParent(node);
                size++;
            } else
                add(value, node.getLeft());
        } else if (value.compareTo(node.getValue()) > 0) {
            if (!node.hasRight()) {
                node.setRight(newNode);
                newNode.setParent(node);
                size++;
            } else
                add(value, node.getRight());
        }

        balance(node);
        return newNode;
    }

    public boolean remove(E value) {
        return remove(value, root) != null;
    }

    private E remove(E value, Node<E> node) {
        if (node == null)
            return null;
        Node<E> delNode = find(value, node);
        if (delNode == null)
            return null;

        if (!delNode.hasLeft() && !delNode.hasRight()) {           // if target is a leaf
            if (delNode.value.compareTo(delNode.getParent().value) < 0)
                delNode.getParent().setLeft(null);
            else
                delNode.getParent().setRight(null);
            {
                balance(node);
                size--;
                return delNode.value;
            }
        }

        if ((delNode.hasLeft() && !delNode.hasRight())) {      // if target has only left child
            if (delNode.value.compareTo(delNode.getParent().value) < 0) {
                delNode.getParent().setLeft(delNode.left);
            } else {
                delNode.getParent().setRight(delNode.left);
            }
            balance(node);
            size--;
            return delNode.getValue();
        } else if (!delNode.hasLeft() && delNode.hasRight()) {   // if target has only right child
            if (delNode.value.compareTo(delNode.getParent().value) < 0) {
                delNode.getParent().setLeft(delNode.right);
            } else {
                delNode.getParent().setRight(delNode.right);
            }
            balance(node);
            size--;
            return delNode.value;
        }

        Node<E> tmpNode = predecessor(delNode);          // if target has two children
        delNode.setValue(tmpNode.value);
        tmpNode.getParent().setRight(null);
        balance(node);
        size--;
        return delNode.value;
    }

    private Node<E> successor(Node<E> successor) {
        return findMin(successor.right);
    }

    private Node<E> predecessor(Node<E> predecessor) {
        return findMax(predecessor.left);
    }

    private Node<E> findMin(Node<E> node) {
        if (!node.hasLeft())
            return node;
        return findMin(node.left);
    }

    private Node<E> findMax(Node<E> node) {
        if (!node.hasRight())
            return node;
        return findMax(node.right);
    }

    private int bfactor(Node<E> node) {
        if (node == null)
            return 0;
        int left = getHeight(node.left);
        int right = getHeight(node.right);
        return left - right;
    }

    private Node<E> balance(Node<E> node) {
        fixHeight(node);
        if (bfactor(node) == 2) {
            if (bfactor(node.getLeft()) < 0) {
                rotateLeft(node.getLeft());
            }
            return rotateRight(node);
        }
        if (bfactor(node) == -2) {
            if (bfactor(node.getRight()) > 0) {
                rotateRight(node.getRight());
            }
            return rotateLeft(node);
        }
        return node;
    }

    private Node<E> rotateRight(Node<E> node) {
        Node<E> temp = node.getLeft();
        node.setLeft(temp.getRight());

        if (temp.hasRight()) {
            temp.getRight().setParent(node);
        }

        temp.setParent(node.getParent());

        if (node.hasParent()) {
            if (node.getParent().hasLeft() && node.getParent().getLeft().equals(node))
                node.getParent().setLeft(temp);
            else
                node.getParent().setRight(temp);
        } else {
            root = temp;
        }

        temp.setRight(node);
        node.setParent(temp);

        fixHeight(node);
        fixHeight(temp);
        return node;
    }

    private Node<E> rotateLeft(Node<E> node) {
        Node<E> temp = node.getRight();
        node.setRight(temp.getLeft());

        if (temp.hasLeft()) {
            temp.getLeft().setParent(node);
        }

        temp.setParent(node.getParent());

        if (node.hasParent()) {
            if (node.getParent().hasLeft() && node.getParent().getLeft().equals(node))
                node.getParent().setLeft(temp);
            else
                node.getParent().setRight(temp);
        } else {
            root = temp;
        }

        temp.setLeft(node);
        node.setParent(temp);

        fixHeight(node);
        fixHeight(temp);
        return temp;
    }

    public int treeHeight() {
        return getHeight(root);
    }

    public int getHeight(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    private void fixHeight(Node<E> node) {
        Node<E> leftNode = node.getLeft();
        Node<E> rightNode = node.getRight();
        int hLeft = getHeight(leftNode);
        int hRight = getHeight(rightNode);
        node.height = (hLeft > hRight ? hLeft : hRight) + 1;
    }

    public int size() {
        return size;
    }

    public void traversalPreorder() throws IllegalStateException {
        if (size == 0) throw new IllegalStateException("Tree is empty.");
        traversalPreorder(root);
    }

    public void traversalPostorder() {
        if (size == 0) throw new IllegalStateException("Tree is empty.");
        traversalPostorder(root);
    }

    public void traversalInorder() {
        if (size == 0) throw new IllegalStateException("Tree is empty.");
        traversalInorder(root);
    }

    private void traversalPreorder(Node<E> node) {
        if (node != null) {
            visit(node.value);
            traversalPreorder(node.left);
            traversalPreorder(node.right);
        }
    }

    private void traversalPostorder(Node<E> node) {
        if (node != null) {
            traversalPostorder(node.left);
            traversalPostorder(node.right);
            visit(node.value);
        }
    }

    private void traversalInorder(Node<E> node) {
        if (node != null) {
            traversalInorder(node.left);
            visit(node.value);
            traversalInorder(node.right);
        }
    }

    private void visit(E value) {
        System.out.println(value.toString());
    }

    public void printTree(Node<E> node) {
        Queue<Node<E>> currLevel = new LinkedList<>();
        Queue<Node<E>> nextLevel = new LinkedList<>();
        currLevel.add(node);
        int off;
        while (!currLevel.isEmpty()) {
            off = 1;
            for (Node<E> currNode: currLevel) {
                if (currNode != null) {
                    if (currNode.left != null)
                        nextLevel.add(currNode.left);
                    else
                        nextLevel.add(null);
                    if (currNode.right != null)
                        nextLevel.add(currNode.right);
                    else
                        nextLevel.add(null);
                    int offset = 1;
                    if (off == 1) {
                        offset = countOffset(currNode) + 1;
                        off = 0;
                    }
                    System.out.print(String.format("%" + offset + "s", "  ") + currNode.value + " ");
                } else
                    System.out.print("  ");
            }
            System.out.println();
            currLevel = nextLevel;
            nextLevel = new LinkedList<>();
        }
    }

    private int countOffset(Node<E> node) {
        if (!node.hasLeft()) {
            return 1;
        }
        return countOffset(node.left) + 2;
    }

    public Node<E> getRoot() {
        return root;
    }




    protected static class Node<E extends Comparable<E>> {
        private E value;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;
        int height = 1; // height stored for each node

        public Node(E value) {
            this.value = value;
            this.parent = null;
            this.left = null;
            this.right = null;
        }

        public Node(E value, Node<E> parent, Node<E> left, Node<E> right) {
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public E getValue() {
            return value;
        }

        public boolean hasParent() {
            return parent != null;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public boolean hasRight() {
            return right != null;
        }

        public Node<E> getParent() {
            return parent;
        }

        public Node<E> getLeft() {
            return left;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public void setParent(Node<E> parent) {
            this.parent = parent;
        }

        public void setLeft(Node<E> left) {
            this.left = left;
        }

        public void setRight(Node<E> right) {
            this.right = right;
        }
    }
}