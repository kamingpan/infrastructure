package com.kamingpan.infrastructure.mini.program.security.matcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 跳过或加入路径请求匹配器
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
public class SkipJoinPathRequestMatcher implements RequestMatcher {

    /**
     * 不进入过滤器的路径
     */
    private OrRequestMatcher skipMatcher;

    /**
     * 匹配进入过滤器的路径
     */
    private RequestMatcher joinMatcher;

    public SkipJoinPathRequestMatcher(@NotEmpty List<String> skipPaths, String joinPath) {
        List<RequestMatcher> skipPathMatcher = skipPaths.stream()
                .map(AntPathRequestMatcher::new).collect(Collectors.toList());

        this.skipMatcher = new OrRequestMatcher(skipPathMatcher);
        this.joinMatcher = new AntPathRequestMatcher(joinPath);
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        if (null == this.skipMatcher || null == this.joinMatcher) {
            return false;
        }

        return !this.skipMatcher.matches(httpServletRequest) && this.joinMatcher.matches(httpServletRequest);
    }

}
