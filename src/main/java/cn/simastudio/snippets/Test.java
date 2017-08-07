package cn.simastudio.snippets;

import java.util.*;

/**
 * Created by chenqk on 2017/5/10.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);

        System.out.println(ids.contains("1"));

        List<Person> people = new ArrayList<>();

        Person p1 = new Person();
        p1.setId(2);
        p1.setName("2");
        p1.setAge(2);

        Person p2 = new Person();
        p2.setId(1);
        p2.setName("1");
        p2.setAge(1);

        Person p3 = new Person();
        p3.setId(5);
        p3.setName("5");
        p3.setAge(5);

        people.add(p1);
        people.add(p2);
        people.add(p3);

        for (Person p : people) {
            System.out.println(p);
        }

        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getAge() > o2.getAge()) {
                    return 1;
                } else if (o1.getAge() == o2.getAge()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (Person p : people) {
            System.out.println(p);
        }
    }

    public void add(int a, int b) {
        int c;
    }

}
