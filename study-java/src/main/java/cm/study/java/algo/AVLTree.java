package cm.study.java.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AVL树
 * 在AVL树中任何节点的两个子树的高度最大差别为1，所以它也被称为高度平衡树
 * 增加和删除可能需要通过一次或多次树旋转来重新平衡这个树
 */
public class AVLTree<T> {
    private static Logger ILOG = LoggerFactory.getLogger(AVLTree.class);

    private static class Node<T> {
        private Long id;

        private T data;

        private Node<T> parent;

        private Node<T> left;

        private Node<T> right;

        @Override
        public String toString() {
            return "Node{" + id + "(" + data + ")}";
        }
    }

    private Node<T> root;

    public AVLTree() {
        root = new Node<>();
        root.id = 0L;
        root.data = null;
    }

    public Node<T> insert(Long id, T value) {
        Node<T> node = new Node<>();
        node.id = id;
        node.data = value;

        if(root.right == null) {
            root.right = node;
            node.parent = root;
            return node;

        } else {
            Node<T> afterInserted = insert0(root.right, node);
            if (null == afterInserted) {
                return null;
            }

            // 从插入的节点往上回朔, 直到发现不平衡或到root节点
            Node<T> rNode = afterInserted.parent.parent;
            Node<T> pNode = afterInserted.parent;

            while (true) {
                if(rNode == null || rNode == root) {
                    break;
                }

                int leftHeight = treeHeight(rNode.left);
                int rightHeight = treeHeight(rNode.right);

                if(leftHeight - rightHeight > 1) { // 不平衡
                    if(treeHeight(pNode.left) - treeHeight(pNode.right) > 0) { // LL型
                        leftLeftRotation(pNode, rNode);

                    } else { // LR型
                        leftRightRotation(pNode, rNode);
                    }

                } else if(leftHeight - rightHeight < -1) { // 不平衡
                    if(treeHeight(pNode.left) - treeHeight(pNode.right) > 0) { // RL
                        rightLeftRotation(pNode, rNode);

                    } else { // RR型
                        rightRightRotation(pNode, rNode);
                    }

                } else {
                    // 平衡
                    ILOG.info("tree is balance, root node: {}, left depth: {}, right depth: {}", rNode, leftHeight, rightHeight);
                }

                pNode = rNode;
                rNode = rNode.parent;
            }

            return afterInserted;
        }
    }

    Node<T> insert0(Node<T> startNode, Node insertNode) {
        if(startNode.id < insertNode.id) {
            // 往右子树插入
            if(startNode.right == null) {
                startNode.right = insertNode;
                insertNode.parent = startNode;
                return insertNode;

            } else {
                return insert0(startNode.right, insertNode);
            }

        } else if(startNode.id > insertNode.id) {
            // 往左子树插入
            if (startNode.left == null) {
                startNode.left = insertNode;
                insertNode.parent = startNode;
                return insertNode;

            } else {
                return insert0(startNode.left, insertNode);
            }

        } else { // id相同不能插入
            return null;
        }

    }

    /**
     * 获得树的高度
     */
    int treeHeight(Node<T> startNode) {
        int height = 0;
        if(startNode != null) {
            height = 1;
        } else {
            return height;
        }

        int leftHeigh = 0;
        if(startNode.left != null) {
            leftHeigh = treeHeight(startNode.left);
        }

        int rightHeigh = 0;
        if (startNode.right != null) {
            rightHeigh = treeHeight(startNode.right);
        }

        return height + Math.max(leftHeigh, rightHeigh);
    }

    /**
     * 左左类型
     * @param pNode: 新插入的节点的前一节点
     * @param rNode: 新插入的节点导致失衡的最近根节点
     *
     * pNode改为根节点, iNode, rNode分别为pNode的左右节点
     * 参见: https://www.cnblogs.com/cherryljr/p/6669489.html
     */
    Node<T> leftLeftRotation(Node<T> pNode, Node<T> rNode) {
        // 旋转
        rNode.left = pNode.right;
        if (pNode.right != null) {
            pNode.right.parent = rNode;
        }

        Node rpNode = rNode.parent;
        pNode.parent = rpNode;
        if (rpNode == root) {
            rpNode.right = pNode;
        } else if(rpNode.id < pNode.id) {
            rpNode.right = pNode;

        } else {
            rpNode.left = pNode;
        }

        rNode.parent = pNode;
        pNode.right = rNode;

        ILOG.info("ll rotation complete, {}", rNode);
        return pNode;
    }

    /**
     * 左右类型
     * 参见: https://www.cnblogs.com/cherryljr/p/6669489.html
     * @param pNode: 新插入的节点的前一节点
     * @param rNode: 新插入的节点导致失衡的最近根节点
     * 参见: https://www.cnblogs.com/cherryljr/p/6669489.html
     */
    Node<T> leftRightRotation(Node<T> pNode, Node<T> rNode) {
        // 再以pNode.left为轴提起来, 把轴的右边向下旋转
        Node<T> newNode = rightRightRotation(pNode.right, pNode);
        ILOG.info("lr rotation after rr, {}", this);

        // 先把pNode提起来, 也就是把pNode边向下旋转
        leftLeftRotation(newNode, rNode);
        ILOG.info("lr rotation after ll, {}", this);
        return newNode;
    }

    /**
     * 右左类型
     * 参见: https://www.cnblogs.com/cherryljr/p/6669489.html
     * @param pNode: 新插入的节点的前一节点
     * @param rNode: 新插入的节点导致失衡的最近根节点
     */
    Node<T> rightLeftRotation(Node<T> pNode, Node<T> rNode) {
        // 再以pNode.left为轴提起来, 把轴的右边向下旋转
        Node<T> newNode = leftLeftRotation(pNode.left, pNode);
        ILOG.info("rl rotation after ll, {}", this);

        // 先把pNode提起来, 也就是把pNode左边向下旋转
        rightRightRotation(newNode, rNode);
        ILOG.info("rl rotation after rr, {}", this);
        return newNode;
    }

    /**
     * 右右类型
     * 参见: https://www.cnblogs.com/cherryljr/p/6669489.html
     * @param pNode: 新插入的节点导致失衡的节点
     * @param rNode: 新插入的节点导致失衡的最近根节点
     */
    Node<T> rightRightRotation(Node<T> pNode, Node<T> rNode) {
        // 旋转
        rNode.right = pNode.left;
        if (pNode.left != null) {
            pNode.left.parent = rNode;
        }

        Node rpNode = rNode.parent;
        pNode.parent = rpNode;
        if (rpNode == root) {
            rpNode.right = pNode;
        } else if(rpNode.id < pNode.id) {
            rpNode.right = pNode;

        } else {
            rpNode.left = pNode;
        }

        rNode.parent = pNode;
        pNode.left = rNode;

        ILOG.info("rr rotation complete, {}", rNode);
        return pNode;
    }

    public T delete(Long id) {
        return null;
    }

    public T search(Long id) {
        return null;
    }

    @Override
    public String toString() {
        if(root != null) {
            return depthFirst0(root.right);
        } else {
            return "[]";
        }
    }

    String depthFirst0(Node<T> node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node);
        sb.append(" # <");

        // 先左, 再右
        if (node.left != null) {
            sb.append(depthFirst0(node.left));
        } else {
            sb.append("nil");
        }

        sb.append(", ");

        if (node.right != null) {
            sb.append(depthFirst0(node.right));
        } else {
            sb.append("nil");
        }
        sb.append(">");

        return sb.toString();
    }

    public static void main(String[] args) {
        AVLTree<String> tree = new AVLTree<>();

        tree.insert(10L, "abc");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(20L, "def");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(30L, "ghi");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(40L, "jkl");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(50L, "mno");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(35L, "pqr");
        ILOG.info("after insert tree: {}", tree);

        tree.insert(5L, "stu");
        ILOG.info("after insert tree: {}", tree);
    }
}
