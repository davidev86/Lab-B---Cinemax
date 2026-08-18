

import BookRecommender.Application.DTO.UserDTO;

LoginCallBack {
    void onLoginSuccess(UserDTO user);
    void onLoginFailed(String errorMessage);

}
