#include "com_mymobkit_audio_PacketLossConcealer.h"
#include "loghelper.h"
#if defined(HAVE_CONFIG_H)
#include "config.h"
#endif

#define INT16_MIN (-32767-1)
#define INT16_MAX (32767)
#define INT32_MAX (0x7FFFFFFF)
#define INT32_MIN (-INT32_MAX-1)

#include <stdio.h>
#include <inttypes.h>
#include <stdlib.h>
#include <string.h>
#include <tgmath.h>
#include <limits.h>

#include "spandsp/telephony.h"
#include "spandsp/fast_convert.h"
#include "spandsp/plc.h"
#include "spandsp/time_scale.h"
// #include "spandsp/saturated.h"

static plc_state_t plcState;
static time_scale_state_t *tsState;
static char plcInit = 0;


JNIEXPORT void JNICALL Java_com_mymobkit_audio_PacketLossConcealer_init (JNIEnv *env, jclass c) {
  if( plcInit != 0 ){
    logv( env, "WARNING: SPANDSP is already initialized..." );
  }
  if( NULL == plc_init( &plcState ) ) {
    logv( env, "error initializing plc state" );
    return;
  }
  if( NULL == (tsState = time_scale_init( NULL, 8000, 1 ) ) ) {
    logv( env, "error initializing time scale state" );
    return;
  }

  plcInit = 1;
}

JNIEXPORT void JNICALL Java_com_mymobkit_audio_PacketLossConcealer_rx (JNIEnv *env, jclass c, jshortArray audioData ) {
  if( !plcInit ) {
    logv( env, "tried to call PacketLossConcealer.rx() without initialization" );
    return;
  }

  jint len = env->GetArrayLength( audioData );

  jshort *raw_audio = env->GetShortArrayElements( audioData, NULL );
  plc_rx( &plcState, raw_audio, len );
  env->ReleaseShortArrayElements( audioData, raw_audio, 0 );
}

JNIEXPORT void JNICALL Java_com_mymobkit_audio_PacketLossConcealer_fillIn (JNIEnv *env, jclass c, jshortArray audioData ) {
  if( !plcInit ) {
    logv( env, "tried to call PacketLossConcealer.fillIn() without initialization" );
    return;
  }

  //consider using GetPrimitiveArrayCritical?
  jint len = env->GetArrayLength( audioData );
  jshort *raw_audio = (jshort*)env->GetShortArrayElements( audioData, NULL );
  plc_fillin( &plcState, raw_audio, len );
  env->ReleaseShortArrayElements( audioData, raw_audio, 0 );
}

JNIEXPORT jint JNICALL Java_com_mymobkit_audio_PacketLossConcealer_maxOutputLengthAtSpeed (JNIEnv *env, jclass c, jint inLength, jfloat rate ) {
  if( 0 != time_scale_rate( tsState, rate ) ) {
    logv( env, "rate change failed" );
    return -1;
  }
  return time_scale_max_output_len( tsState, inLength );  
}
JNIEXPORT jint JNICALL Java_com_mymobkit_audio_PacketLossConcealer_changeSpeed (JNIEnv *env, jclass c, jshortArray output, jshortArray input, jint inputLen, jfloat rate ) {
  curenv( env );
  if( 0 != time_scale_rate( tsState, rate ) ) {
    logv( env, "rate change failed" );
    return -1;
  }

  jint out_buffer_len = env->GetArrayLength( output );
  if( out_buffer_len < time_scale_max_output_len( tsState, inputLen ) ) {
    logv( env, "output buffer too small for rate change" );
    return -1;
  }

  jshort *inBuf  = env->GetShortArrayElements( input , NULL );
  jshort *outBuf = env->GetShortArrayElements( output, NULL );
  
  int outLen = time_scale( tsState, outBuf, inBuf, inputLen );

  env->ReleaseShortArrayElements( input , inBuf , JNI_ABORT  );
  env->ReleaseShortArrayElements( output, outBuf, 0 );

  return outLen;
}
