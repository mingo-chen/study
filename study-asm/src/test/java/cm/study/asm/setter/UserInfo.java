package cm.study.asm.setter;

import java.util.Date;

public class UserInfo {

    private long id;

    private String nickname;

    private boolean sex;

    private int age;

    private Date birthday;

    private double weight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
               "id=" + id +
               ", nickname='" + nickname + '\'' +
               ", sex=" + sex +
               ", age=" + age +
               ", birthday=" + birthday +
               ", weight=" + weight +
               '}';
    }
}
