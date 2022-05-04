package com.tambor.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.tambor.orm.database.Repository;
import com.tambor.orm.database.dao.entity.Entity;
import com.tambor.orm.database.dao.model.FieldTO;
import com.tambor.orm.database.dao.model.TransferObject;
import com.tambor.orm.database.dao.statement.operation.WhereStatement;
import com.tambor.orm.utils.Log;
import com.tambor.samples.database.Config;
import com.tambor.samples.database.models.User;


import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {
    Repository<User> repository = null;
    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        try {

        repository = new Repository<>(
                appContext,
                Config.DATABASE_NAME,
                Config.getDBPath(appContext),
                Config.DATABASE_VERSION,
                User.class);
        } catch (Exception e) {
            Log.e(appContext,e.getLocalizedMessage(),e);
        }

    }

    @Test
    public void step1_useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.tambor.samples", appContext.getPackageName());
    }

    @Test
    public void step2_createScript() {

        try {

            String result = repository.createScript();
            assertNotNull(result);
            String createStatement = "create table if not exists tb_user(" +
                    "full_name text," +
                    "id integer primary key autoincrement ," +
                    "user_name text," +
                    "user_password text," +
                    "status text);";
            assertEquals(createStatement, result);
            /*assertEquals("create table tb_user" +
                    "(user_password text,user_name text," +
                    "full_name text," +
                    "id_device integer," +
                    "id integer primary key autoincrement );", result);*/
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void step3_insertAccept1(){
        try {
            Entity e = new User( 3, "test", "test", "test");
            long result = repository.insert(e);
            assertTrue(result>0);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }





    @Test
    public void step4_updateAccept1(){

        try {
            TransferObject to = repository.selectMax("id");
            int maxId = to.getInteger("max");
            Entity e = new User(  maxId, "test", "test", "test");
            long result = repository.update(e);
            assertNotNull(result);
            assertEquals(1, result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }


    @Test
    public void step5_selectAccept1(){
        try {
            WhereStatement w = new  WhereStatement(
                    new FieldTO("full_name","test")
            );
            List<TransferObject> list = repository.select( w );
            assertNotNull(list);
            assertTrue(list.size()>0);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void step6_deleteAccept1(){
        try {

            TransferObject to = repository.selectMax( "id");
            Integer maxId = to.getInteger("max");
            Entity e = new User( maxId , "test", "test", "test");
            long result = repository.delete(e);
            assertNotNull(result);
            assertEquals(1, result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void step7_deleteScriptAccept1(){
        try {

            String result = repository.deleteScript();
            assertNull(result);
            //assertEquals("drop table if exists tb_usuario;", result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}