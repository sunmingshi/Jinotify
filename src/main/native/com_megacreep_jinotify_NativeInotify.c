#include <errno.h>
#include <poll.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/inotify.h>
#include <unistd.h>

#include "com_megacreep_jinotify_NativeInotify.h"

/*
 * Class:     com_megacreep_jinotify_NativeInotify
 * Method:    init
 * Signature: ()I
 */
jint Java_com_megacreep_jinotify_NativeInotify_init
    (JNIEnv *env, jobject jobj)
{
  int ret = inotify_init();
  return (jint)ret;
}

/*
 * Class:     com_megacreep_jinotify_NativeInotify
 * Method:    init1
 * Signature: (I)I
 */
jint Java_com_megacreep_jinotify_NativeInotify_init1
    (JNIEnv *env, jobject jobj, jint flag)
{
  int ret = inotify_init1((int)flag);
  return (jint)ret;
}

/*
 * Class:     com_megacreep_jinotify_NativeInotify
 * Method:    addWatch
 * Signature: (ILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_megacreep_jinotify_NativeInotify_addWatch
    (JNIEnv *env, jobject jobj, jint jfd, jstring jpath, jint jmask)
{
    const char *path = (*env)->GetStringUTFChars(env, jpath, JNI_FALSE);
    int wd = inotify_add_watch((int)jfd, path,(int)jmask);
    (*env)->ReleaseStringUTFChars(env, jpath, path);
    return (jint)wd;
}

/*
 * Class:     com_megacreep_jinotify_NativeInotify
 * Method:    removeWatch
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_megacreep_jinotify_NativeInotify_removeWatch
    (JNIEnv *env, jobject jobj, jint jfd, jint jwd)
{
    int ret = inotify_rm_watch((int)jfd, (int)jwd);
    return (jint)ret;
}

/*
 * Class:     com_megacreep_jinotify_NativeInotify
 * Method:    takeEvent
 * Signature: (II)Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_megacreep_jinotify_NativeInotify_takeEvent
    (JNIEnv *env, jobject jobj, jint jfd, jint jwd)
{
    /* Some systems cannot read integer variables if they are not
       properly aligned. On other systems, incorrect alignment may
       decrease performance. Hence, the buffer used for reading from
       the inotify file descriptor should have the same alignment as
       struct inotify_event. */
    char buf[4096] __attribute__ ((aligned(__alignof__(struct inotify_event))));
    const struct inotify_event *event;
    char *ptr;
    struct pollfd fds[1];
    fds[0].fd = jfd;
    fds[0].events = POLLIN;
    int nfds = 1;
    int poll_num;
    for(;;){
        poll_num = poll(fds, nfds, -1);
        if(poll_num<=0){
            continue;
        }
        if (!(fds[0].revents & POLLIN)){
            continue;
        }
        ssize_t len = read(jfd, buf, sizeof buf);
        if (len == -1 && errno != EAGAIN) {
            perror("read");
            return NULL;
        }
        jclass cls_list = (*env)->FindClass(env,"java/util/ArrayList");
        jmethodID constructor_list = (*env)->GetMethodID(env,cls_list,"<init>","()V");
        jobject obj_list = (*env)->NewObject(env,cls_list,constructor_list);
        jmethodID list_add = (*env)->GetMethodID(env,cls_list,"add","(Ljava/lang/Object;)Z");
        /* Loop over all events in the buffer */
        if(len > 0){
            jclass cls  = (*env)->FindClass(env,"com/megacreep/jinotify/InotifyEvent");
            jmethodID constructor = (*env)->GetMethodID(env,cls,"<init>","()V");
            jfieldID fwd = (*env)->GetFieldID(env,cls,"wd","I");
            jfieldID fmask = (*env)->GetFieldID(env,cls,"mask","I");
            jfieldID fcookie = (*env)->GetFieldID(env,cls,"cookie","I");
            jfieldID flen = (*env)->GetFieldID(env,cls,"len","I");
            jfieldID fname = (*env)->GetFieldID(env,cls,"name","Ljava/lang/String;");
            for (ptr = buf; ptr < buf + len; ptr += sizeof(struct inotify_event) + event->len) {
                event = (const struct inotify_event *) ptr;
                jobject jevent = (*env)->NewObject(env, cls, constructor);
                (*env)->SetIntField(env,jevent,fwd,event->wd);
                (*env)->SetIntField(env,jevent,fmask,event->mask);
                (*env)->SetIntField(env,jevent,fcookie,event->cookie);
                (*env)->SetIntField(env,jevent,flen,event->len);
                jstring jstr = (*env)->NewStringUTF(env,event->name);
                (*env)->SetObjectField(env,jevent,fname,jstr);
                (*env)->CallObjectMethod(env,obj_list,list_add,jevent);
            }
        }
        return obj_list;
    }
}