package cm.study.common.model;

import java.util.Arrays;

/**
 * Created by chenming on 2017/3/26.
 */
public class Pet {

    private String name;

    private float weight;

    private int age;

    private Long[] foods;

    public Pet() {

    }

    public Pet(String name, float weight, int age) {
        this.name = name;
        this.weight = weight;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long[] getFoods() {
        return foods;
    }

    public void setFoods(Long[] foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", age=" + age +
                ", foods=" + Arrays.toString(foods) +
                '}';
    }
}
