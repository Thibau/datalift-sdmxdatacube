package org.datalift.sdmxdatacube.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SdmxFileUtils {

	public static boolean isSdmx(Reader i) throws IOException {
		BufferedReader in = new BufferedReader(i);
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
