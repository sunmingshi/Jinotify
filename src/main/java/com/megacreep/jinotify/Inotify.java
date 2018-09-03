package com.megacreep.jinotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Inotify {

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

    /**
     * 构造方法会把jar中的so文件提取出来，并在保存在当前目录下加载
     * 此方法中还注册了一个ShutdownHook，在进程结束时，删除这个so文件
     * 注意：当使用 kill -9 时，ShutdownHook不会执行，也就不会删除这个so文件，当再次启动调用到该方法时，会先检查是否已经有了这个文件
     *
     * @throws Exception
     */
    public Inotify() throws Exception {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("libNativeInotify.so")) {
            File jnilibdir = new File(".jnilib");
            if (!jnilibdir.exists() && !jnilibdir.mkdir()) {
                throw new SecurityException("Failed to mkdir:" + jnilibdir.getAbsolutePath());
            }
            File tmp = new File(jnilibdir.getAbsolutePath() + "/libNativeInotify.so");
            // 文件不存在并且创建失败
            if (!tmp.exists() && !tmp.createNewFile()) {
                throw new SecurityException("Failed to create file:" + tmp.getAbsolutePath());
            }
            // 写入当前目录下
            try (OutputStream out = new FileOutputStream(tmp)) {
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
            }
            // 加载so
            System.load(tmp.getAbsolutePath());
            // 注册删除hook
            Thread clear = new Thread(() -> {
                tmp.delete();
                jnilibdir.delete();
            });
            Runtime.getRuntime().addShutdownHook(clear);
            nativeObj = new NativeInotify();
            fd = nativeObj.init();
            status = 0;
        }
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
