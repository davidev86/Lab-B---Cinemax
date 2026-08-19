// Classe per contenere lo stato del login


//qui bisogna inserire il tipo
package cinemax.gui.login;

public class LoginState {
    private boolean isLoggedIn = false;
    private String username;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login(String username) {
        this.isLoggedIn = true;
        this.username = username;
    }

    public boolean logout() {
        this.isLoggedIn = false;
        this.username = null;
        return false;
    }

    public String getUsername() {
        return username;
    }
}
