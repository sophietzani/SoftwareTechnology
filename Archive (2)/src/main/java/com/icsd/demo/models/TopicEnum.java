package com.icsd.demo.models;

import static com.icsd.demo.models.TopicEnum.valueOf;


public enum TopicEnum {
    CREATED,
    APPROVED;
    
    public static TopicEnum fromString(String param){
        try{
            int value = Integer.parseInt(param);
            return TopicEnum.values()[value];
        }catch (NumberFormatException ex){
            return valueOf(param);
        }
    }
}
