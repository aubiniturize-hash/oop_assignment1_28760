package com.auca.interfaces;

public interface Identifiable {
    public static final String ID_PREFIX = "AUCA";
    public static final int MIN_ID_LENGTH = 4;
    public static final int MAX_ID_LENGTH = 30;
    public static final String ID_SEPARATOR = "-";
    public static final String DEFAULT_STATUS = "ACTIVE";
    public static final int ENTITY_VERSION = 1;

    String getId();
    String getDisplayName();
    boolean isActive();
}
