package com.icsd.demo.models;

import static com.icsd.demo.models.NewsEnum.valueOf;


public enum CommentEnum {
    CREATED,
    APPROVED;
    
    public static CommentEnum fromString(String param){
        try{
            int value = Integer.parseInt(param);
            return CommentEnum.values()[value];
        }catch (NumberFormatException ex){
            return valueOf(param);
        }
    }
}
