package com.funWithStocks.FunWithStocks.component;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
@WebFilter(urlPatterns = {"/api/v1/submit/*"}) // Specify the common URL pattern for the filter// Order of the filter
public class TimeLimitFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(TimeLimitFilter.class);
    // Map to store time limits for different endpoints
    private static final Map<String, TimeRange> TIME_RANGES = new HashMap<>();
    private static final String PATH_VARIABLE_PLACEHOLDER = "{username}";

    // Static initialization block to define time ranges for different endpoints
    static {
        // Define time ranges for different endpoints
        TIME_RANGES.put(createMapKey("/api/v1/submit/slot1/{username}"), new TimeRange(LocalTime.of(15, 55), LocalTime.of(17, 30)));
        TIME_RANGES.put(createMapKey("/api/v1/submit/slot2/{username}"), new TimeRange(LocalTime.of(16, 40), LocalTime.of(17, 30)));
        // Add more mappings as needed
    }
    private static String createMapKey(String endpoint) {
        return endpoint.replaceAll("\\{[^}]*\\}", PATH_VARIABLE_PLACEHOLDER);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //In case you want to do by matcher use matcher
        //RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        String path = request.getServletPath();
        return !path.startsWith("/api/v1/submit/");
        //return matcher.matches(request);
    }
    private TimeRange getTimeRange(String requestURI) {
        for (Map.Entry<String, TimeRange> entry : TIME_RANGES.entrySet()) {
            String key = entry.getKey();
            if (requestURI.matches(key.replace("{username}", ".*"))) {
                return entry.getValue();
            }
        }
        return null;
    }
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        // Get the requested URL
        //String requestURI = servletRequest.get();
        // Get the requested URL
        logger.info("Inside doFilterInternal of TimeLimitFilter class");
        String requestURI = request.getRequestURI();
        //
        logger.info("Request URL : {}",requestURI);

        // If a match is found, get the corresponding time range
        TimeRange timeRange = getTimeRange(requestURI);
        logger.info("TimeRange is {} and {}", timeRange.startTime, timeRange.endTime);
        if (timeRange != null) {
            // Get the current time
            LocalTime currentTime = LocalTime.now();
            // Check if the current time falls within the time range
            if (currentTime.isAfter(timeRange.startTime) && currentTime.isBefore(timeRange.endTime)) {
                // If not, reject the request
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access to this functionality is restricted between "
                        + timeRange.startTime.toString() + " and " + timeRange.endTime.toString());
                logger.info("Access to this functionality is restricted between {} and {}"
                        ,timeRange.startTime, timeRange.endTime);
                return;
            }
        }
        logger.info("In else, running the filter chain..");
        // If the URL is not found in the map or if the current time falls within the time range, proceed with the request
        filterChain.doFilter(request, response);
    }
    // Inner class to represent a time range
    private static class TimeRange {
        private final LocalTime startTime;
        private final LocalTime endTime;

        public TimeRange(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}