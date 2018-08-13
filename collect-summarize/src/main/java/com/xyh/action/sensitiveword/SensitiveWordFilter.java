package com.xyh.action.sensitiveword;

import java.util.HashSet;
import java.util.Set;

/**
 * 敏感词过滤实践
 * @author hcxyh  2018年8月13日
 * 敏感词过滤功能在很多地方都会用到，理论上在Web应用中，只要涉及用户输入的地方，都需要进行文本校验，
 * 如：XSS校验、SQL注入检验、敏感词过滤等。
 * TODO https://github.com/hzjjames/TextFilter
 */
public class SensitiveWordFilter {
	
	//暴力遍历(很吃cpu)
	public boolean test01() {
		Set<String> setSensitiveWord = new HashSet<>();
		setSensitiveWord.add("shit");
		setSensitiveWord.add("傻逼");
		for(String sensitiveWord : setSensitiveWord) {
			if(setSensitiveWord.contains(sensitiveWord)) {
				return false;
			}
		}
		return true;
	}
	
	//确定又穷自动机算法
	//基本思想是基于状态转移来检索敏感词，只需要扫描一次待检测文本，就能对所有敏感词进行检测，
	public boolean testDeterministicFiniteAutomaton() {
		
		return true;
	}
	
}
