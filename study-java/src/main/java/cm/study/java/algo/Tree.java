package cm.study.java.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 树
 * A->B->C
 * A->E->F
 */
public class Tree<T> {

    private static Logger ILOG = LoggerFactory.getLogger(Tree.class);

    private static class Node<T> {
        private T data;

        private Node<T> parent; // 父结点

        private List<Node<T>> sons;  // 子结点

        @Override
        public String toString() {
            return "Node{" +
                   "data=" + data +
//                   ", parent=" + parent +
//                   ", sons=" + sons +
                   '}';
        }
    }

    /**
     * 根结点
     */
    private Node<T> root;

    /**
     * 遍历模式
     * true -> 深度优先
     * false -> 广度优先
     */
    private boolean mode;

    public Tree(boolean mode) {
        this.mode = mode;
    }

    public Node<T> init(T value) {
        root = new Node<>();
        root.data = value;
        root.parent = null;
        root.sons = new ArrayList<>();
        return root;
    }

    public Node<T> addSon(Node<T> node, T value) {
        Node<T> sonNode = new Node<>();
        sonNode.data = value;
        sonNode.parent = node;
        sonNode.sons = new ArrayList<>();

        node.sons.add(sonNode);
        return sonNode;
    }

    @Override
    public String toString() {
        if (mode) {
            return depthFirst();
        } else {
            return breadthFirst();
        }
    }

    /**
     *
     *               A
     *            /  |   \
     *         /     |     \
     *      B1       B2      B3
     *     / \      / \     /  \
     *   C1a C1b  C2a C2b  C3a C3b
     *
     * 深度前序遍历: A # B1 # C1a C1b # B2 # C2a C2b # B3 # C3a C3b #
     * 深度后序遍历: C1a C1b # B1 # C2a C2b # B2 # C3a C3b # B3 # A #
     */
    public String depthFirst() {
        return depthFirst0(root);
    }

    String depthFirst0(Node<T> node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.data);
        sb.append(" # ");

        for (Node<T> son : node.sons) {
            if(son.sons.isEmpty()) {
                sb.append(son.data).append(" ");
            } else {
                String subTree = depthFirst0(son);
                sb.append(subTree);
                sb.append("# ");
                ILOG.info("sub node: {}, sub output: {}, output: {}", son, subTree, sb.toString());
            }

        }

        ILOG.info("node: {}, output: {}", node, sb.toString());
        return sb.toString();
    }

    /**
     *               A
     *            /  |   \
     *         /     |     \
     *      B1       B2      B3
     *     / \      / \     /  \
     *   C1a C1b  C2a C2b  C3a C3b
     *
     * 广度遍历: A B1 B2 B3 C1a C1b C2a C2b C3a C3b
     *
     */
    public String breadthFirst() {
        StringBuilder sb = new StringBuilder();

        if(root != null) {
            List<Node<T>> ret1 = breadthFirst0(Arrays.asList(root), sb);
            while(!ret1.isEmpty()) {
                ret1 = breadthFirst0(ret1, sb);
            }

        } else {
            sb.append("@");
        }

        return sb.toString();
    }

    List<Node<T>> breadthFirst0(List<Node<T>> layerX, StringBuilder sb) {
        List<Node<T>> sons = new ArrayList<>();

        for (Node<T> node : layerX) {
            sb.append(node.data);
            if (layerX.indexOf(node) == layerX.size() - 1) { // 最后一个, 用#表示换行
                sb.append(" # ");
            } else {
                sb.append(" ");
            }

            if(!node.sons.isEmpty()) {
                sons.addAll(node.sons);
            }
        }

        ILOG.debug("output: {}, {}", sb.toString(), layerX);
        return sons;
    }

    public static void main(String[] args) {
        Tree<String> familyTree = new Tree<>(false);
        Node<String> root = familyTree.init("A");

        Node<String> b1X = familyTree.addSon(root, "B1");
        Node<String> b2X = familyTree.addSon(root, "B2");
        Node<String> b3X = familyTree.addSon(root, "B3");

        Node<String> c1aX = familyTree.addSon(b1X, "C1a");
        Node<String> c1bX = familyTree.addSon(b1X, "C1b");

        Node<String> c2aX = familyTree.addSon(b2X, "C2a");
        Node<String> c2bX = familyTree.addSon(b2X, "C2b");

        Node<String> c3aX = familyTree.addSon(b3X, "C3a");
        Node<String> c3bX = familyTree.addSon(b3X, "C3b");

//        ILOG.info("search node value: {}", root.data);
//        ILOG.info("search node value: {}", b1X.parent.data);
//        ILOG.info("search node value: {}", c1aX.parent.parent.data);
//        ILOG.info("search node value: {}", c2bX.parent.parent.data);
//        ILOG.info("search node value: {}", c3bX.parent.parent.parent);

        ILOG.info("tree to string: {}", familyTree.depthFirst());
    }
}
