package com.txyjssr.share;

import java.io.InputStream;
import java.io.OutputStream;

public interface Communication {
	public void execute(OutputStream out, InputStream in);
}