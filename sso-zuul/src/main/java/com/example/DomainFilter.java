package com.example;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;

public class DomainFilter extends ZuulFilter {

	public static final String VALID_DOMAIN = "insnergy.com";

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
//		HttpServletRequest request = ctx.getRequest();
//		Object accessToken = request.getParameter("accessToken");

		try {
			JSONObject authMain = new JSONObject(SecurityContextHolder.getContext().getAuthentication());
			JSONObject userAuthentication = authMain.getJSONObject("userAuthentication");
			JSONObject details = userAuthentication.getJSONObject("details");
			String hostDomain = details.getString("hd");
			if (!VALID_DOMAIN.equalsIgnoreCase(hostDomain)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseBody("domain of email is invalid");
				ctx.setResponseStatusCode(401);
				System.out.println("hostDomain error " + hostDomain);
				return null;
			}
		} catch (JSONException ex) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseBody("domain of email is invalid");
				ctx.setResponseStatusCode(401);
				return null;
		}

		System.out.println("ok");
		return null;
	}
}
