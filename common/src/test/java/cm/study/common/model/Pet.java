package cm.study.common.model;

/**
 * Created by chenming on 2017/3/26.
 */
public class Pet {

    private String name;

    private float weight;

    private int age;

    public Pet() {

    }

    public Pet(String name, float weight, int age) {
        this.name = name;
        this.weight = weight;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", age=" + age +
                '}';
    }
}
