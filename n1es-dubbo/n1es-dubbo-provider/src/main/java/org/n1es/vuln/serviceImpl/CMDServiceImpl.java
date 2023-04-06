package org.n1es.vuln.serviceImpl;

import org.apache.dubbo.common.utils.IOUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.n1es.interfaces.CMDService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@DubboService
public class CMDServiceImpl implements CMDService {

	@Override
	public Map<String, Object> execCMD(String cmd) {
		HashMap<String, Object> data = new HashMap<>();
		try {
			InputStream in     = Runtime.getRuntime().exec(cmd).getInputStream();
			String[]    result = IOUtils.readLines(in);
			data.put("result", result);
			in.close();
		} catch (Exception e) {
			return null;
		}
		return data;
	}
}
