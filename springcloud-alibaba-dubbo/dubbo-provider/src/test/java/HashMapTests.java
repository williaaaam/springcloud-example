/**
 * @author Williami
 * @description
 * @date 2022/3/4
 */
public class HashMapTests {


    public static void main(String[] args) {
        System.out.println(fic(5));
    }

    public static int fic(int n){
        if(n == 1 || n == 2){
            return 1;
        }
        return fic(n - 1) + fic(n - 2);
    }

    static class Node {
        String value;
        Node next;

        public Node(String value, Node next) {
            this.value = value;
            this.next = next;
        }

        public Node() {
        }

        public Node(String value) {
            this.value = value;
        }
    }

}
