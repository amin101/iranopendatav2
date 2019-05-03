import com.google.gson.Gson;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ServletQueryOntology extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Map<String, Map<String, ArrayList<String>>> ontClassesWithProperties = new HashMap<>();


        String param = request.getParameter("param");
        if (param == null || param.isEmpty()) {
            response.getWriter().write("0");
            return;
        }
        QueryOntology vocabulary = new QueryOntology("data/pizza.owl");
        ArrayList ontClasses = vocabulary.queryByClassName(param);
        Iterator ontClassesIterator = ontClasses.iterator();

        while (ontClassesIterator.hasNext()) {

            Object ontClass = ontClassesIterator.next();

            Map<String, ArrayList<String>> properties = vocabulary.getClassProperties(ontClass.toString());
            ontClassesWithProperties.put(ontClass.toString(), properties);
        }


        String jsonArray = new Gson().toJson(ontClassesWithProperties);

        response.getWriter().write(jsonArray);
    }
}
