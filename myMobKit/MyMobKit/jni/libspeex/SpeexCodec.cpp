#include "com_mymobkit_audio_codec_SpeexCodec.h"
#include "speex/speex.h"
#include "speex/speex_preprocess.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include "loghelper.h"

#define MAX_DEC_FRAMES 10

static void *enc; //speex encoder
static void *dec; //speex decoder
static SpeexBits enc_bits, dec_bits;
static int enc_frame_size, dec_frame_size;
static int initialized = 0;

static spx_int16_t *dec_buffer;
static char *enc_buffer;

//static JitterBuffer *jitter;

//SpeexPreprocessState *prepState;



void logv( JNIEnv *env, const char *msg, ... ) {
  jmethodID logw;
  jclass logc;
  
  logc = env->FindClass( "android.util.Log" );
  logw = env->GetStaticMethodID( logc, "w", "(Ljava/lang/String;Ljava/lang/String;)I" ); 

  if( logc == NULL || logw == NULL ) return;

  va_list list;
  va_start( list, msg );
  char fullmsg[1024];
  vsnprintf( fullmsg, 1024, msg, list );

  jstring tagStr = env->NewStringUTF( "SPEEX-JNI" );
  jstring msgStr = env->NewStringUTF( fullmsg );

  env->CallStaticIntMethod( logc, logw, tagStr, msgStr );

  //don't know why I do this here... TODO try removing this
  const char *utf = env->GetStringUTFChars( msgStr, NULL );
  env->ReleaseStringUTFChars( msgStr, utf );

  utf = env->GetStringUTFChars( tagStr, NULL );
  env->ReleaseStringUTFChars( tagStr, utf );
}

static JNIEnv *cenv;

void curenv( JNIEnv *_c ) {
  cenv = _c;
}

extern "C" void loge( const char *msg, ... ) {
  jmethodID logw;
  jclass logc;
  
  logc = cenv->FindClass( "android.util.Log" );
  logw = cenv->GetStaticMethodID( logc, "w", "(Ljava/lang/String;Ljava/lang/String;)I" ); 

  if( logc == NULL || logw == NULL ) return;

  va_list list;
  va_start( list, msg );
  char fullmsg[1024];
  vsnprintf( fullmsg, 1024, msg, list );

  jstring tagStr = cenv->NewStringUTF( "SPEEX-JNI" );
  jstring msgStr = cenv->NewStringUTF( fullmsg );

  cenv->CallStaticIntMethod( logc, logw, tagStr, msgStr );

  //don't know why I do this here... TODO try removing this
  const char *utf = cenv->GetStringUTFChars( msgStr, NULL );
  cenv->ReleaseStringUTFChars( msgStr, utf );

  utf = cenv->GetStringUTFChars( tagStr, NULL );
  cenv->ReleaseStringUTFChars( tagStr, utf );
}

JNIEXPORT jint JNICALL Java_com_mymobkit_audio_codec_SpeexCodec_openSpeex (JNIEnv *env, jobject obj){

  if( initialized ) {
    logv( env, "speex codec already initialized:%d", initialized );
    return 0;
  }

  logv( env, "Initializing Speex Codec" );

  enc = speex_encoder_init( speex_lib_get_mode( SPEEX_MODEID_NB ) );
  dec = speex_decoder_init( speex_lib_get_mode( SPEEX_MODEID_NB ) ); 

  if( enc == NULL ) {
    logv( env, "speex encoder init failed" );
    return 1;
  }
  if( dec == NULL ) {
    logv( env, "speex decoder init failed" );
    return 1;
  }

  //  logv( env, "speex enc/dec allocated" );

  spx_int32_t tmp;
  tmp=1;
  speex_decoder_ctl(dec, SPEEX_SET_ENH, &tmp);
  tmp=0;
  speex_encoder_ctl(enc, SPEEX_SET_VBR, &tmp);
  tmp=3;
  speex_encoder_ctl(enc, SPEEX_SET_QUALITY, &tmp);
  tmp=1;
  speex_encoder_ctl(enc, SPEEX_SET_COMPLEXITY, &tmp);

  speex_encoder_ctl(enc, SPEEX_GET_FRAME_SIZE, &enc_frame_size );
  speex_decoder_ctl(dec, SPEEX_GET_FRAME_SIZE, &dec_frame_size );

  logv( env, "frame sizes ... enc: %d, dec: %d", enc_frame_size, dec_frame_size );

  speex_bits_init(&enc_bits);
  speex_bits_init(&dec_bits);

  //  logv( env, "speex bits initialized" );

  enc_buffer = (char *) malloc( sizeof( char ) * enc_frame_size );
  dec_buffer = (spx_int16_t *) malloc( sizeof( spx_int16_t ) * dec_frame_size*MAX_DEC_FRAMES );

  if( enc_buffer == NULL || dec_buffer == NULL ) {
    logv( env, "buffer allocation failed" );
    return 1;
  }

  //  jitter = jitter_buffer_init( 20 );

  /*prepState = speex_preprocess_state_init( enc_frame_size, 8000 );
  if( prepState == NULL ) {
    logv( env, "preprocessor failed to initialize" );
    return 1;
  }
  tmp=1;
  speex_preprocess_ctl( prepState, SPEEX_PREPROCESS_SET_DENOISE, &tmp );
  tmp=1;
  speex_preprocess_ctl( prepState, SPEEX_PREPROCESS_SET_AGC, &tmp );
  tmp=0;
  speex_preprocess_ctl( prepState, SPEEX_PREPROCESS_SET_VAD, &tmp );
  //  float agcTarget = 32767*.8;
  //speex_preprocess_ctl( prepState, SPEEX_PREPROCESS_SET_AGC_LEVEL, &agcTarget );
  */
  initialized = 123;  

  return 0;
}
JNIEXPORT void JNICALL Java_com_mymobkit_audio_codec_SpeexCodec_closeSpeex (JNIEnv *env, jobject obj ){
  if( !initialized ) {
    logv( env, "tried to shut down speex before initialization" );
    return;
  }

  logv( env, "speex shutdown" );
  speex_bits_destroy( &enc_bits );
  speex_bits_destroy( &dec_bits );

  logv( env, "bits destroyed" );

  speex_encoder_destroy( enc );
  speex_decoder_destroy( dec );

  //  jitter_buffer_destroy( jitter );

  logv( env, "codecs destroyed" );
}

JNIEXPORT jint JNICALL Java_com_mymobkit_audio_codec_SpeexCodec_decode (JNIEnv *env, jobject obj, jbyteArray encArr, jshortArray decArr, jint encLen ){
  cenv = env;
  if( !initialized ) {
    logv(env, "tried to decode without initializing" );
    return -1;
  }

  if (decArr == NULL) {
    logv(env, "Speex decode passed a null decode buffer" );
    return -1;
  }

  jint dec_buffer_len = env->GetArrayLength( decArr );
  int dec_buffer_idx = 0;

  jbyte *jb_enc_stream;
  char *enc_stream;
  SpeexBits *dbits = NULL; //if this is null, speex will do PLC for us
  if( !env->IsSameObject( encArr, NULL ) ) {
    jb_enc_stream = env->GetByteArrayElements( encArr, NULL );
    enc_stream = (char *)jb_enc_stream;
    speex_bits_read_from( &dec_bits, enc_stream, encLen );
    env->ReleaseByteArrayElements( encArr, jb_enc_stream, JNI_ABORT );
    dbits = &dec_bits;
  } else {
    //    logv( env, "null encArr" );
  }

  while( 0 == speex_decode_int( dec, dbits, dec_buffer+dec_buffer_idx ) ) {
    dec_buffer_idx += dec_frame_size;
    if( dec_buffer_idx + dec_frame_size >= dec_buffer_len ) {
      logv( env, "out of space in the decArr buffer, idx=%d", dec_buffer_idx );
      break;
    }

    if( dec_buffer_idx + dec_frame_size >= dec_frame_size * MAX_DEC_FRAMES ) {
      logv( env, "out of space in the dec_buffer buffer, idx=%d", dec_buffer_idx );
      break;
    }

    if( dbits == NULL ) {
      //      logv(env, "did PLC" );
      break;//only generate one frame for PLC...
    }
  }
    
  env->SetShortArrayRegion( decArr, 0, dec_buffer_idx, dec_buffer );
  return dec_buffer_idx;
}

JNIEXPORT jint JNICALL Java_com_mymobkit_audio_codec_SpeexCodec_encode (JNIEnv *env, jobject obj, jshortArray decArr, jbyteArray encArr, jint rawLen ){
  if( !initialized ) {
    logv( env, "tried to encode without initializing" );
    return -1;
  }

  //  logv( env, "entered encode %d", rawLen );
  //  logv( env, "encoding %d samples", rawLen );
  jshort *raw_stream = env->GetShortArrayElements( decArr, NULL );
  speex_bits_reset( &enc_bits );

  //  logv( env, "bits reset" );
  //  speex_preprocess_run( prepState, (spx_int16_t *)raw_stream );
  speex_encode_int( enc, (spx_int16_t *)raw_stream, &enc_bits );

  //  logv( env, "encode complete" );

  env->ReleaseShortArrayElements( decArr, raw_stream, JNI_ABORT );

  //  logv( env, "writing up to %d bytes to buffer", enc_frame_size );

  int nbytes = speex_bits_write( &enc_bits, enc_buffer, enc_frame_size );

  //  logv( env, "wrote %d bytes to buffer", nbytes );

  env->SetByteArrayRegion( encArr, 0, nbytes, (jbyte*)enc_buffer );

  //  logv( env, "wrote back to java buffer" );

  return (jint)nbytes;
}
