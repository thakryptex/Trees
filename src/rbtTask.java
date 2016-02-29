import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class rbtTask<E extends Comparable<E>> {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new File("rbt.in"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("rbt.out"));
        StringBuilder sb = new StringBuilder();

        String line1 = scanner.nextLine().trim();
        String line2 = scanner.nextLine().trim();

        rbtTask<Integer> rbt = new rbtTask<>();
        List<Integer> ints2add = null, ints2test = null;

        ints2add = Stream.of(line1.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
        ints2test = Stream.of(line2.split(" ")).map(Integer::valueOf).collect(Collectors.toList());

        ints2add.forEach(rbt::add);

        rbt.printTree();

        ints2test.forEach(i -> {
            Node<Integer> node = rbt.find(i);
            Node<Integer> right = (node == null) ? null : node.getRight();
            sb.append((right == null) ? "null" : right.getValue());
            sb.append(" ");
        });

        sb.deleteCharAt(sb.length() - 1);

        bw.append(sb.toString());
        bw.close();

    }

    private int size = 0;
    private Node<E> root;

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
            root = new Node<>(value, Node.BLACK);
            size++;
            return root;
        }

        Node<E> newNode = new Node<>(value, Node.RED);

        if (value.compareTo(node.getValue()) < 0) {
            if (!node.hasLeft()) {
                node.setLeft(newNode);
                newNode.setParent(node);
                size++;
                balanceInsert(newNode);
            } else
                add(value, node.getLeft());
        } else if (value.compareTo(node.getValue()) > 0) {
            if (!node.hasRight()) {
                node.setRight(newNode);
                newNode.setParent(node);
                size++;
                balanceInsert(newNode);
            } else
                add(value, node.getRight());
        }

//        balanceInsert(newNode);
        return newNode;
    }

    private void balanceInsert(Node<E> node) {

        if (node != root) {
            if (node.isRed() && node.parent.isRed()) {

                Node<E> grand = node.getGrandparent();
                Node<E> parent = node.getParent();
                Node<E> uncle = node.getUncle();

                if (parent.isRed() && uncle.isRed()) {
                    grand.makeRed();
                    parent.makeBlack();
                    uncle.makeBlack();
                    balanceInsert(grand);
                } else if (parent.isRed() && uncle.isBlack()) {

                    if (node == parent.getRight() && parent == grand.getLeft()) {
                        rotateLeft(node);
                    } else if (node == parent.getRight() && parent == grand.getRight()) {
                        rotateLeft(grand);
                        grand.makeRed();
                        parent.makeBlack();
                    } else if (node == parent.getLeft() && parent == grand.getRight()) {
                        rotateRight(node);
                    } else if (node == parent.getLeft() && parent == grand.getLeft()) {
                        rotateRight(grand);
                        grand.makeRed();
                        parent.makeBlack();
                    }

                }
//                checkGrandparent(grand);
            }
        } else
            node.makeBlack();
    }

//    private void checkGrandparent(Node<E> grand) {
//        Node<E> parent = grand.parent;
//        if (parent != null) {
//            if (grand.isRed() && parent.isRed())
//                balanceInsert(grand);
//        }
//    }

    private Node<E> rotateRight(Node<E> node) {
        Node<E> temp = node.getLeft();
        Node<E> parent = node.getParent();
        node.setLeft(temp.getRight());

        if (temp.hasRight()) {
            temp.getRight().setParent(node);
        }

        temp.setParent(parent);

        if (node.hasParent()) {
            if (parent.hasLeft() && parent.getLeft().equals(node))
                parent.setLeft(temp);
            else
                parent.setRight(temp);
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
        Node<E> parent = node.getParent();
        node.setRight(temp.getLeft());

        if (temp.hasLeft()) {
            temp.getLeft().setParent(node);
        }

        temp.setParent(parent);

        if (node.hasParent()) {
            if (parent.hasLeft() && parent.getLeft().equals(node))
                parent.setLeft(temp);
            else
                parent.setRight(temp);
        } else {
            root = temp;
        }

        temp.setLeft(node);
        node.setParent(temp);

        fixHeight(node);
        fixHeight(temp);
        return temp;
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
                balanceInsert(node);
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
            balanceInsert(node);
            size--;
            return delNode.getValue();
        } else if (!delNode.hasLeft() && delNode.hasRight()) {   // if target has only right child
            if (delNode.value.compareTo(delNode.getParent().value) < 0) {
                delNode.getParent().setLeft(delNode.right);
            } else {
                delNode.getParent().setRight(delNode.right);
            }
            balanceInsert(node);
            size--;
            return delNode.value;
        }

        Node<E> tmpNode = predecessor(delNode);          // if target has two children
        delNode.setValue(tmpNode.value);
        tmpNode.getParent().setRight(null);
        balanceInsert(node);
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

    public void printTree() {
        Node<E> node = root;
        Queue<Node<E>> currLevel = new LinkedList<>();
        Queue<Node<E>> nextLevel = new LinkedList<>();
        currLevel.add(node);
        int off;
        while (!currLevel.isEmpty()) {
            off = 1;
            for (Node<E> currNode: currLevel) {
                if (currNode != null) {
                    if (currNode.hasLeft())
                        nextLevel.add(currNode.left);
                    else
                        nextLevel.add(null);
                    if (currNode.hasRight())
                        nextLevel.add(currNode.right);
                    else
                        nextLevel.add(null);
                    int offset = 1;
                    if (off == 1) {
                        offset = countOffset(currNode) + 1;
                        off = 0;
                    }
                    System.out.print(String.format("%" + offset + "s", "  ") + currNode.value + " " + (currNode.color == 0 ? "R" : "B"));
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
        public static int BLACK = 1;
        public static int RED = 0;

        private E value;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;
        int height = 1;
        int color;

        public Node() {
            color = BLACK;
        }

        public Node(E value) {
            this.value = value;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.color = RED;
        }

        protected Node(E value, int color) {
            this.value = value;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.color = color;
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

        public boolean isLeaf() {
            return !hasLeft() && !hasRight();
        }

        public boolean isNIL() {
            return value == null;
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

        public Node<E> getGrandparent() {
            return hasParent() ? parent.getParent() : new Node<>();
        }

        public Node<E> getUncle() {
            Node<E> grand = getGrandparent();
            if (grand != null) {
                if (grand.left == parent) {
                    return grand.right != null ? grand.right : new Node<>();
                    } else
                if (grand.right == parent) {
                    return grand.left != null ? grand.left : new Node<>();
                }
            }
            return new Node<>();
        }

        public int getColor() {
            return color;
        }

        public boolean isBlack() {
            return color == BLACK;
        }

        public boolean isRed() {
            return color == RED;
        }

        public void makeBlack() {
            color = BLACK;
        }

        public void makeRed() {
            color = RED;
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