

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultimapTest {

    public static void main2(String[] args) {
        Multimap<String, String> m = ArrayListMultimap.create();
        m.put("a", "b1");
        m.put("a", "b2");
        m.forEach((x, y) -> System.out.println(x + "=>" + y));
        m.asMap();
        m.forEach((x, y) -> System.out.println(x + "=>" + y));
    }

    public static void main(String[] args) {
        ArrayList<Map> cp = new ArrayList<>();
        ArrayList<String> zz = new ArrayList<>();
        zz.add("eeeeeee");
        zz.add("ttttt");
        Map<String, ArrayList> qq = new HashMap<String, ArrayList>() {{
            put("superclasses", zz);
        }};
        //   qq.forEach((x, y) -> System.out.println(x + "=>" + y));
        cp.add(qq);
        cp.forEach(System.out::println);
    }

}
