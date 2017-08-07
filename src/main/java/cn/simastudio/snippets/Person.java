package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/6/22.
 */

import java.util.List;

public class Person {
    int id;
    List<String> names;
    String name;
    int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", names=" + names +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static Person person = new Person();

    public static Person getPerson(int id, List<String> names) {
        person.setId(id);
        person.setNames(names);
        return person;
    }
}