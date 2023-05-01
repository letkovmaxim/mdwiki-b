package org.sbtitcourses.mdwiki.security;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * Фильтр, отвечающий за проверку аутентифицированного пользователя.
 */
public class AuthFilter implements Filter {

    /**
     * Метод проверяет аутентифицирован ли пользователь.
     * Если пользователь прошел аутентификацию, то в контексте
     * находится объект типа {@link PersonDetails},
     * если нет - строка "Anonymous user".
     * В последнем случае фильтр отправляет сообщение об ошибке.
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof PersonDetails) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(SC_FORBIDDEN);
        }
    }
}