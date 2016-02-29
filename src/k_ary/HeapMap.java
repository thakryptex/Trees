package k_ary;

import java.util.ArrayList;

public class HeapMap<K extends Comparable<K>, V extends Comparable<V>> {

    private ArrayList<Node> tree;
    private int degree = 2;
    private int size;

    public HeapMap() {
        tree = new ArrayList<>();
        size = 0;
    }

    public void add(Node<K,V> node) {
        tree.add(node);
        size++;
    }

    public Node delete(Node<K,V> node) {
        int pos = search(node);
        if (pos == -1)
            return null;
        int[] children = getChildrenPos(pos);
        for (int i: children)
            tree.remove(i);
        return tree.remove(pos);
    }

    public int search(Node<K,V> node) {
        for (int i = 0; i < tree.size(); i++) {
            if (tree.get(i).key.compareTo(node.key) == 0)
                return i;
        }
        return -1;
    }




    public Node getRoot() {
        return tree != null ? tree.get(0) : null;
    }

    public int getParent(int child) {
        return calculateParent(child);
    }

    public int getChild(int parent, int numOfChild) {
        return calculateChildPos(parent, numOfChild);
    }

    private int[] getChildrenPos(int parent) {
        int[] pos = new int[degree];
        pos[0] = calculateChildPos(parent, 1);
        for (int i = 1; i < degree; i++) {
            pos[i] = pos[i-1] + 1;
        }
        return pos;
    }









    private int calculateParent(int child) {
        if (child <= 1) {
            throw new IllegalStateException("Node #1 is the root.");
        }
        return ((degree + child - 2) / degree) - 1;
    }

    private int calculateChildPos(int parent, int jth_child) {
        if (jth_child < 1 || jth_child >= degree) {
            throw new IllegalStateException("Node can't have more children than degree of tree.");
        }
        return degree*parent - (degree - 2) + jth_child + 2;
    }











    public void traversalPreorder() throws IllegalStateException {
        if (tree.size() == 0) throw new IllegalStateException("Tree is empty.");
        traversalPreorder(getRoot());
    }

    private void traversalPreorder(Node<K,V> node) {
        if (node != null) {
            visit(node);
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                Node<K,V> left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            for (int i = children.length/2; i < children.length; i++) {
                Node<K,V>  right;
                try {
                    right = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    right = null;
                }
                traversalPreorder(right);
            }
        }
    }

    public void traversalPostorder() {
        if (tree.size() == 0) throw new IllegalStateException("Tree is empty.");
        traversalPostorder(getRoot());
    }

    private void traversalPostorder(Node<K,V> node) {
        if (node != null) {
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                Node<K,V> left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            for (int i = children.length/2; i < children.length; i++) {
                Node<K,V>  right;
                try {
                    right = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    right = null;
                }
                traversalPreorder(right);
            }
            visit(node);
        }
    }

    public void traversalInorder() {
        if (tree.size() == 0) throw new IllegalStateException("Tree is empty.");
        traversalInorder(getRoot());
    }

    private void traversalInorder(Node<K,V> node) {
        if (node != null) {
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                Node<K,V>  left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            visit(node);
            for (int i = children.length/2; i < children.length; i++) {
                Node<K,V>  right;
                try {
                    right = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    right = null;
                }
                traversalPreorder(right);
            }
        }
    }

    public void visit(Node<K,V> node) {
        System.out.println(node);
    }



    public class Node<K extends Comparable<K>, V extends Comparable<V>> {

        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }


}