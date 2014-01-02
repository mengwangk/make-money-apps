// JpegHandler.h

#ifndef __JPEG_HANDLER_H__
#define __JPEG_HANDLER_H__

#include <stdio.h>

extern "C" {
#include "jpeglib.h"
#include "jerror.h"
}

#include <setjmp.h>

#define DEFAULT_JPEG_WRITE_BUFFER_SIZE 3000

#define RGB565_MASK_RED 0xF800
#define RGB565_MASK_GREEN 0x07E0
#define RGB565_MASK_BLUE 0x001F

class CJpegEncoder
{
public:
	CJpegEncoder();
	~CJpegEncoder();

	void InitRGB();
	void InitYUV(int frameBufferLen, int frameH, int frameW);

	unsigned char* GetFrameBuffer();

	int SetQuality(int quality);

	int EncodeYUV420SP(const unsigned char* srcBuff, int width = -1, int height = -1);
	int EncodeYUV420P(const unsigned char* srcBuff, int width, int height);
	int EncodeYV12(const unsigned char* srcBuff, int width, int height);
    int EncodeRGB24(const unsigned char* srcBuff, int width, int height);
	int EncodeRGB565(const unsigned char* srcBuff, int width, int height, bool isReversed);

//private:
// Methods:
	static void error_exit(j_common_ptr cinfo);
	static void output_message(j_common_ptr cinfo);

	static void init_destination(j_compress_ptr cinfo);
	static boolean empty_output_buffer(j_compress_ptr cinfo);
	static void term_destination(j_compress_ptr cinfo);

// Data:
	int planeHeight;
	int planeDataSize;

	JSAMPROW uPlaneData;
	JSAMPROW vPlaneData;

	JSAMPARRAY yPlane;
	JSAMPARRAY uPlane;
	JSAMPARRAY vPlane;

	bool cinfoCreated;
	struct jpeg_compress_struct cinfo;
	struct jpeg_error_mgr jerr;
	struct jpeg_destination_mgr destMgr;

	jmp_buf returnPoint;
	char messageBuffer[JMSG_LENGTH_MAX + 1];

	unsigned char* headerAndWriteBuffer;
	unsigned char* writeBuffer;
	int writeBufferSize;
	unsigned char* frameBuffer;
	int frameBufferSize;
	int written;
	int frameWidth;
	int frameHeight;
};

#endif//__JPEG_HANDLER_H__
