package com.lookren.rssreader.data;

public interface Columns {

    public static final String _ID = "_id";

    public static final String[] CONTENT_PROJECTION_ID = new String[]{_ID};
    public static final String WHERE_ID_CLAUSE = _ID + " = ?";
    public static final String ORDER_BY_ID = _ID;
    public static final String ORDER_BY_ID_LIMIT_1 = _ID + " LIMIT 1";
    public static final String SECTION_COLUMN_NAME = "SECTION";

    public static final int COLUMN_ID_NUMBER = 0;

    public interface SubscriptionColumns extends Columns {

        public static final String DISPLAY_NAME = "display_name";
        public static final String URL = "url";
        public static final String TOKEN = "token";
        public static final String UNTIL_TIME = "until_time";
        public static final String PHONE = "phone";

        public static final String[] CONTENT_PROJECTION = new String[] {
                _ID,
                DISPLAY_NAME,
                URL
        };

        public static final int COLUMN_HEADER_NUMBER = COLUMN_ID_NUMBER + 1;
        public static final int COLUMN_URL_NUMBER = COLUMN_HEADER_NUMBER + 1;

        String TABLE_NAME = "Subscription";
    }

    public interface PostColumns extends Columns {

        public static final String DISPLAY_NAME = "display_name";
        public static final String SUBSCRIPTION = "subscription";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";


        public static final String[] CONTENT_PROJECTION = new String[] {
                _ID,
                DISPLAY_NAME,
                SUBSCRIPTION,
                DESCRIPTION,
                LINK
        };

        public static final int COLUMN_HEADER_NUMBER = COLUMN_ID_NUMBER + 1;
        public static final int COLUMN_SUBSCRIPTION_NUMBER = COLUMN_HEADER_NUMBER + 1;
        public static final int COLUMN_DESCRIPTION_NUMBER = COLUMN_SUBSCRIPTION_NUMBER + 1;
        public static final int COLUMN_LINK_NUMBER = COLUMN_DESCRIPTION_NUMBER + 1;

        String TABLE_NAME = "Post";

        public static final String WHERE_SUBSCRIPTION_ID_CLAUSE = SUBSCRIPTION + " = ?";
    }
}
