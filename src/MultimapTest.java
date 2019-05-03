import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MultimapTest {

    public static void main(String[] args) {
        Multimap<String, String> m = ArrayListMultimap.create();
        m.put("a", "b1");
        m.put("a", "b2");
        m.forEach((x, y) -> System.out.println(x + "=>" + y));
        m.asMap();
        m.forEach((x, y) -> System.out.println(x + "=>" + y));
    }
}
