#include "JpegHandler.h"

#include <stdlib.h>

#include "JpegHandler_jni.h"
#include "LogUtil.h"

#define JPEG_QUALITY 70
#define PKG_HEADER_SIZE 4

CJpegEncoder* enc = NULL;

JNIEXPORT jbyteArray JNICALL Java_com_mymobkit_video_JpegHandler_initRgb(JNIEnv * env, jobject obj)
{
	enc = new CJpegEncoder();
	enc->InitRGB();
	jbyteArray bArray = env->NewByteArray(4 + enc->written);

		enc->headerAndWriteBuffer[0] = (unsigned char) 0;// jpeg header
		enc->headerAndWriteBuffer[1] = (unsigned char) ((enc->written >> 16) & 0xFF);
		enc->headerAndWriteBuffer[2] = (unsigned char) ((enc->written >> 8) & 0xFF);
		enc->headerAndWriteBuffer[3] = (unsigned char) ((enc->written >> 0) & 0xFF);

	env->SetByteArrayRegion(bArray, 0, 4 + enc->written, (jbyte *)enc->headerAndWriteBuffer);//writeBuffer);
	return bArray;
}

JNIEXPORT jbyteArray JNICALL Java_com_mymobkit_video_JpegHandler_initYuv(
	JNIEnv * env, jobject obj, jint frameBufferSize, jint frameWidth, jint frameHeight)
{
	if(enc != NULL)
	{
		delete enc;
	}
	enc = new CJpegEncoder();
	enc->InitYUV(frameBufferSize, frameWidth, frameHeight);
	jbyteArray bArray = env->NewByteArray(4 + enc->written);

		enc->headerAndWriteBuffer[0] = (unsigned char) 0;// jpeg header
		enc->headerAndWriteBuffer[1] = (unsigned char) ((enc->written >> 16) & 0xFF);
		enc->headerAndWriteBuffer[2] = (unsigned char) ((enc->written >> 8) & 0xFF);
		enc->headerAndWriteBuffer[3] = (unsigned char) ((enc->written >> 0) & 0xFF);

	env->SetByteArrayRegion(bArray, 0, 4 + enc->written, (jbyte *)enc->headerAndWriteBuffer);
	return bArray;
}

JNIEXPORT jbyteArray JNICALL Java_com_mymobkit_video_JpegHandler_encodeYUV420SP(JNIEnv * env, jobject obj, jbyteArray data)
{
	jint len = env->GetArrayLength(data);
	jbyte* copyBuf = (jbyte*)enc->GetFrameBuffer();
	env->GetByteArrayRegion(data, 0, len, copyBuf);

	enc->EncodeYUV420SP((const unsigned char*) copyBuf);

	jbyteArray bArray = env->NewByteArray(4 + enc->written);

		enc->headerAndWriteBuffer[0] = (unsigned char) 1;// jpeg header
		enc->headerAndWriteBuffer[1] = (unsigned char) ((enc->written >> 16) & 0xFF);
		enc->headerAndWriteBuffer[2] = (unsigned char) ((enc->written >> 8) & 0xFF);
		enc->headerAndWriteBuffer[3] = (unsigned char) ((enc->written >> 0) & 0xFF);

	env->SetByteArrayRegion(bArray, 0, 4 + enc->written, (jbyte *)enc->headerAndWriteBuffer);//writeBuffer);
	return bArray;
}

CJpegEncoder::CJpegEncoder():
	planeHeight(0),
	planeDataSize(0),
	yPlane(NULL),
	uPlane(NULL),
	vPlane(NULL),
	cinfoCreated(FALSE),
	headerAndWriteBuffer(NULL),
	writeBuffer(NULL),
	writeBufferSize(DEFAULT_JPEG_WRITE_BUFFER_SIZE),
	frameBuffer(NULL),
	frameBufferSize(0),
	written(0)
{
	headerAndWriteBuffer = (unsigned char*) malloc(PKG_HEADER_SIZE + writeBufferSize);
	writeBuffer = headerAndWriteBuffer + PKG_HEADER_SIZE;

	destMgr.init_destination = init_destination;
	destMgr.empty_output_buffer = empty_output_buffer;
	destMgr.term_destination = term_destination;

	cinfo.client_data = (void*) this;

	cinfo.err = jpeg_std_error(&jerr);
	jerr.error_exit = error_exit;
	jerr.output_message = output_message;
}

CJpegEncoder::~CJpegEncoder()
{
	if(cinfoCreated)
		jpeg_destroy_compress(&cinfo);
	free(headerAndWriteBuffer);
	headerAndWriteBuffer = NULL;
	writeBuffer = NULL;
	writeBufferSize = 0;
	written = 0;

	if(frameBuffer != NULL)
	{
		free(frameBuffer);
		frameBuffer = NULL;
		frameBufferSize = 0;
	}

	if(uPlaneData != NULL)
	{
		free(uPlaneData);
		uPlaneData = NULL;
	}
	if(vPlaneData != NULL)
	{
		free(vPlaneData);
		vPlaneData = NULL;
	}

	if(yPlane != NULL)
	{
		free(yPlane);
		yPlane = NULL;
	}
	if(uPlane != NULL)
	{
		free(uPlane);
		uPlane = NULL;
	}
	if(vPlane != NULL)
	{
		free(vPlane);
		vPlane = NULL;
	}
}

void CJpegEncoder::InitRGB()
{
	if(cinfoCreated)
		return;
	cinfoCreated = TRUE;
	jpeg_create_compress(&cinfo);

	cinfo.dest = &destMgr;

	cinfo.input_components = 3;
	cinfo.in_color_space = JCS_RGB;

	jpeg_set_defaults(&cinfo);

	cinfo.dct_method = JDCT_FASTEST;
	cinfo.optimize_coding = FALSE;

	SetQuality(JPEG_QUALITY);
}

void CJpegEncoder::InitYUV(int frameBufferLen, int frameW, int frameH)
{
	if(cinfoCreated)
		return;
	cinfoCreated = TRUE;
	jpeg_create_compress(&cinfo);

	cinfo.dest = &destMgr;

	cinfo.input_components = 3;
	cinfo.in_color_space = JCS_YCbCr;

	jpeg_set_defaults(&cinfo);

	cinfo.dct_method = JDCT_FASTEST;
	cinfo.optimize_coding = FALSE;

	cinfo.raw_data_in = TRUE;
	jpeg_set_colorspace(&cinfo, JCS_YCbCr);

	SetQuality(JPEG_QUALITY);

	frameBufferSize = frameBufferLen;
	frameBuffer = (unsigned char*) malloc(frameBufferSize);
	frameWidth = frameW;
	frameHeight = frameH;
}

int CJpegEncoder::SetQuality(int quality)
{
	if(setjmp(returnPoint))
	{
		return -1;
	}

	jpeg_set_quality(&cinfo, quality, FALSE);
	jpeg_suppress_tables(&cinfo, FALSE);
	jpeg_write_tables(&cinfo);
	return 0;
}

unsigned char* CJpegEncoder::GetFrameBuffer()
{
	return frameBuffer;
}

int CJpegEncoder::EncodeYUV420SP(const unsigned char* srcBuff, int width, int height)
{
	if(width == -1 || height == -1)
	{
		width = frameWidth;
		height = frameHeight;
	}

	if(planeHeight < height || yPlane == NULL)
	{
		free(yPlane);
		free(uPlane);
		free(vPlane);
		yPlane = (JSAMPARRAY) malloc( sizeof(JSAMPROW) * height);
		uPlane = (JSAMPARRAY) malloc( sizeof(JSAMPROW) * (height/2) );
		vPlane = (JSAMPARRAY) malloc( sizeof(JSAMPROW) * (height/2) );
		planeHeight = height;
	}

	if(planeDataSize < (width/2) * (height/2) || uPlaneData == NULL)
	{
		free(uPlaneData);
		free(vPlaneData);
		planeDataSize = (width/2) * (height/2);
		uPlaneData = (JSAMPROW) malloc(planeDataSize);
		vPlaneData = (JSAMPROW) malloc(planeDataSize);
	}

	if(setjmp(returnPoint))
	{
		free(uPlaneData); uPlaneData = NULL;
		free(vPlaneData); vPlaneData = NULL;

		free(yPlane); yPlane = NULL;
		free(uPlane); uPlane = NULL;
		free(vPlane); vPlane = NULL;
		return -1;
	}


	cinfo.image_width = width;
	cinfo.image_height = height;

	cinfo.comp_info[0].h_samp_factor = cinfo.comp_info[0].v_samp_factor = 2;
	cinfo.comp_info[1].h_samp_factor = cinfo.comp_info[1].v_samp_factor = 1;
	cinfo.comp_info[2].h_samp_factor = cinfo.comp_info[2].v_samp_factor = 1;
	//
	for(int i = 0; i < height; i++)
		yPlane[i] = (JSAMPROW) &srcBuff[i * width];
	for(int i = 0; i < height/2; i++) {
		uPlane[i] = &uPlaneData[i * (width/2)];
		vPlane[i] = &vPlaneData[i * (width/2)];
	}

	JSAMPROW cbPtr = uPlaneData;
	JSAMPROW crPtr = vPlaneData;
	JSAMPROW inPtr = (JSAMPROW) &srcBuff[width * height];
	for(int i = 0; i < planeDataSize; i++)
	{
		*cbPtr++ = inPtr[0];
		*crPtr++ = inPtr[1];
		inPtr += 2;
	}

	jpeg_start_compress(&cinfo, FALSE);


	int y = 0;
	int blockrow = 0;
	while(y < height)
	{
		JSAMPARRAY planes[3];
		planes[0] = yPlane + cinfo.comp_info[0].v_samp_factor * DCTSIZE * blockrow;
//		planes[1] = uPlane + cinfo.comp_info[1].v_samp_factor * DCTSIZE * blockrow;
//		planes[2] = vPlane + cinfo.comp_info[2].v_samp_factor * DCTSIZE * blockrow;
		planes[1] = vPlane + cinfo.comp_info[1].v_samp_factor * DCTSIZE * blockrow;
		planes[2] = uPlane + cinfo.comp_info[2].v_samp_factor * DCTSIZE * blockrow;

		jpeg_write_raw_data(&cinfo, planes, cinfo.comp_info[0].v_samp_factor * DCTSIZE);
		y += cinfo.comp_info[0].v_samp_factor * DCTSIZE;
		blockrow++;
	}

	jpeg_finish_compress(&cinfo);

	return 0;
}

int CJpegEncoder::EncodeYUV420P(const unsigned char* srcBuff, int width, int height)
{
	if(planeHeight < height || yPlane == NULL)
	{
		free(yPlane);
		free(uPlane);
		free(vPlane);
		yPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW)*height);
		uPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW)*(height/2));
		vPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW)*(height/2));
		planeHeight = height;
	}

	if(setjmp(returnPoint))
	{
		free(yPlane); yPlane = NULL;
		free(uPlane); uPlane = NULL;
		free(vPlane); vPlane = NULL;
		return -1;
	}


	cinfo.image_width = width;
	cinfo.image_height = height;

	cinfo.comp_info[0].h_samp_factor = cinfo.comp_info[0].v_samp_factor = 2;
	cinfo.comp_info[1].h_samp_factor = cinfo.comp_info[1].v_samp_factor = 1;
	cinfo.comp_info[2].h_samp_factor = cinfo.comp_info[2].v_samp_factor = 1;
	for(int i = 0; i < height; i++)
		yPlane[i] = (JSAMPROW) &srcBuff[i * width];
	for(int i = 0; i < height/2; i++) {
		uPlane[i] = (JSAMPROW) &srcBuff[width * height + i * (width/2)];
		vPlane[i] = (JSAMPROW) &srcBuff[width * height + (width/2) * (height/2) + i * (width/2)];
	}

	jpeg_start_compress(&cinfo, FALSE);

	int y = 0;
	int blockrow = 0;
	while(y < height)
	{
		JSAMPARRAY planes[3];
		planes[0] = yPlane + cinfo.comp_info[0].v_samp_factor * DCTSIZE * blockrow;
		planes[1] = uPlane + cinfo.comp_info[1].v_samp_factor * DCTSIZE * blockrow;
		planes[2] = vPlane + cinfo.comp_info[2].v_samp_factor * DCTSIZE * blockrow;
		jpeg_write_raw_data(&cinfo, planes, cinfo.comp_info[0].v_samp_factor * DCTSIZE);
		y += cinfo.comp_info[0].v_samp_factor * DCTSIZE;
		blockrow++;
	}

	jpeg_finish_compress(&cinfo);

	return 0;
}

int CJpegEncoder::EncodeYV12(const unsigned char* srcBuff, int width, int height)
{
	if(planeHeight < height || yPlane == NULL)
	{
		free(yPlane);
		free(uPlane);
		free(vPlane);
		yPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW) * height);
		uPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW) * (height/2));
		vPlane = (JSAMPARRAY) malloc(sizeof(JSAMPROW) * (height/2));
		planeHeight = height;
	}

	if(setjmp(returnPoint))
	{
		free(yPlane); yPlane = NULL;
		free(uPlane); uPlane = NULL;
		free(vPlane); vPlane = NULL;
		return -1;
	}

	cinfo.image_width = width;
	cinfo.image_height = height;

	cinfo.comp_info[0].h_samp_factor = cinfo.comp_info[0].v_samp_factor = 2;
	cinfo.comp_info[1].h_samp_factor = cinfo.comp_info[1].v_samp_factor = 1;
	cinfo.comp_info[2].h_samp_factor = cinfo.comp_info[2].v_samp_factor = 1;
	for(int i = 0; i < height; i++)
		yPlane[i] = (JSAMPROW) &srcBuff[i * width];
	for(int i = 0; i < height/2; i++) {
		vPlane[i] = (JSAMPROW) &srcBuff[width * height + i * (width/2)];
		uPlane[i] = (JSAMPROW) &srcBuff[width * height + (width/2) * (height/2) + i * (width/2)];
	}

	jpeg_start_compress(&cinfo, FALSE);

	int y = 0;
	int blockrow = 0;
	while(y < height)
	{
		JSAMPARRAY planes[3];
		planes[0] = yPlane + cinfo.comp_info[0].v_samp_factor * DCTSIZE * blockrow;
		planes[1] = uPlane + cinfo.comp_info[1].v_samp_factor * DCTSIZE * blockrow;
		planes[2] = vPlane + cinfo.comp_info[2].v_samp_factor * DCTSIZE * blockrow;
		jpeg_write_raw_data(&cinfo, planes, cinfo.comp_info[0].v_samp_factor * DCTSIZE);
		y += cinfo.comp_info[0].v_samp_factor * DCTSIZE;
		blockrow++;
	}

	jpeg_finish_compress(&cinfo);

	return 0;
}

int CJpegEncoder::EncodeRGB24(const unsigned char* srcBuff, int width, int height)
{
	JSAMPROW row_pointer[1];			// pointer to a single row
	int row_stride = width * 3;			// physical row width in buffer

	if(setjmp(returnPoint))
	{
		return -1;
	}

	cinfo.image_width = width;
	cinfo.image_height = height;

	jpeg_start_compress(&cinfo, FALSE);

	while(cinfo.next_scanline < cinfo.image_height)
	{
	    row_pointer[0] = (JSAMPROW) &srcBuff[cinfo.next_scanline * row_stride];
	    jpeg_write_scanlines(&cinfo, row_pointer, 1);
	}

	jpeg_finish_compress(&cinfo);

	return 0;
}

int CJpegEncoder::EncodeRGB565(const unsigned char* srcBuff, int width, int height, bool isReversed)
{
	JSAMPROW row_pointer[1];			// pointer to a single row
	int row_stride = width * 3;			// physical row width in buffer
	short pixel = 0;
	int crtRow = 0;
	if(setjmp(returnPoint))
	{
		return -1;
	}

	unsigned char* lineRGB24 = (unsigned char*) malloc(width * 3);
	cinfo.image_width = width;
	cinfo.image_height = height;

	jpeg_start_compress(&cinfo, FALSE);

	while(cinfo.next_scanline < cinfo.image_height)
	{
		if(isReversed)
			crtRow = cinfo.image_height - 1 - cinfo.next_scanline;
		else
			crtRow = cinfo.next_scanline;

		for(int w = 0; w < width; w++)
		{
			pixel = *(((short *)srcBuff) + crtRow * width + w);
			lineRGB24[w*3] = (pixel & RGB565_MASK_RED) >> 8;
			lineRGB24[w*3 + 1] = (pixel & RGB565_MASK_GREEN) >> 3;
			lineRGB24[w*3 + 2] = (pixel & RGB565_MASK_BLUE) << 3;
		}
		row_pointer[0] = (JSAMPROW) lineRGB24;
	    jpeg_write_scanlines(&cinfo, row_pointer, 1);
	}

	jpeg_finish_compress(&cinfo);
	free(lineRGB24);

	return 0;
}

void CJpegEncoder::init_destination(j_compress_ptr cinfo)
{
	CJpegEncoder* data = (CJpegEncoder*) cinfo->client_data;
	cinfo->dest->next_output_byte = data->writeBuffer;
	cinfo->dest->free_in_buffer = data->writeBufferSize;
}

boolean CJpegEncoder::empty_output_buffer(j_compress_ptr cinfo)
{
	CJpegEncoder* data = (CJpegEncoder*) cinfo->client_data;
	int pos = data->writeBufferSize;
	data->writeBufferSize *= 2;
	unsigned char* newBuf = (unsigned char*) realloc(data->headerAndWriteBuffer, PKG_HEADER_SIZE + data->writeBufferSize);
	if (!newBuf)
		ERREXIT(cinfo, JERR_FILE_WRITE);
	data->headerAndWriteBuffer = newBuf;
	data->writeBuffer = PKG_HEADER_SIZE + data->headerAndWriteBuffer;

	cinfo->dest->next_output_byte = data->writeBuffer + pos;
	cinfo->dest->free_in_buffer = data->writeBufferSize - pos;
	return TRUE;
}

void CJpegEncoder::term_destination(j_compress_ptr cinfo)
{
	CJpegEncoder* data = (CJpegEncoder*) cinfo->client_data;
	data->written = data->writeBufferSize - cinfo->dest->free_in_buffer;
}

void CJpegEncoder::error_exit(j_common_ptr cinfo)
{
	CJpegEncoder* data = (CJpegEncoder*) cinfo->client_data;
	cinfo->err->format_message(cinfo, data->messageBuffer);
	printf("error_exit(): %s\n", data->messageBuffer);
	longjmp(data->returnPoint, 1);
}

void CJpegEncoder::output_message(j_common_ptr cinfo)
{
	printf("Outputting message:\n");
	char buf[JMSG_LENGTH_MAX + 1];
	cinfo->err->format_message(cinfo, buf);
	printf("%s\n", buf);
}
