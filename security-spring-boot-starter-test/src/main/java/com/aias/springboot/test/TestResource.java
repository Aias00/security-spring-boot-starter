package com.aias.springboot.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestResource {

	/**
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/getResult", method = { RequestMethod.POST })
	public String getResult(String num) {
		return "result success" + num;
	}

}
