package cn.simastudio.snippets.supplier;

import java.util.function.Supplier;

/**
 * Created by chenqk on 2017/5/4.
 */
public class SupplierDemo {

    public static void main(String[] args) {
        Supplier<Person> personSupplier = Person::new;
        Person person = personSupplier.get();
        System.out.println(person.toString());
    }

}
