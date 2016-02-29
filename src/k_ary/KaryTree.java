package k_ary;

import java.util.ArrayList;

public class KaryTree<E extends Comparable<E>> {

    private ArrayList<E> tree;
    private int degree;
    private int size;

    public KaryTree(int k) {
        tree = new ArrayList<>();
        tree.ensureCapacity(100);
        degree = k;
        size = 0;
    }

    public void add(E value) {
        tree.add(value);
        size++;
    }

    public E delete(E value) {
        int pos = search(value);
        if (pos == -1)
            return null;
        int[] children = getChildrenPos(pos);
        for (int i: children)
            tree.remove(i);
        return tree.remove(pos);
    }

    public int search(E value) {
        for (int i = 0; i < tree.size(); i++) {
            if (tree.get(i).compareTo(value) == 0)
                return i;
        }
        return -1;
    }

    public E getRoot() {
        return tree != null ? tree.get(0) : null;
    }

    public int getParent(int child) {
        return calculateParent(child);
    }

    private int calculateParent(int child) {
        if (child <= 1) {
            throw new IllegalStateException("Node #1 is the root.");
        }
        return ((degree + child - 2) / degree) - 1;
    }

    public int getChild(int parent, int numOfChild) {
        return calculateChildPos(parent, numOfChild);
    }

    private int calculateChildPos(int parent, int jth_child) {
        if (jth_child < 1 || jth_child >= degree) {
            throw new IllegalStateException("Node can't have more children than degree of tree.");
        }
        return degree*parent - (degree - 2) + jth_child + 2;
    }

    private int[] getChildrenPos(int parent) {
        int[] pos = new int[degree];
        pos[0] = calculateChildPos(parent, 1);
        for (int i = 1; i < degree; i++) {
            pos[i] = pos[i-1] + 1;
        }
        return pos;
    }

    public void traversalPreorder() throws IllegalStateException {
        if (tree.size() == 0) throw new IllegalStateException("Tree is empty.");
        traversalPreorder(getRoot());
    }

    private void traversalPreorder(E node) {
        if (node != null) {
            visit(node);
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                E left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            for (int i = children.length/2; i < children.length; i++) {
                E right;
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

    private void traversalPostorder(E node) {
        if (node != null) {
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                E left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            for (int i = children.length/2; i < children.length; i++) {
                E right;
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

    private void traversalInorder(E node) {
        if (node != null) {
            int[] children = getChildrenPos(tree.indexOf(node));
            for (int i = 0; i < children.length/2; i++) {
                E left;
                try {
                    left = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    left = null;
                }
                traversalPreorder(left);
            }
            visit(node);
            for (int i = children.length/2; i < children.length; i++) {
                E right;
                try {
                    right = tree.get(children[i]);
                } catch(IndexOutOfBoundsException e) {
                    right = null;
                }
                traversalPreorder(right);
            }
        }
    }

    public void visit(E node) {
        System.out.println(node);
    }
}