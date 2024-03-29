

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ServletCreateRDF extends HttpServlet {
    public static String APP_ROOT;

    public void init() {
        APP_ROOT = this.getServletContext().getRealPath(File.separator);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String objectToReturn;
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        if (params.isEmpty()) {
            response.getWriter().write("0");
            return;
        }
        String outPutType = request.getParameter("outputtype");
        String resource = request.getParameter("resource");
        params.remove("outputtype");
        params.remove("resource");

        CreateRDF createRDF = new CreateRDF("data/family_v2.owl", resource, params);

        String result = createRDF.output(outPutType, APP_ROOT);
        out.write(result);

        out.flush();

    }
}
