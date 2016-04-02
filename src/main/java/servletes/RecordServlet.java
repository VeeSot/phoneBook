package servletes;

import dao.RecordsDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RecordServlet extends HttpServlet {
    final RecordsDao dao;

    public void init(ServletConfig servletConfig) throws ServletException {
    }


    public RecordServlet(RecordsDao dao) {
        this.dao = dao;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
    }
}
