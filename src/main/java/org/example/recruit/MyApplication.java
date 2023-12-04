package org.example.recruit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: ${USER}
 * @date: ${DATE}
 **/
@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
@MapperScan("org.example.recruit.mapper")
public class MyApplication {
    //Springboot的启动
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
