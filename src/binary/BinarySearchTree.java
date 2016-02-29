package binary;

import java.util.*;

public class BinarySearchTree<E extends Comparable<E>> {

    private Node<E> root;
    private int size;

    public BinarySearchTree() {
        size = 0;
        root = null;
    }

    public BinarySearchTree(Node<E> root) {
        size = 1;
        this.root = root;
    }

    public void add(E value) {
        if (root == null) {
            root = new Node<>(value);
            size++;
        } else {
            Node<E> node = findNode(value, root);
            if (value.compareTo(node.value) < 0) {
                node.left = new Node<>(value, node);
                size++;
            } else if (value.compareTo(node.value) > 0) {
                node.right = new Node<>(value, node);
                size++;
            } else {
                // do nothing, because we don't need duplicates
            }
        }
    }

    public void addAll(E[] array) {
        Arrays.sort(array);
        addAll(array, 0, array.length-1);
    }

    private void addAll(E[] array, int start, int finish) {
        //TODO I lose some information some how
        if (start < finish) {
            int middle = (finish + start) / 2;
            add(array[middle]);
            addAll(array, start, middle - 1);
            addAll(array, middle + 1, finish);
        } else if (start == finish)
            add(array[start]);
//        else
//            add(array[finish]);
    }

    public void delete(E value) {
        Node<E> node = find(value);
        if (node != null) {
            if (value.compareTo(node.value) == 0) {
                if (node.left == null && node.right == null) {
                    if (node.parent == null) {
                        root = null;
                        size--;
                    } else {
                        Node<E> parent = node.parent;
                        if (parent.left == node)
                            parent.left = null;
                        else
                            parent.right = null;
                        node.value = null;
                        size--;
                    }
                } else if (node.left == null && node.right != null)
                    successor(node);
                else
                    predecessor(node);
            }
        }
    }

    public Node<E> find(E value) {
        Node<E> node = findNode(value, root);
        if (value.compareTo(node.value) == 0)
            return node;
        return null;
    }

    private Node<E> findNode(E value, Node<E> node) {
        if (value.compareTo(node.value) < 0) {
            if (node.left != null)
                return findNode(value, node.left);
            else
                return node;
        } else if (value.compareTo(node.value) > 0) {
            if (node.right != null)
                return findNode(value, node.right);
            else
                return node;
        } else //if values equal
            return node;
    }

    public int size() {
        return size;
    }

    private void successor(Node<E> delNode) {
        Node<E> replaceNode = findMin(delNode.getRight());
        replacement(delNode, replaceNode);
        size--;
    }

    private void predecessor(Node<E> delNode) {
        Node<E> replaceNode = findMax(delNode.getLeft());
        replacement(delNode, replaceNode);
        size--;
    }

    private void replacement(Node<E> delNode, Node<E> replaceNode) {
        Node<E> parentDel = delNode.parent;
        Node<E> parentRep = replaceNode.parent;

        if (parentRep != delNode) {
            if (parentRep.left == replaceNode) {
                parentRep.left = null;
            } else {
                parentRep.right = null;
            }
        }

        replaceNode.parent = delNode.parent;

        if (parentDel != null) {
            if (parentDel.left == delNode) {
                parentDel.left = replaceNode;
            } else {
                parentDel.right = replaceNode;
            }
        } else {
            root = replaceNode;
        }

        if (replaceNode.left != null || replaceNode.right != null) {
            if (parentRep != delNode) {
                if (replaceNode.right != null && replaceNode.left == null) {
                    parentRep.left = replaceNode.right;
                }
                if (replaceNode.left != null && replaceNode.right == null) {
                    parentRep.right = replaceNode.left;
                }
            }
        }

        if (delNode.left != replaceNode)
            replaceNode.left = delNode.left;
        if (delNode.right != replaceNode)
            replaceNode.right = delNode.right;

        delNode = null;
    }

    private Node<E> findMin(Node<E> node) {
        if (node.left == null)
            return node;
        return findMin(node.left);
    }

    private Node<E> findMax(Node<E> node) {
        if (node.right == null)
            return node;
        return findMax(node.right);
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

    public int treeHeight() {
        return countHeight(root) - 1;
    }

    private int countHeight(Node<E> node) {
        if (node != null) {
            int left = countHeight(node.left);
            int right = countHeight(node.right);
            int res = left;
            if (left > right)
                res = left;
            else if (left < right)
                res = right;
            return res + 1;
        }
        return 0;
    }

    public void printTree(Node<E> root) {
        Queue<Node<E>> currLevel = new LinkedList<>();
        Queue<Node<E>> nextLevel = new LinkedList<>();
        currLevel.add(root);
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
        if (node.left == null) {
            return 1;
        }
        return countOffset(node.left) + 2;
    }

    public Node<E> getRoot() {
        return root;
    }

    public void visit(E data) {
        System.out.print(data + " ");
    }


    public class Node<E extends Comparable<E>> {
        protected E value;
        protected Node<E> parent;
        protected Node<E> left;
        protected Node<E> right;

        public Node(E value) {
            this.value = value;
            parent = null;
            left = null;
            right = null;
        }

        public Node(E value, Node<E> parent) {
            this.value = value;
            this.parent = parent;
            left = null;
            right = null;
        }

        public E getValue() {
            return value;
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

        public Node(E value, Node<E> parent, Node<E> left, Node<E> right) {
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            if (value == null)
                return "null";
            if (parent == null) {
                if (left == null && right == null)
                    return "Node value: " + value;
                else if (left != null && right == null)
                    return "Node value: " + value + ", left: " + left.value;
                else if (right != null && left == null)
                    return "Node value: " + value + ", right: " + right.value;
                else
                    return "Node value: " + value + ", left: " + left.value + ", right: " + right.value;
            } else {
                if (left == null && right == null)
                    return "Node value: " + value + ", parent: " + parent.value;
                else if (left != null && right == null)
                    return "Node value: " + value + ", parent: " + parent.value + ", left: " + left.value;
                else if (right != null && left == null)
                    return "Node value: " + value + ", parent: " + parent.value + ", right: " + right.value;
                else
                    return "Node value: " + value + ", parent: " + parent.value + ", left: " + left.value + ", right: " + right.value;
            }
        }
    }
}
