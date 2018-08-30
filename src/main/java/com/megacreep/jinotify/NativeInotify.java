package com.megacreep.jinotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class NativeInotify {

    /**
     * 构造方法会把jar中的so文件提取出来，并在保存在当前目录下加载
     * 此方法中还注册了一个ShutdownHook，在进程结束时，删除这个so文件
     * 注意：当使用 kill -9 时，ShutdownHook不会执行，也就不会删除这个so文件，当再次启动调用到该方法时，会先检查是否已经有了这个文件
     *
     * @throws Exception
     */
    public NativeInotify() throws Exception {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("libNativeInotify.so")) {
            File tmp = new File("libNativeInotify.so");
            // 文件不存在并且创建失败
            if (!tmp.exists() && !tmp.createNewFile()) {
                throw new SecurityException("Failed to overwrite file:" + tmp.getAbsolutePath());
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
            Runtime.getRuntime().addShutdownHook(new Thread(tmp::delete));
        }
    }

    /**
     * 初始化并获得一个 inotify 实例
     *
     * @return inotify的fd
     */
    native int init();

    /**
     * 初始化并获得一个 inotify 实例
     * 与 init() 的区别是，可选为非阻塞方式，具体参考inotify的init1
     *
     * @return inotify的fd
     */
    native int init1(int flag);

    /**
     * 监视path目录
     *
     * @param fd   inotify实例的fd
     * @param path 要监视的目录
     * @param mask 事件掩码，{@link Mask}
     * @return 监视fd
     */
    native int addWatch(int fd, String path, int mask);

    /**
     * 取消监视
     *
     * @param fd inotify实例的fd
     * @param wd 监视fd
     * @return 取消结果
     */
    native int removeWatch(int fd, int wd);

    /**
     * 这是一个阻塞方法，当事件发生后返回，可通过循环或递归调用来完成对一个目录的监视
     *
     * @param fd inotify实例的fd
     * @param wd 监视fd
     * @return 事件列表
     */
    native List<InotifyEvent> takeEvent(int fd, int wd);
}
