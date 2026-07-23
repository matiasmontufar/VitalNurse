package com.proyecto.vitalnurse.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingFilter implements Filter {

    private final Map<String, Window> counters = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 120;
    private static final long WINDOW_MS = 60_000;

    private static final String[] STATIC_PREFIXES = {"/css/", "/js/", "/img/", "/vendor/", "/login"};

    private static class Window {
        final AtomicInteger count = new AtomicInteger(0);
        final long startTime = System.currentTimeMillis();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        String path = httpReq.getRequestURI();

        for (String prefix : STATIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpServletResponse httpRes = (HttpServletResponse) response;
        String ip = httpReq.getRemoteAddr();
        Window window = counters.computeIfAbsent(ip, k -> new Window());

        synchronized (window) {
            long now = System.currentTimeMillis();
            if (now - window.startTime > WINDOW_MS) {
                counters.put(ip, new Window());
                chain.doFilter(request, response);
                return;
            }
            if (window.count.incrementAndGet() > MAX_REQUESTS) {
                httpRes.setStatus(429);
                httpRes.setContentType("application/json; charset=UTF-8");
                httpRes.getWriter().write("{\"error\":\"Demasiadas solicitudes. Intente de nuevo en un minuto.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
