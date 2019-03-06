package cm.study.java.algo;

public class ReverseLinkedList {

    public LinkedList reverse(LinkedList head, int k) {
        LinkedList prev = null;
        LinkedList cur = head;
        LinkedList tail = head;
        LinkedList next = null;

        for (int i = 0; cur != null && i < k; i++) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        tail.next = next;

        return prev;
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

        ReverseLinkedList reverse = new ReverseLinkedList();
        LinkedList r1 = reverse.reverse(n1, 6);
        System.out.println(r1);
    }
}
