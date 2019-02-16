package com.shihao.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    private static final Pattern mobile_pattren = Pattern.compile("1\\d{10}");


    public static Boolean isMobile(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }else{
            Matcher matcher = mobile_pattren.matcher(str);
            return matcher.matches();
        }
    }
}
