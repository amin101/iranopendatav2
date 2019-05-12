

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ServletCreateRDFBag extends HttpServlet {
    public static String APP_ROOT;

    public void init() {
        APP_ROOT = this.getServletContext().getRealPath(File.separator);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());

        if (params.isEmpty()) {
            response.getWriter().write("0");
            return;
        }
        String outPutType = request.getParameter("outputtype");
        String param = request.getParameter("param");

//        out.write(param);
//        out.flush();

        CreateRDFBag createRDF = new CreateRDFBag(param);
        createRDF.addBagMetaData();
        createRDF.addPrimaryMetadata(createRDF.getResource());
        String result = createRDF.output(outPutType, APP_ROOT);
        out.write(result);
        out.flush();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        doPost(request, response);
    }

}
