package com.indrajeet.buspass;

public class ApiUrls {
    public static final String BASE_URL = "  https://yearly-suited-glowworm.ngrok-free.app/bus";
    public static final String LOGIN_URL = BASE_URL + "/login.php";
    public static final String SIGNUP_URL = BASE_URL + "/signup.php";
    public static final String NEW_PASS_REQUEST_URL = BASE_URL + "/new_pass.php";
    public static final String GET_USER_PASSES_URL = BASE_URL + "/all_passes.php?user_id=";
    public static final String ADMIN_GET_PASSES_URL = BASE_URL + "/admin_pass.php";
    public static final String ADMIN_GET_USERS_DETAILS_URL = BASE_URL + "/admin_user.php";
}
