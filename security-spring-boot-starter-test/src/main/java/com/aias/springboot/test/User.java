package com.aias.springboot.test;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7616779447033714705L;

	/**
	 * 
	 */

	private Long id;

    private String phone;

    private String username;

    private Date createTime;


    //区分是不是管理员
    private Long role;


}
