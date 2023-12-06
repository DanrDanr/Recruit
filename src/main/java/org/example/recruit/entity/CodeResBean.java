package org.example.recruit.entity;

import lombok.Data;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/6
 **/
@Data
public class CodeResBean<T> {
    public T v;
    public String msg;
}
