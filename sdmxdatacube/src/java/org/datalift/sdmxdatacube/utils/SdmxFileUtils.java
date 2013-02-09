package org.datalift.sdmxdatacube.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Utility for SDMX files
 *
 * @author thibaut
 *
 */
public class SdmxFileUtils {

	/**
	 * Check if a Reader contains Sdmx XML
	 *
	 * @param i
	 *            the Reader
	 * @return True if i contains SDMX XML, else false
	 * @throws IOException
	 */
	public static boolean isSdmx(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		boolean comment = false;
		int max_line = 100;
		String uri_sdmx = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/";

		while ((line = in.readLine()) != null && max_line > 0) {
			if (comment) {
				if (line.contains("-->"))
					comment = false;
			} else {
				if (line.contains("<!--") && line.contains("-->"))
					comment = false;
				else if (line.contains("<!--"))
					comment = true;
			}

			if (!comment) {
				--max_line;
				if (line.contains(uri_sdmx))
					return true;
			}
		}
		return false;
	}
}
