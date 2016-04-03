package servletes;

import com.google.gson.Gson;
import dao.RecordsDao;
import dataSets.Record;
import dbService.Config;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static common.ProdUtils.splitQuery;


public class RecordServlet extends HttpServlet {
    final RecordsDao dao;

    @SuppressWarnings("unused")
    public RecordServlet() {
        this.dao = new RecordsDao();
        Class clazz = Record.class;
        dao.setConfiguration(Config.getPgConfiguration(clazz));
    }


    public RecordServlet(RecordsDao dao) {
        this.dao = dao;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Gson gson = new Gson();
            String jsonRepr;
            String path = request.getPathInfo();
            String queryString = request.getQueryString();
            if (path != null && !path.equals("/") && queryString == null) {
                int id = Integer.parseInt(path.substring(1));
                Record dataSet = dao.get(id);
                jsonRepr = gson.toJson(dataSet);
            } else if (queryString != null) {
                //Попробуем найти строку поиска по имени абонента
                Map<String, String> map = splitQuery(queryString);
                String people = map.get("people");
                if (people != null) {//Что то прислали
                    List records = dao.findByName(people);
                    jsonRepr = gson.toJson(records);
                } else {//Что то иное запросили.Ответим полным списком.
                    List records = dao.getAll();
                    jsonRepr = gson.toJson(records);
                }

            } else {
                List records = dao.getAll();
                jsonRepr = gson.toJson(records);
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(jsonRepr);
            response.setStatus(HttpServletResponse.SC_OK);


        } catch (NullPointerException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);


        Gson gson = new Gson();
        Record dataSet = gson.fromJson(jb.toString(), (Type) Record.class);
        long createRecordId;
        int status;
        String message;
        createRecordId = dao.save(dataSet);
        if (createRecordId > 0) {
            status = HttpServletResponse.SC_CREATED;
            message = "Contact added";
        } else {//Упс...кто то хочет дубль создать
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = "Contact exist!";
        }

        String stringForResponse = String.format("{'id':%1$s,'status':%2$s,'message':'%3$s'}", createRecordId, status, message).replaceAll("'", "\"");

        response.setStatus(status);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(stringForResponse.trim());

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        int id = Integer.parseInt(path.substring(1));
        dao.delete(id);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
