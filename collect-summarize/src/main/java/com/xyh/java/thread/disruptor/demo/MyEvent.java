package com.xyh.java.thread.disruptor.demo;

/*
 * 定义事件
 * 事件(Event)就是通过 Disruptor 进行交换的数据类型。
 */
public class MyEvent {

    private String id;
    private String name;
    private String text;
    private String age;

    @Override
    public String toString() {
        return "MyEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
