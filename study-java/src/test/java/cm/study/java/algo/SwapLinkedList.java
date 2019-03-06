package cm.study.java.algo;

public class SwapLinkedList {

    public LinkedList swapValue(LinkedList list) {
        LinkedList cur = list;

        for (; cur != null && cur.next != null; ) {
            int t = cur.v;
            cur.v = cur.next.v;
            cur.next.v = t;

            cur = cur.next.next;
        }

        return list;
    }

    public LinkedList swapRef(LinkedList list) {
        LinkedList prev = null, head = null;

        for (LinkedList cur = list, next = list.next; cur != null && next != null; ) {
            if (prev != null) {
                prev.next = next;
            } else {
                head = next;
            }//

            cur.next = next.next;
            next.next = cur;

            prev = cur;
            cur = cur.next;
            if (cur != null) {
                next = cur.next;
            }//
        }

        return head == null? list : head;
    }

    public static void main(String[] args) {
        LinkedList n1 = new LinkedList(1);
        LinkedList n2 = new LinkedList(2);
        LinkedList n3 = new LinkedList(3);
        LinkedList n4 = new LinkedList(4);
        LinkedList n5 = new LinkedList(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;

        System.out.println(n1);

        SwapLinkedList swap = new SwapLinkedList();
//        LinkedList s1 = swap.swapValue(n1);
        LinkedList s1 = swap.swapRef(n1);
        System.out.println(s1);
    }
}
