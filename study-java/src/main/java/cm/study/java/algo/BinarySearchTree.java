package cm.study.java.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 二叉排序树或者是一棵空树，或者是具有下列性质的二叉树：
 * （1）若左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 * （2）若右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 * （3）左、右子树也分别为二叉排序树；
 * （4）没有键值相等的节点。
 *
 * 其它扩展:
 * (1) 完全二叉树——若设二叉树的高度为h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第h层有叶子结点，并且叶子结点都是从左到右依次排布，这就是完全二叉树。
 * (2) 满二叉树——除了叶结点外每一个结点都有左右子叶且叶子结点都处在最底层的二叉树。
 * (3) 平衡二叉树——平衡二叉树又被称为AVL树（区别于AVL算法），它是一棵二叉排序树，且具有以下性质：它是一棵空树或它的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。
 *
 */
public class BinarySearchTree<T> {

    private static Logger ILOG = LoggerFactory.getLogger(BinarySearchTree.class);

    private static class Node<T> {
        private Long id;

        private T data;

        private Node<T> left;

        private Node<T> right;

        @Override
        public String toString() {
            return "Node{" +
                   "id=" + id +
                   ", data=" + data +
                   ", hash=" + hashCode() +
                   '}';
        }
    }

    private Node<T> root;

    public BinarySearchTree() {
        root = new Node<>();
        root.id = 0L;
        root.data = null;

    }

    /**
     * 插入
     * 树的最终形态跟插入的顺序相关
     * 比如有40, 50, 55, 60, 65, 70, 75这7个数字
     * 1) 如果按60, 50, 40, 55, 70, 65, 75的顺序插入, 根结点是60, 一共有3层; 7个数字完美的插入3层二叉树里
     * 2) 如果按40, 50, 55, 60, 65, 70, 75的顺序插入, 根结点是40, 一共有7层, 树已经退化成了链表
     *
     * 我们期望树的形态是1), 使用最少的层数插入最多的数据; 这样树的形态不能跟插入的顺序完全相关, 而是有自平衡机制
     * 仔细观察这种树的特点是: 每个根结点的左右子树的高度相差不过1;
     *
     * 常见的自平衡算法有: AVL, 红黑树, Treep(Tree + Heap)
     */
    public Node<T> insert(Long index, T value) {
        Node<T> node = new Node<>();
        node.id = index;
        node.data = value;

        if(root.right == null) {
            root.right = node;
            return node;

        } else {
            return insert0(root.right, node);
        }

    }

    Node<T> insert0(Node<T> startNode, Node insertNode) {
        if(startNode.id > insertNode.id) {
            // 应该在startNode的左子树
            if(startNode.left == null) {
                startNode.left = insertNode;
                return insertNode;
            } else {
                return insert0(startNode.left, insertNode);
            }

        } else if(startNode.id < insertNode.id) {
            // 应该在startNode的右子树
            if(startNode.right == null) {
                startNode.right = insertNode;
                return insertNode;
            } else {
                return insert0(startNode.right, insertNode);
            }

        } else {
            // 相同, 不插入
            return startNode;
        }
    }

    /**
     * 删除
     */
    public T delete(Long index) {
        Node<T> target = root.right;
        Node<T> prev = root;

        while(target != null && target.id != index) {
            prev = target;

            if(target.id > index) {
                target = target.left;

            } else if(target.id < index) {
                target = target.right;
            }

        }

        if(target == null) { // 未找到
            return null;
        }

        T result = target.data;
        if(target.left == null) {
            // 如果target左子树为空, 直接把右子值替换成target
            if(prev.id > target.id) {
                prev.left = target.right;

            } else {
                prev.right = target.right;
            }

        } else {
            // 查找左子树里最大的结点做为target的结果
            Node<T> leftMaxNode = target.left;
            Node<T> leftPrev = target.left;

            while(leftMaxNode.right != null) {
                leftPrev = leftMaxNode;
                leftMaxNode = leftMaxNode.right;
            }

            target.id = leftMaxNode.id;
            target.data = leftMaxNode.data;

            if(leftMaxNode == leftPrev) {
                target.left = leftMaxNode.left;
            } else {
                leftPrev.right = leftMaxNode.left;
            }
        }

        return result;
    }

    /**
     * 搜索
     */
    public Node<T> search(Long index) {
        if (root.right == null) {
            return null;
        } else {
            return search0(root.right, index);
        }
    }

    Node<T> search0(Node<T> start, Long target) {
        if (start.id == target) {
            return start;

        } else if (start.id < target) {
            if(start.right == null) {
                return null;
            } else {
                return search0(start.right, target);
            }

        } else {
            if (start.left == null) {
                return null;
            } else {
                return search0(start.left, target);
            }
        }
    }

    @Override
    public String toString() {
        if(root != null) {
            return depthFirst0(root);
        } else {
            return "[]";
        }
    }

    String depthFirst0(BinarySearchTree.Node<T> node) {
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
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        ILOG.info("tree create info: {}", tree);

//        tree.insert(100L, "cm");
//        tree.insert(101L, "cmm");
//        tree.insert(99L, "cnm");
        tree.insert(50L, "ab");
        tree.insert(60L, "abcd");
        tree.insert(55L, "abc");
        tree.insert(70L, "abcd");
        ILOG.info("tree after insert info: {}", tree);

        ILOG.info("tree search: {}", tree.search(99L));
        ILOG.info("tree search: {}", tree.search(55L));

        tree.delete(50L);
        ILOG.info("tree after delete info: {}", tree);
    }
}
