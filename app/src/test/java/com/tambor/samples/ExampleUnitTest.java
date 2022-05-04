package com.tambor.samples;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    /*@Mock
    private Context mockContext;

    Repository<User> repository = null;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new Repository<User>(
                mockContext,
                "databasename","databasepath",
                1,
                User.class);
    }


    @Test
    public void addition_createScript() {

        try {

            String result = repository.createScript();
            assertNotNull(result);
            assertEquals("create table tb_user" +
                    "(user_password text,user_name text," +
                    "full_name text," +
                    "id_device integer," +
                    "id integer primary key autoincrement );", result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteScriptAccept1(){
        try {

            String result = repository.deleteScript();
            assertNotNull(result);
            assertEquals("drop table if exists tb_usuario;", result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertAccept1(){
        try {
            Entity e = new User( 3, "test", "test", "test", 1);
            long result = repository.insert(e);
            assertTrue(result>0);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void testUpdateAccept1(){
        try {
            TransferObject to = repository.selectMax("TB_USUARIO", "id");
            Integer maxId = to.getInteger("max");
            Entity e = new User(  maxId ), "test", "test", "test", 1);
            long result = repository.update(e);
            assertNotNull(result);
            assertEquals(1, result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void testDeleteAccept1(){
        try {

            TransferObject to = repository.selectMax("TB_USUARIO", "id");
            Integer maxId = to.getInteger("max");
            Entity e = new User( Long.parseLong( maxId.toString() ), "test", "test", "test", 1);
            long result = repository.delete(e);
            assertNotNull(result);
            assertEquals(1, result);
        } catch (Exception e) {
            fail("not pass here " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Test
    public void testSelectAccept1(){
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

    }*/
    /*
https://www.backbase.com/2015/03/31/testing-in-android-with-mockito/

    @Test
    public void verifyOutputStreamCalls() throws Exception {
        String json = "{'json':'anyJson'}";
        when(fakeContext.openFileOutput("jsonModel", Context.MODE_PRIVATE)).thenReturn(fakeOutputStream);

        cacheManager.put("jsonModel", json);

        Mockito.verify(fakeOutputStream).write(json.getBytes());
        Mockito.verify(fakeOutputStream).close();
    }
     */
/*
@Test(expected = IOException.class)
    public void verifyOutputStreamClosesWithIOException() throws Exception {
        String json = "{'json':'anyJson'}";
        when(fakeContext.openFileOutput("jsonModel", Context.MODE_PRIVATE)).thenReturn(fakeOutputStream);
        doThrow(new IOException()).when(fakeOutputStream).write(json.getBytes());

        cacheManager.put("jsonModel", json);

        Mockito.verify(fakeOutputStream).close();
    }
*/
}