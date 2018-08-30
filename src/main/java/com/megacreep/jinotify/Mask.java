package com.megacreep.jinotify;

public class Mask {
    public static final int IN_ACCESS = 1;
    public static final int IN_MODIFY = 2;
    public static final int IN_ATTRIB = 4;
    public static final int IN_CLOSE_WRITE = 8;
    public static final int IN_CLOSE_NOWRITE = 16;
    public static final int IN_OPEN = 32;
    public static final int IN_MOVED_FROM = 64;
    public static final int IN_MOVED_TO = 128;
    public static final int IN_CREATE = 256;
    public static final int IN_DELETE = 512;
    public static final int IN_DELETE_SELF = 1024;
    public static final int IN_MOVE_SELF = 2048;
    public static final int ALL_EVENT = 4096;
}
