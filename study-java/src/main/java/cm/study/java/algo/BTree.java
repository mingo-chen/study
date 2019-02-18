package cm.study.java.algo;

/**
 * B树
 *
 *
 */
public class BTree<T> {

    private static class Node<T> {
        private T data;

        private Node<T> left;

        private Node<T> right;
    }

    private Node<T> root;

    public BTree() {

    }

    public Node<T> init(T rootValue) {
        root = new Node<>();
        root.data = rootValue;

        return root;
    }

    public Node<T> addLeft(Node<T> target, T value) {
        Node<T> newLeftNode = new Node<>();
        newLeftNode.data = value;
        target.left = newLeftNode;

        return newLeftNode;
    }

    public Node<T> addRight(Node<T> target, T value) {
        Node<T> newRightNode = new Node<>();
        newRightNode.data = value;
        target.right = newRightNode;

        return newRightNode;
    }
}
