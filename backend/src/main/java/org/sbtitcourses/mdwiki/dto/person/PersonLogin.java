package org.sbtitcourses.mdwiki.dto.person;


/**
 * DTO сущности Person для логина
 */
public class PersonLogin {

    /**
     * Логин или email пользователя
     */
    private String usernameOrLogin;

    /**
     * Пароль пользователя
     */
    private String password;

    public String getUsernameOrLogin() {
        return usernameOrLogin;
    }

    public void setUsernameOrLogin(String usernameOrLogin) {
        this.usernameOrLogin = usernameOrLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
