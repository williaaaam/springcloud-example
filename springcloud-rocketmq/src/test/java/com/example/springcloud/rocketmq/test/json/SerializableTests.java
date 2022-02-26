package com.example.springcloud.rocketmq.test.json;

import java.io.*;

/**
 * @author Williami
 * @description
 * @date 2022/1/26
 */
public class SerializableTests {

    public static void main(String[] args) {

        System.out.println("-----------------------------------开始-------------------------------------");

        String filePath = "C:\\Users\\Williami\\Desktop\\serializable.txt";
        People people = new People();
        people.setAge(18);
        people.setName("Williami");

        System.out.println("-----------------------------------开始序列化-------------------------------------");

        // 序列化
        //serialize(filePath, people);

        System.out.println("------------------------------------开始反序列化------------------------------------");

        // 反序列化
        System.out.println(deSerialize(filePath));

        System.out.println("-------------------------------------结束-----------------------------------");

    }


    private static void serializeClass(String path, Class<?> clz) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            System.out.println("invoke writeObject");
            objectOutputStream.writeObject(clz);
            System.out.println("after invoke writeObject ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ObjectOutputStream只能对实现了Serializable接口的类的对象进行序列化：Java Objects -> 二进制字节流
     *
     * @param path
     * @param people
     */
    private static void serialize(String path, People people) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            System.out.println("invoke writeObject");
            objectOutputStream.writeObject(people);
            System.out.println("after invoke writeObject ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化是深拷贝,反序列化不是通过构造器构造的对象
     *
     * @param path
     * @return
     */
    private static People deSerialize(String path) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            System.out.println("invoke readObject");
            People object = (People) objectInputStream.readObject();
            System.out.println("after invoke readObject ");
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    static class People implements Serializable {

        private static final long serialVersionUID = 0L;

        /**
         * 反序列化不会调用构造器
         */
        public People() {
            System.out.println("People 默认无参构造器");
        }

        public People(String name, Integer age, double salary) {
            this.name = name;
            this.age = age;
            //this.salary = salary;
            System.out.println("People有参默认构造器");
        }

        String name;
        Integer age;

        //double salary;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        //public double getSalary() {
        //    return salary;
        //}
        //
        //public void setSalary(double salary) {
        //    this.salary = salary;
        //}

        /**
         * 不想实现Externalizable接口，又想按照自己的规则进行序列化
         * 定制：序列化重写接口
         *
         * @param outputStream
         */
        private void writeObject(ObjectOutputStream outputStream) throws IOException {
            System.out.println("invoke custom writeObject");
            // 调用默认提供方式
            //System.out.println("invoke defaultWriteObject");
            outputStream.defaultWriteObject();
            System.out.println("--- 这是自定义的writeExternal方法 ---");
            //outputStream.writeObject(this.name);
            //outputStream.writeInt(this.age);
            System.out.println("after invoke defaultWriteObject");
        }

        /**
         * 定制：反序列化重写接口
         *
         * @param objectInputStream
         */
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            System.out.println("invoke custom readObject");
            // 调用默认提供方式
            //System.out.println("invoke defaultReadObject");
            objectInputStream.defaultReadObject();
            System.out.println("--- 这是自定义的readExternal方法 ---");
            //this.name = (String) objectInputStream.readObject();
            //this.age = objectInputStream.readInt();
            System.out.println("after invoke defaultReadObject");
        }

        /**
         * 序列化之前的操作方法，可对序列化之前的对象进行处理
         *
         * @return
         */
        private Object writeReplace() {
            System.out.println("invoke writePlace");
            return this;
        }

        /**
         * 在反序列化后对返回对象的进行处理
         * <p>
         * 单例需要额外处理该方法
         *
         * @return
         */
        private Object readResolve() {
            System.out.println("invoke readResolve");
            return this;
        }

        @Override
        public String toString() {
            return "People{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

    }


}
