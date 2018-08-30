# Jinotify
The JNI implements of Linux sys/inotify.h

## compile so

1 cd src/main/native
2 gcc -I $JAVA_HOME/include -I $JAVA_HOME/include/linux com_megacreep_jinotify_NativeInotify.c -fPIC -shared -o libNativeInotify.so

## compile java

1 cd /path/to/*.java
2 javac *

## run

1 cd src/main/java
2 java -Djava.library.path=/path/to/libNativeInotify.so com.megacreep.jinotify.Main /path/to/watch/dir