package com.wiproevents;

import com.wiproevents.utils.Helper;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.Map;
import java.util.UUID;

/**
 * The main application.
 */
@SpringBootApplication(exclude = SocialWebAutoConfiguration.class)
@ComponentScan(basePackages = {"com.wiproevents"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.wiproevents.dbtool.*")
})
public class Application {
    /**
     * The request id listener.
     */
    public class RequestIdListener implements ServletRequestListener {
        /**
         * Handle request initialized event.
         *
         * @param event the servlet request event.
         */
        public void requestInitialized(ServletRequestEvent event) {
            ThreadContext.put("RequestId", UUID.randomUUID().toString());
        }

        /**
         * Handle request destroyed event.
         *
         * @param event the servlet request event.
         */
        public void requestDestroyed(ServletRequestEvent event) {
            ThreadContext.clearAll();
        }
    }

    /**
     * Create request id listener bean.
     *
     * @return the request id listener bean.
     */
    @Bean
    public RequestIdListener executorListener() {
        return new RequestIdListener();
    }

    /**
     * Custom dispatcher servlet bean.
     *
     * @return the dispatcher servlet bean.
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        // handle 404 error
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

    /**
     * The error attributes.
     *
     * @return the custom error attributes.
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            /**
             * Format error attributes with code and message key only.
             * @param requestAttributes the request attributes.
             * @param includeStackTrace the include stack trace flag.
             * @return the error attributes with code and message key only.
             */
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean
                    includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                if (!errorAttributes.containsKey("code") || !errorAttributes.containsKey("message")
                        || errorAttributes.size() != 2) {
                    Throwable error = getError(requestAttributes);
                    Object status = errorAttributes.getOrDefault("status",
                            HttpStatus.INTERNAL_SERVER_ERROR.value());
                    Object message = errorAttributes.getOrDefault("message",
                            error != null && !Helper.isNullOrEmpty(error.getMessage()) ? error.getMessage()
                                    : "Unexpected error");
                    errorAttributes.clear();
                    errorAttributes.put("code", status);
                    errorAttributes.put("message", message);
                }
                return errorAttributes;
            }
        };
    }

    @Bean(name = "bodyTemplateEngine")
    public TemplateEngine bodyTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver("body", TemplateMode.HTML));
        return templateEngine;
    }

    @Bean(name = "subjectTemplateEngine")
    public TemplateEngine subjectTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver("subject", TemplateMode.TEXT));
        return templateEngine;
    }

    private ITemplateResolver templateResolver(String file, TemplateMode mode) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(file + ".vm");
        templateResolver.setTemplateMode(mode);
        templateResolver.setCharacterEncoding("UTF8");
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);
        return templateResolver;
    }




    /**
     * The main entry point of the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // change to use Spanish Locale
        // see http://www.oracle.com/technetwork/java/javase/java8locales-2095355.html
        // Locale.setDefault(new Locale("es"));

        // System.setProperty("socksProxyHost", "127.0.0.1");
        // System.setProperty("socksProxyPort", "8921");

        SpringApplication.run(Application.class, args);
    }
}
