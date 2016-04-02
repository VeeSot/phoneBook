package servletes;

import com.google.gson.Gson;
import dao.RecordsDao;
import dataSets.Record;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Formatter;
import java.util.List;


public class RecordServlet extends HttpServlet {
    final RecordsDao dao;

    public void init(ServletConfig servletConfig) throws ServletException {
    }


    public RecordServlet(RecordsDao dao) {
        this.dao = dao;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Gson gson = new Gson();
            String jsonRepr;
            String path = request.getPathInfo();
            if (path != null && !path.equals("/")) {
                int id = Integer.parseInt(path.substring(1));
                Record dataSet = dao.get(id);
                jsonRepr = gson.toJson(dataSet);
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
        long createRecordId = dao.save(dataSet);
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        String stringForResponse = formatter.format("{'id':%1$s}", createRecordId).toString();

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(stringForResponse);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        int id = Integer.parseInt(path.substring(1));
        dao.delete(id);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
