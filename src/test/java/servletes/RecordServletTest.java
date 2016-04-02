package servletes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import common.Config;
import common.TestServer;
import dao.RecordsDao;
import dataSets.Record;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static common.Utils.getRandomName;
import static common.Utils.getRandomNumber;

/**
 * veesot on 4/2/16.
 */
public class RecordServletTest {
    RecordsDao dao;

    String API_URL = "/api/v1/records/";
    String FULL_PATH_API = "http://localhost:8080" + API_URL;


    @Before
    public void setUp() throws Exception {
        Class clazz = Record.class;
        dao = new RecordsDao();
        dao.setConfiguration(Config.getH2Configuration(clazz));
    }

    @Test
    public void testDoGet() throws Exception {
        //Создадим  заготовку записи о абоненте
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);
        long recordId = dao.save(record);
        record = dao.get(recordId);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new RecordServlet(dao)), API_URL + "*");
        TestServer testServer = new TestServer(context);
        testServer.start();


        //Request
        Client client = Client.create();
        WebResource webResource = client
                .resource(FULL_PATH_API + recordId);
        ClientResponse response = webResource
                .accept("application/json")
                .get(ClientResponse.class);


        Gson gson = new Gson();
        String expectedJson = gson.toJson(record);
        //Сравним дамп того что в базе и того что ответил сервер
        Assert.assertEquals(HttpStatus.OK_200, response.getStatus());
        Assert.assertEquals(expectedJson, response.getEntity(String.class).trim());

        testServer.stop();
    }

    @Test
    public void testDoGetInExist() throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new RecordServlet(dao)), API_URL + "*");
        TestServer testServer = new TestServer(context);
        testServer.start();


        //Request
        Client client = Client.create();
        WebResource webResource = client.resource(FULL_PATH_API + 1);
        ClientResponse response = webResource
                .accept("application/json")
                .get(ClientResponse.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());

        testServer.stop();
    }

    @Test
    public void testDoGetAll() throws Exception {
        //Create record thought ORM
        Record record = new Record();
        //First record
        record.setNumber(getRandomNumber());
        record.setPeople(getRandomName());
        dao.save(record);


        record.setNumber(getRandomNumber());
        record.setPeople(getRandomName());
        dao.save(record);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new RecordServlet(dao)), API_URL);
        TestServer testServer = new TestServer(context);
        testServer.start();


        //Request
        Client client = Client.create();
        WebResource webResource = client.resource(FULL_PATH_API);
        ClientResponse response = webResource
                .accept("application/json")
                .get(ClientResponse.class);
        String jsonRepr = response.getEntity(String.class).trim();

        //Ожидаем массив
        Gson gson = new Gson();
        ArrayList recordList = gson.fromJson(jsonRepr, ArrayList.class);
        Assert.assertEquals(recordList.size(), 2);
        testServer.stop();
    }

    @Test
    public void testDoPost() throws Exception {
        //Create record thought ORM
        Record record = new Record();
        //First record
        record.setNumber(getRandomNumber());
        record.setPeople(getRandomName());

        Gson gson = new Gson();
        String dataSetJson = gson.toJson(record);//Сразу из датасета удобнее делать,чем собирать из строчек

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new RecordServlet(dao)), API_URL);
        TestServer testServer = new TestServer(context);
        testServer.start();

        //Request
        Client client = Client.create();

        WebResource webResource = client.resource(FULL_PATH_API);
        ClientResponse response = webResource
                .accept("application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(ClientResponse.class, dataSetJson);
        String bodyOfResponse = response.getEntity(String.class).trim();


        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(bodyOfResponse);

        long recordId = Long.parseLong(((JsonObject) element).get("id").toString());//Получаем Id только что созданой записи
        Record storeDataSet = dao.get(recordId);

        Assert.assertEquals(HttpStatus.CREATED_201, response.getStatus());
        Assert.assertEquals(record.getNumber(), storeDataSet.getNumber());
        Assert.assertEquals(record.getPeople(), storeDataSet.getPeople());
        testServer.stop();
    }

    @Test
    public void testDoDelete() throws Exception {

        Record record = new Record();
        //First record
        record.setNumber(getRandomNumber());
        record.setPeople(getRandomName());
        long recordId = dao.save(record);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new RecordServlet(dao)), API_URL + "*");
        TestServer testServer = new TestServer(context);
        testServer.start();

        //Request
        Client client = Client.create();
        WebResource webResource = client.resource(FULL_PATH_API + recordId);
        ClientResponse response = webResource
                .accept("application/json")
                .delete(ClientResponse.class);


        Assert.assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());

        try {
            dao.get(recordId);//Wait Exception
        } catch (NullPointerException e) {
            testServer.stop();
        }


    }
}