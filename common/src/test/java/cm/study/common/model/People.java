package cm.study.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by chenming on 2017/3/25.
 */
public class People {

    private Pet pet;

    private Map<String, Object> ext; // 扩展

    private List<Long> bankIds; // 银行卡

    private String[] likes; // 爱好

    private long id; // 身份证ID

    private String name; // 姓名

    private int age; // 年龄

    private double height; // 身高, 单位m

    private boolean marry; // 是否结婚

    public People() {

    }

    public People(long id, String name, int age, double height, boolean marry) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.marry = marry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isMarry() {
        return marry;
    }

    public void setMarry(boolean marry) {
        this.marry = marry;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public List<Long> getBankIds() {
        return bankIds;
    }

    public void setBankIds(List<Long> bankIds) {
        this.bankIds = bankIds;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "People{" +
                "ext=" + ext +
                ", bankIds=" + bankIds +
                ", likes=" + Arrays.toString(likes) +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", marry=" + marry +
                ", pet=" + pet +
                '}';
    }
}
