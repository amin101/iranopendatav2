import com.google.gson.Gson;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServletQueryOntologyOwlApi extends HttpServlet {
    private QueryOntologyOwlApiOntology vocabulary;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String param = request.getParameter("param");
        if (param == null || param.isEmpty()) {
            response.getWriter().write("0");
            return;
        }

        try {
            vocabulary = new QueryOntologyOwlApiOntology("data/pizza.owl");
            response.getWriter().write(vocabulary.queryByClassName(param));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }
}
