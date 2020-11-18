package com.koreait.onnet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.onnet.rest.RestMapper;
import com.koreait.onnet.rest.model.RestDMI;
import com.koreait.onnet.rest.model.RestPARAM;

public class RestInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private RestMapper mapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("rest - interceptor");
		
		String uri = request.getRequestURI();
		System.out.println("uri : " + uri);
		String[] uriArr = uri.split("/");
		
		String[] checkKeywords  = {"dle", "Del", "upd", "Upd"};
		for(String keyword: checkKeywords) {
			if(uriArr[2].contains(keyword)) {
				int i_rest = CommonUtils.getIntParameter("i_rset", request);
				if(i_rest == 0) {
					return false;
				}
				int i_user = SecurityUtils.getLoginUserPk(request);
				
				boolean result = _authSuccess(i_rest, i_user);
				System.out.println(" === auth result : " + result);
				return result;
			}
		}
		return true;
	}
	
	private boolean _authSuccess(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		
		RestDMI dbResult = mapper.selRest(param);
		if(dbResult == null || dbResult.getI_user() != i_user) {
			return false;
		}
	
		return true;

	}
}
