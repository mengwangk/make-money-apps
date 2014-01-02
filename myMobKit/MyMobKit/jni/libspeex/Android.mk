LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -DFIXED_POINT -DEXPORT="" -UHAVE_CONFIG_H -DUSE_KISS_FFT -DHAVE_SINF -DHAVE_TANF -DHAVE_COSF -DHAVE_ASINF -DHAVE_ATANF -DHAVE_ACOSF -DHAVE_ATAN2F -DHAVE_CEILF -DHAVE_FLOORF -DHAVE_POWF -DHAVE_LOG10F
LOCAL_MODULE := redspeex
LOCAL_SRC_FILES :=  PacketLossConcealer.cpp plc.c time_scale.c playout.c cb_search.c 	exc_10_32_table.c 	exc_8_128_table.c \
	filters.c 	gain_table.c 	hexc_table.c 	high_lsp_tables.c 	lsp.c \
	ltp.c 	speex.c 	stereo.c 	vbr.c 	vq.c bits.c exc_10_16_table.c \
	exc_20_32_table.c exc_5_256_table.c exc_5_64_table.c gain_table_lbr.c hexc_10_32_table.c \
	lpc.c lsp_tables_nb.c modes.c modes_wb.c nb_celp.c quant_lsp.c sb_celp.c \
	speex_callbacks.c speex_header.c window.c SpeexCodec.cpp

include $(BUILD_SHARED_LIBRARY)
