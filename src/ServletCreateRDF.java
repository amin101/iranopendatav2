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

//        String user = request.getParameter("user");
        Map params = request.getParameterMap();
        System.out.println(params);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");


        // create HTML response
//        PrintWriter writer = response.getWriter();
//        writer.append("<!DOCTYPE html>\r\n")
//                .append("<html>\r\n")
//                .append("		<head>\r\n")
//                .append("			<title>Welcome message</title>\r\n")
//                .append("		</head>\r\n")
//                .append("		<body>\r\n");
//        if (user != null && !user.trim().isEmpty()) {
//            writer.append("	Welcome " + user + ".\r\n");
//            writer.append("	You successfully completed this javatutorial.net example.\r\n");
//        } else {
//            writer.append("	You did not entered a name!\r\n");
//        }
//        writer.append("		</body>\r\n")
//                .append("</html>\r\n");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String objectToReturn;
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        if (params.isEmpty()) {
            response.getWriter().write("0");
            return;
        }
        String outPutType = request.getParameter("outputtype") != null ? request.getParameter("outputtype") : "RDF/XML";
        String resource = request.getParameter("resource");
        params.remove("outputtype");
        params.remove("resource");

//       if(params.isEmpty()){
//           System.out.println("no data");
//           return;
//       }


        CreateRDF createRDF = new CreateRDF("data/family_v2.owl", resource, params);
        String result = new Gson().toJson(createRDF.saveType(outPutType, APP_ROOT));

        //  objectToReturn = "{\"file\":\""+createRDF.saveType(outPutType,APP_ROOT)+"\"}";
        out.write(result);
        // out.write( System.getProperty("catalina.base")+ File.separator+"webapps"+File.separator+"outputdata");

        out.flush();


//        System.out.println(params.get("resource")[0]);
//        for(Map.Entry<String,String[]> entry: params.entrySet()){
//            System.out.print(entry.getKey()+":");
//
//            System.out.println(entry.getValue()[0]);
//           for(String val: entry.getValue()){
//               System.out.print(val+" ");
//           }
//            System.out.println();
    }

//        // set response headers
//        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
//
//        // create HTML form
//        PrintWriter writer = response.getWriter();
//        writer.append("<!DOCTYPE html>\r\n")
//                .append("<html>\r\n")
//                .append("		<head>\r\n")
//                .append("			<title>Form input</title>\r\n")
//                .append("		</head>\r\n")
//                .append("		<body>\r\n")
//                .append("			<form action=\"hello\" method=\"POST\">\r\n")
//                .append("				Enter your name: \r\n")
//                .append("				<input type=\"text\" name=\"user\" />\r\n")
//                .append("				<input type=\"submit\" value=\"Submit\" />\r\n")
//                .append("			</form>\r\n")
//                .append("		</body>\r\n")
//                .append("</html>\r\n");
    //  }
}
