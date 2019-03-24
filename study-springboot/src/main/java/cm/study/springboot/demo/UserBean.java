package cm.study.springboot.demo;

public class UserBean {

    private int id;
    private String name;

    public UserBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
