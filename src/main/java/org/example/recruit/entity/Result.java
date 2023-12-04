package org.example.recruit.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/11
 **/
@Data
public class Result {
    private String request_id;
    private String status;
    private String reason;

    public static Result fromJsonString(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
