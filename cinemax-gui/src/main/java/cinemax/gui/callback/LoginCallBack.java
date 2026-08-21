package cinemax.gui.callback;




import cinemax.contracts.dto.UserMinInfo;


public interface LoginCallBack {
    void onLoginSuccess(UserMinInfo user);
    void onLoginFailed(String errorMessage);

}
