package com.imperium.Database;

import android.provider.BaseColumns;

public class DbContract {

    private DbContract(){} // Prevents instantiation

    public static abstract class ActiveSystems implements BaseColumns {
        public static final String TABLE_NAME = "activesystems";
        public static final String COLUMN_NAME_SYSTEM_NAME = "systemname";
        public static final String COLUMN_NAME_IP_ADDRESS = "ipaddress";
        public static final String COLUMN_NAME_PORT = "port";
        public static final String COLUMN_STATUS = "status";
    }
}
