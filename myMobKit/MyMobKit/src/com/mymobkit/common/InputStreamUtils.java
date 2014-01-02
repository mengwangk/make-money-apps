package com.mymobkit.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * 
 */
public final class InputStreamUtils {

	private final static int BUFFER_SIZE = 4096;

	/**************************************************************************
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 *             This function converts an InputStream to a String
	 **************************************************************************/

	public static String inputStreamToString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;

		return new String(outStream.toByteArray(), "ISO-8859-1");

	}

	/**
	 * This function converts an InputStream in a predefined encoding to a
	 * String.
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String inputStreamToString(InputStream in, String encoding) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;

		return new String(outStream.toByteArray(), encoding);

	}

	/**************************************************************************
	 * 
	 * @param in
	 *            String
	 * @return InputStream
	 * @throws Exception
	 * 
	 *             This function converts a String to a InputStream
	 **************************************************************************/

	public static InputStream stringToInputStream(String in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));

		return is;
	}

	/**************************************************************************
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 * 
	 *             This function converts an InputStream to a byte[]
	 **************************************************************************/

	public static byte[] inputStreamToByte(InputStream in) throws IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;

		return outStream.toByteArray();

	}

	/**
	 * This function converts a byte[] into a InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) throws Exception {

		ByteArrayInputStream resultado = new ByteArrayInputStream(in);

		return resultado;
	}

	/**
	 * This function converts byte[] into a String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String byteToString(byte[] in) throws Exception {
		InputStream ins = byteTOInputStream(in);
		return inputStreamToString(ins);
	}

}