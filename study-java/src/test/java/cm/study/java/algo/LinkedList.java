package cm.study.java.algo;

public class LinkedList {

    public int v;

    public LinkedList(int v) {
        this.v = v;
    }

    public LinkedList next;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (LinkedList head = this; head != null; head = head.next) {
            sb.append(head.v).append(" -> ");
        }

        sb.append("nil");

        return sb.toString();
    }
}
