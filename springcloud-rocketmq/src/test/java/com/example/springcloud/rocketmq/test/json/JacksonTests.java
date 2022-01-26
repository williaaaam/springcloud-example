package com.example.springcloud.rocketmq.test.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Williami
 * @description
 * @date 2022/1/26
 */
public class JacksonTests {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Animal animal = new Animal();
        animal.age = 1;
        animal.name = "Moto";
        System.out.println(objectMapper.writeValueAsString(animal));
    }


    static class Animal {
        String name;
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
