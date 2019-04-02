package com.spring.filters;

import com.spring.db.Key.KeyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class KeyFilter extends OncePerRequestFilter {

    @Autowired
    KeyDAO keyDAO;

    /**
     * Put by WebSecurityConfig before every request. Checks if target is /location, and checks if valid API-key
     * header is provided. If not, blocks request. otherwise passes on.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String key = request.getHeader("API-Key");
        try {
            if (request.getRequestURI().equals("/location") && !keyDAO.keyExists(key)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        filterChain.doFilter(request, response);
    }
}
