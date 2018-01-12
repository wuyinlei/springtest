# Intellij IDEA 搭建Spring Boot项目

标签（空格分隔）： SpringBoot JAVA后台

---

### 第一步
选择File --> New --> Project -->Spring Initialer  --> **点击Next**
![image.png](http://upload-images.jianshu.io/upload_images/1316820-a38a44b5f4f34cd7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第二步
自己修改 **Group** 和 **Artifact** 字段名 -->**点击next**
![image.png](http://upload-images.jianshu.io/upload_images/1316820-669c90a9c4879e2b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第三步
![image.png](http://upload-images.jianshu.io/upload_images/1316820-e59a6ea64e4036ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第四步
**点击finish**
![image.png](http://upload-images.jianshu.io/upload_images/1316820-d0cc53f00ad4c51c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第五步
等待编译完成(这个貌似翻墙会好点,要不然下载依赖特别慢。。。)

### 第六步
![image.png](http://upload-images.jianshu.io/upload_images/1316820-39a2aa2a2707f2a3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第七步(最终目录结构)
![image.png](http://upload-images.jianshu.io/upload_images/1316820-f75cf5cd6cea739c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第八步
显示hello world
我们需要编辑一个HelloController类用于转发链接
```
@RestController
@EnableAutoConfiguration
public class HelloController {
    
    @RequestMapping("/hello")
    private String index(){
        return "Hello World!";
    }
}
```

![image.png](http://upload-images.jianshu.io/upload_images/1316820-8d7e499f808284be.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 第九步
启动项目

* 方式一
    * ![image.png](http://upload-images.jianshu.io/upload_images/1316820-e2197abae79b0a4b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
* 方式二
    * ![image.png](http://upload-images.jianshu.io/upload_images/1316820-b8ff62e991364021.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* 启动之后可以看控制台显示的是如下:
![image.png](http://upload-images.jianshu.io/upload_images/1316820-51df6da870643e7e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

    
* 见证奇迹的时刻
    * 我们在游览器中输入如下的http://localhost:8080/hello  -->显示的如下界面代表我们项目成功了
    * ![image.png](http://upload-images.jianshu.io/upload_images/1316820-bf8f049a903d287a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 写在最后
第一步的那个next,如果感觉编译慢的话,或有有可能会加载失败,这个时候可以
![image.png](http://upload-images.jianshu.io/upload_images/1316820-10b71e8e5cdfffc1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后找到这个文件并解压
![image.png](http://upload-images.jianshu.io/upload_images/1316820-71a3f431e87279dd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/1316820-ec85160f54fce34c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**不过导入之后还是要下载编译的,祝成功哈**


### 测试项目地址

* https://github.com/wuyinlei/springtest
* https://github.com/wuyinlei/springtest
* https://github.com/wuyinlei/springtest





