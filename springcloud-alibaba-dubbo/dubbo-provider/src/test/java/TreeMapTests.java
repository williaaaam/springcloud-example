import java.util.TreeMap;

/**
 * @author Williami
 * @description
 * @date 2022/3/8
 */
public class TreeMapTests {


    public static void main(String[] args) {

        TreeMap<Integer, String> treeMap = new TreeMap<>(((o1, o2) -> {
            return o1.compareTo(o2);
        }));
        treeMap.put(1, "1");
        treeMap.put(10, "23333");
        treeMap.put(1, "23333");
        treeMap.put(-1, "23333");
        treeMap.put(3, "23333");
        treeMap.put(1, "hahhaha");

        treeMap.forEach((k, v) -> System.out.println("<" + k + " , " + v + ">"));
        System.out.println("-----------------------------");

        // 返回严格大于4的key
        System.out.println("higher key = " + treeMap.higherKey(4));
        // 返回严格小于4的key
        System.out.println("lower key = " + treeMap.lowerKey(4));

        // 返回大于等于1的key,否则为null
        System.out.println(treeMap.ceilingKey(1));
        // 返回小于等于1的key,否则为null
        System.out.println(treeMap.floorKey(1));


        System.out.println(treeMap.firstKey()); // -1
        System.out.println(treeMap.lastKey()); // 10
    }
}
