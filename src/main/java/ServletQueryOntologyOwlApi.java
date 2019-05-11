
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            vocabulary = new QueryOntologyOwlApiOntology("data/domain1.owl");
            response.getWriter().write(vocabulary.queryByClassName(param));
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
