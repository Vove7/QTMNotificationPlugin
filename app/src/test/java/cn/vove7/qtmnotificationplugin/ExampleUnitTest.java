package cn.vove7.qtmnotificationplugin;


import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.vove7.qtmnotificationplugin.util.Utils.inTimeQuantum;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
   @Test
   public void addition_isCorrect() throws Exception {
      assertEquals(4, 2 + 2);

      String title="QQ空间动态(共2条未读)";
      Matcher matcher = Pattern.compile("QQ空间动态(\\(共(\\d+)条未读\\))?$").matcher(title);
      if (matcher.find()) {
         //Log.d(TAG, "parseQQNotificationType: QQ空间");
         System.out.print("yes");
      }
   }

   @Test
   public void test() {
      int a = 1;
      v(a);
      float f = 1;
      v(f);
      boolean b = false;
      v(b);
      String s = "11";
      v(s);
      Set<String> strings = new HashSet<>();
      v(strings);

   }

   private void v(Object val) {

      if (val instanceof Boolean) {
         System.out.println("Boolean");
      } else if (val instanceof Integer) {
         System.out.println("Integer");
      } else if (val instanceof Long) {
         System.out.println("Long");

      } else if (val instanceof Float) {
         System.out.println("Float");
      } else if (val instanceof String) {
         System.out.println("String");
      } else if (val instanceof Set) {
         System.out.println("Set");
      }
   }

   @Test
   public void t() {
      int i=3;
      System.out.print(String.format("i=%03d",i));
   }

   @Test
   public void testTimeQuantum() {
      System.out.println(inTimeQuantum("0:10", "5:23", "2:3"));
      System.out.println(inTimeQuantum("0:10", "5:23", null));
      System.out.println(inTimeQuantum("23:00", "5:23", null));
      System.out.println(inTimeQuantum("23:00", "5:23", "22:02"));
      System.out.println(inTimeQuantum("23:00", "22:23", "22:02"));
      System.out.println(inTimeQuantum("23:00", "22:23", "22:44"));
      System.out.println(inTimeQuantum("2:01", "3:00", "2:0"));
   }
}