package cn.vove7.qtmnotificationplugin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.vove7.qtmnotificationplugin.utils.SQLiteHelper;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
   @Test
   public void useAppContext() throws Exception {
      // Context of the app under test.
      Context appContext = InstrumentationRegistry.getTargetContext();


      SQLiteDatabase database = new SQLiteHelper(appContext).getReadableDatabase();
      database.execSQL("delete  from nickname where type='WECHAT'");

      assertEquals("cn.vove7.qtmnotificationplugin", appContext.getPackageName());
   }
}
