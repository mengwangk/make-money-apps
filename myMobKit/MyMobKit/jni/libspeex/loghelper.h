#include "jni.h"
void logv( JNIEnv *env, const char *msg, ... );
#ifdef __cplusplus
extern "C" {
#endif
void loge( const char *msg, ... );
void curenv( JNIEnv *env );
#ifdef __cplusplus
}
#endif
