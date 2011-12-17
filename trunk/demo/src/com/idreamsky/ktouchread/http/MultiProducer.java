package com.idreamsky.ktouchread.http;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.entity.ContentProducer;

public class MultiProducer implements ContentProducer {

	private byte[] mByteArray;
	private String mContentType;
	private HashMap<String, String> mParams;

	public static final String BOUNDARY = "----123456789";

	public MultiProducer(byte[] data, String contentType,
			HashMap<String, String> params) {
		mByteArray = data;
		mContentType = contentType;
		mParams = params;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		final StringBuilder sb = new StringBuilder();
		HashMap<String, String> params = mParams;
		if (null != params && params.size() > 0) {
			Set<String> keyset = params.keySet();
			for (String key : keyset) {
				// Write boundary
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"");
				sb.append(key);
				sb.append("\"\r\n\r\n");
				sb.append(params.get(key));
				sb.append("\r\n");
			}
		}
		/*
		 * Append boundary
		 */
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");

		sb.append("Content-Disposition: form-data; name=\"image\"; "
				+ "filename=\"123.jpeg\"\r\n");
		sb.append("Content-Type: ");
		sb.append(mContentType);

		/*
		 * Skip two lines to start writing file data for name 'image'.
		 */
		sb.append("\r\n\r\n");
		outstream.write(sb.toString().getBytes());

		/*
		 * This is a big error here. StringBuilder#append(Object obj) will just
		 * append the obj's hash value, in other words, it equals to
		 * append(obj.toString()).
		 * 
		 * <p>
		 * 
		 * FIXME So here append the byte[] object will not actually append its
		 * array content.
		 * 
		 * sb.append(mByteArray);
		 */

		outstream.write(mByteArray);

		sb.setLength(0);
		sb.append("\r\n");

		/*
		 * Over
		 */
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("--\r\n");
		outstream.write(sb.toString().getBytes());
		outstream.flush();
	}
}


