package org.n1es.vuln.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.n1es.interfaces.CMDService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/CMD/")
public class CMDController {

	@DubboReference
	private CMDService cmdService;

	@GetMapping("/get/cmd.do")
	public Map<String, Object> getProcessBuilder(String cmd) throws Exception {
		return cmdService.execCMD(cmd);
	}
}
