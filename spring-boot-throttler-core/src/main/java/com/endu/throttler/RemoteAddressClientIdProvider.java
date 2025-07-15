package com.endu.throttler;

import jakarta.servlet.http.HttpServletRequest;

public class RemoteAddressClientIdProvider implements ClientIdProvider {

    @Override
    public String getClientId(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

}
