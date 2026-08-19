package cinemax.gui.callback;




import cinemax.contracts.dto.UserMinInfos;


public interface LoginCallBack {
    void onLoginSuccess(UserMinInfos user);
    void onLoginFailed(String errorMessage);

}
