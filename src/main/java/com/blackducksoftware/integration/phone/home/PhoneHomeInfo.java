/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.blackducksoftware.integration.phone.home;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author nrowles
 * 
 * 
 *         Information to be sent to a REST endpoint
 */
public class PhoneHomeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5604676370200060866L;

	private final String regId;
	private final Map<String, String> infoMap;

	public PhoneHomeInfo(String regId, Map<String, String> infoMap) {
		this.regId = Objects.requireNonNull(regId);
		this.infoMap = Objects.requireNonNull(infoMap);
	}

	public String getRegId() {
		return regId;
	}

	public Map<String, String> getInfoMap() {
		return infoMap;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("{regId:" + regId + ", ");
		
		str.append("infoMap:{");
		for(String key : infoMap.keySet()){
			str.append(key + ":" + infoMap.get(key));
			str.append(",");
		}
		str.deleteCharAt(str.length()-1);
		str.append("}}");
		
		return str.toString();
	}
}