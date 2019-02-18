package cm.study.java.algo;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 堆, 使用数组来存储树结构的数据类型
 *
 * parent(i) = floor((i-1)/2)
 * left(i) = 2i + 1
 * right(i) = 2i + 2
 *
 * 最大堆: 所有父结点都比子结点id大
 * 最小堆: 所有父结点都比子结点id小
 */
public class Heap<T> {

    private static Logger ILOG = LoggerFactory.getLogger(Heap.class);

    private static class Node<T> {
        private int id;

        private T data;

        public Node(int id, T data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Node{" +
                   "id=" + id +
                   ", data=" + data +
                   '}';
        }
    }

    private Node<T>[] table;

    private int length;

    public Heap(int size) {
        table = new Node[size];
    }

    public void put(Node<T> add) {
        int idx = length++;
        table[idx] = add;
        shiftUp(idx);
    }

    public T remove(int id) {
        int index = search(0, id);
        if (index == -1) {
            return null;

        } else {
            T ret = table[index].data;

            length--;
            table[index].id = table[length].id;
            table[index].data = table[length].data;
            table[length] = null;

            shiftDown(index);

            return ret;
        }
    }

    /**
     * 如果一个节点比它的父节点大（最大堆）或者小（最小堆），那么需要将它同父节点交换位置。这样是这个节点在数组的位置上升
     */
    Node<T> shiftUp(int index) {
        int parentIdx = (index - 1) / 2;
        if (parentIdx < 0) {
            return null;
        }

        Node<T> parent = table[parentIdx];
        Node<T> self = table[index];
        // 如果i节点id大于i节点父id, 则进行交换; 直到i=0或node(i) < node(i).parent
        if(parent.id < self.id) {
            // swap
            int tempId = parent.id;
            T tempData = parent.data;
            parent.id = self.id;
            parent.data = self.data;

            self.id = tempId;
            self.data = tempData;

            return shiftUp(parentIdx);
        } else {
            return null;
        }
    }

    /**
     * 如果一个节点比它的子节点小（最大堆）或者大（最小堆），那么需要将它向下移动。这个操作也称作“堆化（heapify）”
     */
    Node<T> shiftDown(int index) {
        // 找到index子结点里最大的结点跟index进行交换
        Node<T> ex = table[index];

        int lIdx = 2 * index + 1;
        int rIdx = 2 * index + 2;
        Node<T> lChild = table[lIdx] == null ? null : table[lIdx];
        Node<T> rChild = table[rIdx] == null ? null : table[rIdx];

        if ((lChild == null || ex.id > lChild.id)
            && (rChild == null || ex.id > rChild.id)) { // 比2个子结点都大, 结束
            return ex;

        } else {
            int tempId = ex.id;
            T tempData = ex.data;

            if (lChild.id < rChild.id) {
                ex.id = rChild.id;
                ex.data = rChild.data;

                rChild.id = tempId;
                rChild.data = tempData;

                return shiftDown(rIdx);
            } else {
                ex.id = lChild.id;
                ex.data = lChild.data;

                lChild.id = tempId;
                lChild.data = tempData;

                return shiftDown(lIdx);
            }

        }

    }

    public T get(int id) {
        int index = search(0, id);
        if (index == -1) {
            return null;
        } else {
            return table[index].data;
        }
    }

    int search(int level, int id) {
        int start = power(2, level) - 1;
        int stop = power(2, level + 1) - 1;

        boolean allGreat = true;
        boolean isEnd = false;

        for (int index = start; index < stop; index++) {
            Node<T> node = table[index];
            if (node == null) {
                isEnd = true;
            }

            if (node.id == id) {
                return index;
            } else if(node.id > id) {
                allGreat = false;
            }
        }

        if (isEnd || allGreat) {
            return -1;
        } else {
            return search(level + 1, id); // 找下一层
        }
    }

    static int power(int x, int y) {
        if(y == 0) {
            return 1;
        } else {
            int ret = x;
            for (int c = 0; c < y; c++) {
                ret = ret * x;
            }
            return ret;
        }
    }

    @Override
    public String toString() {
        return "Heap{" +
               "table=" + Arrays.toString(table) +
               '}';
    }

    public static void main(String[] args) {
        Heap<String> heap = new Heap<>(64);
        heap.put(new Node<>(1, "ab"));
        heap.put(new Node<>(2, "ac"));
        heap.put(new Node<>(5, "ad"));
        heap.put(new Node<>(7, "ae"));
        heap.put(new Node<>(10, "af"));

        ILOG.info("after insert heap: {}", heap);

        ILOG.info("heap search: {}", heap.get(2));

        heap.remove(10);
        ILOG.info("after remove heap: {}", heap);
    }
}
