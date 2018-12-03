package com.megacreep.jinotify;

import java.util.List;

public class Inotify {

    static {
        // 加载so
        LibLoader.load("", "NativeInotify");
    }

    /**
     * inotify 文件描述符
     */
    private int fd;
    /**
     * 监视描述符
     */
    private int wd;

    private NativeInotify nativeObj;

    private int status = -1;

    public Inotify() {
        nativeObj = new NativeInotify();
        fd = nativeObj.init();
        status = 0;
    }

    /**
     * 监视path目录
     *
     * @param path 要监视的目录
     * @param mask 事件掩码，{@link Mask}
     * @return 监视fd
     */
    public synchronized int addWatch(String path, int mask) {
        if (status != 0) {
            throw new IllegalStateException("Inotify not ready");
        }
        wd = nativeObj.addWatch(fd, path, mask);
        status = 1;
        return wd;
    }

    /**
     * 取消监视
     */
    public synchronized int removeWatch() {
        if (status != 1) {
            throw new IllegalStateException("Can not remove watch before addWatch");
        }
        int code = nativeObj.removeWatch(fd, wd);
        status = 0;
        return code;
    }

    /**
     * 这是一个阻塞方法，当事件发生后返回，可通过循环或递归调用来完成对一个目录的监视
     *
     * @return 事件列表
     */
    public synchronized List<InotifyEvent> takeEvent() {
        if (status != 1) {
            throw new IllegalStateException("Can not take watch before addWatch");
        }
        return nativeObj.takeEvent(fd, wd);
    }
}
