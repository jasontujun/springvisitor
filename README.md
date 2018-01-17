# Spring Visitor

SpringVisitor是一个轻量级的Spring访问层框架。

它提供了从Controller层到Service层的自动调用：比如url模式为/xxxx/{service}/{method}，则只需要编写一个通用controller便可以将该url下的http请求路由到对应service的对应method中，不再因为service层的扩展而编写更多模板化的controller类。


## 特性
  - Controller层到Service层的自动关联
  - 方法级别的缓存，应该重复请求的场景
  - 统一的HttpResponse处理和异常处理
  - 更加简洁的Interceptor编写


## 导入

在你的spring项目的pom文件中，添加如下Maven依赖:

```xml
<dependency>
  <groupId>com.yunchang</groupId>
  <artifactId>springvisitor-core</artifactId>
  <version>1.0</version>
</dependency>
```


## 如何使用

1. 创建一个自定义的Controller，并继承AReflectiveController。添加一个自定义方法，url模式中包含service和method，如下：

```java
@Controller
public class XXXController extends AReflectiveController {

    @RequestMapping("/xxx/{service}/{method}")
    @ResponseBody
    public Object doRequest(@PathVariable final String service, @PathVariable final String method,
                            final HttpServletRequest request, final HttpServletResponse response) {
        return invokeService(request, response, service, method);
    }
}
```

2. 然后在自己的业务逻辑service类，添加@ReflectiveService注解；在对应的方法中，添加@ReflectiveMethod注解。表明该Service的那些方法可以自动反射调用。

```java
@ReflectiveService("YourServiceName")
@Service
public class XXXService {

    @ReflectiveMethod
    public String xxxMethod(Map<String, String> params) {
        ...
        return "";
    }

     @ReflectiveMethod(params = {"aa", "bb"})
    public String xxxMethod2(String aa, String bb) {
        ...
        return "";
    }
}
```

3. 自定义统一的HttpResponse处理器和异常处理器

```java
@Component
public class MyResponseHandler implements IResponseHandler {
    @Override
    public Object handleResponse(HttpServletRequest request, HttpServletResponse response,
                                 String service, String method, Map<String, String> paramMap,
                                 long costTime, Object result) {
        // 比如统一返回内容为json格式，成功status为ok，失败status为fail
        return result;
    }
}
```

```java
@Component
public class MyExceptionHandler implements IExceptionHandler {
    @Override
    public Object handleError(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> paramMap, Exception exception) {
        // 比如统一返回内容为json格式，status为fail
    }
}
```

4. 在项目的spring配置文件xml中，添加如下配置:

```xml
<!--使用默认的service路由器，可以替换成自己的实现ServiceInvoker-->
<bean class="com.yunchang.spring.visitor.core.invoker.DefaultServiceInvoker"/>

<!--使用默认的Map缓存，可以替换成自己的实现-->
<bean class="com.yunchang.spring.visitor.core.cache.MapServiceCache"/>

<!--使用自定义的HttpResponse处理器，处理返回内容-->
<bean class="com.xxxx.xxxxx.MyResponseHandler"/>

<!--使用自定义的异常处理器，处理返回内容-->
<bean class="com.xxxx.xxxxx.MyExceptionHandler"/>
```

5. 测试一下，在浏览器输入http://127.0.0.1:8080/xxx/YourServiceName/xxxMethod?aa=123&bb=789，该请求即可路由到XXXService的xxxMethod方法了。


## 示例

可以运行项目中的demo项目，对Spring Visitor进行调试。

demo中实现了Json格式的HttpResponse处理器和异常处理器，实现了常用的签名验证和白名单验证的Interceptor。

你可以在自己项目中实现自定义的ServiceInvoker和自定义的缓存ServiceCache，并在spring配置文件中指定成自己的实现。


## 开源许可证

Apache v2 License


## 作者

jasontujun