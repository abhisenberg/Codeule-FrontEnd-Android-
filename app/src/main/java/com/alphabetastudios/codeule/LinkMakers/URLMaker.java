package com.alphabetastudios.codeule.LinkMakers;

/**
 * Created by abheisenberg on 20/8/17.
 */

public class URLMaker {

    public static String HE_getFullLink(String url){
        if(url.startsWith("https")){
            return url;
        } else {
            return "https://www.hackerearth.com"+url;
        }
    }

    public static String CC_getFullLink(String contest_code){
        return "https://www.codechef.com/"+ contest_code;
    }

    public static String HR_getFullLink(String url){
        return "https://www.hackerrank.com"+url;
    }

}
