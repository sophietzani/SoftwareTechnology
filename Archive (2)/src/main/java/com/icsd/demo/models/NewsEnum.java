package com.icsd.demo.models;

import static com.icsd.demo.models.NewsEnum.valueOf;


public enum NewsEnum {
    CREATED,
    SUBMITED,
    APPROVED,
    PUBLISHED;
    
    public static NewsEnum fromString(String param){
        try{
            int value = Integer.parseInt(param);
            return NewsEnum.values()[value];
        }catch (NumberFormatException ex){
            return valueOf(param);
        }
    }
}
