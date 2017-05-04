package cn.simastudio.snippets.enums;

/**
 * Created by chenqk on 2017/5/4.
 */
public enum Gender {

    FEMALE(0, "女"),
    MALE(1, "男");

    private int id;
    private String name;

    Gender(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Gender{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
