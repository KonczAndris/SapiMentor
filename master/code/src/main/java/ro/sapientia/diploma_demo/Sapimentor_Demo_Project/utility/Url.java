package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import javax.servlet.http.HttpServletRequest;

public class Url {
    public static String getApplicationUrl(HttpServletRequest request){
        String appUrl = request.getRequestURL().toString();
        return appUrl.replace(request.getServletPath(), "");

    }
}
