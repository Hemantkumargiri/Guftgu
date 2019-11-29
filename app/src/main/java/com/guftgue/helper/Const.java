package com.guftgue.helper;

public class Const
{
    public static final int NOT_FOUND_NON_STRING = -1;
    public static final String NOT_FOUND_STRING= "not found";

    public static class Tag
    {
        public static final String PROFILE_VIEW = "ProfileView";
        public static final String TEXT_IMAGE_DASHBOARD = "TextImageDashboard";
        public static final String USER_LIST = "UserList";
        public static final String MY_ENCRYPTION = "MyEncryption";
        public static final String HELP = "Help";
        public static final String CONTACT_US = "ContactUs";
        public static final String CHAT = "chat";
        public static final String FAQ = "faq";

    }

    public static class Type
    {
        public static final int APP_LOGIN = 1;
        public static final int FACEBOOK_LOGIN = 2;
        public static final int TWITTER_LOGIN = 3;

        public static final String DEVICE_TYPE_ANDROID = "1";  // device type(Android), it's fixed in server
    }

    public static class Key
    {
        public static String EMAIL = "email";
    }
}
