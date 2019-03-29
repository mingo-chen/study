package cm.study.store.demo.mybatis_h2;

import java.util.Date;

public class User {

    private int id;

    private long uid;

    private int registerType;

    private String username;

    private String password;

    private Date createTime;

    private Date loginTime;

    private String loginIp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getRegisterType() {
        return registerType;
    }

    public void setRegisterType(int registerType) {
        this.registerType = registerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", uid=" + uid +
               ", registerType=" + registerType +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", createTime=" + createTime +
               ", loginTime=" + loginTime +
               ", loginIp='" + loginIp + '\'' +
               '}';
    }
}
