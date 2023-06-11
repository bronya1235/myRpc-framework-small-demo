package com.github.bronya1235.netty.nameService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Bao
 * @Date: 2023/6/9-06-09-21:30
 * @Description com.github.bronya1234.netty.nameService
 * @Function
 */
public class Metadata extends HashMap<String, List<URI>> {
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Metadata:").append("\n");
		for (Entry<String, List<URI>> entry : entrySet()) {
			sb.append("\t").append("Classname: ")
					.append(entry.getKey()).append("\n");
			sb.append("\t").append("URIs:").append("\n");
			for (URI uri : entry.getValue()) {
				sb.append("\t\t").append(uri).append("\n");
			}
		}
		return sb.toString();
	}
}
