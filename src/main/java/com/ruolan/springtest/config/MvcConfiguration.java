package com.ruolan.springtest.config;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.ruolan.springtest.interceptor.LoginInterceptor;
import com.ruolan.springtest.interceptor.PermissionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.rmi.ServerException;


@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{

    //spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/downloads/work/image/upload/");
    }

    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置spring容器
        viewResolver.setApplicationContext(applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置解析的后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }


    /**
     * 文件上传解析器
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(20971520); //20MB
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //
//    kaptcha.border = no
    @Value("${kaptcha.border}")
    private String border;

    //    kaptcha.textproducer.font.color = red
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;
    //    kaptcha.image.width = 135
    @Value("${kaptcha.image.width}")
    private String width;
    //    kaptcha.textproducer.char.string = ACDEFHKPRSTWX345679
    @Value("${kaptcha.textproducer.char.string}")
    private String cString;
    //    kaptcha.image.height = 50
    @Value("${kaptcha.image.height}")
    private String height;
    //    kaptcha.textproducer.font.size = 43
    @Value("${kaptcha.textproducer.font.size}")
    private String size;
    //    kaptcha.noise.color = black
    @Value("${kaptcha.noise.color}")
    private String nColor;
    //    kaptcha.textproducer.char.length = 4
    @Value("${kaptcha.textproducer.char.length}")
    private String clength;
    //    kaptcha.textproducer.font.names = Arial
    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    /**
     * 由于web.xml 不生效了  需要在这里配置Kaptcha验证码的Servlet
     *
     * @return
     * @throws ServerException
     */
    @Bean
    public ServletRegistrationBean createServletRegisterationBean() throws ServerException{
        ServletRegistrationBean kaptchaServlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        kaptchaServlet.addInitParameter("kaptcha.border",border); //无边框
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);//字体颜色
        kaptchaServlet.addInitParameter("kaptcha.image.width",width);//图片宽度
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.string",cString);//使用哪些字符
        kaptchaServlet.addInitParameter("kaptcha.image.height",height);//图片高度
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.size",size);//字体大小
        kaptchaServlet.addInitParameter("kaptcha.noise.color",nColor);//干扰线的颜色
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.length",clength); //字符个数
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.names",fnames);//字体
        return kaptchaServlet;
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
        String interceptPaht = "/shopadmin/**";  //表示请求路径上只要包含这个shopadmin就需要拦截
        //注册拦截器
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new LoginInterceptor());
        //配置拦截器的路径
        loginInterceptor.addPathPatterns(interceptPaht);
        //还可以注册其他的拦截器
        InterceptorRegistration permissionInterceptor = registry.addInterceptor(new PermissionInterceptor());
        //配置拦截器的路径
        permissionInterceptor.addPathPatterns(interceptPaht);


        //配置不拦截的路径
        /**shopregister page**/
        permissionInterceptor.excludePathPatterns("/shopadmin/shoplist");
        permissionInterceptor.excludePathPatterns("shopadmin/getshoplist");

    }
}
