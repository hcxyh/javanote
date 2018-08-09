package com.xyh.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 你想获得更多jar中的注解：
 * 1.修改 一下main方法中的BasePath，也就是本地jar的位置即可。
 * 2.修改 一下main方法中的packagePath， org 或com
 * @author hcxyh  2018年8月9日
 *
 */
public class PackageInfo {
	
	/**
	 1.spring-context
		org.springframework.scheduling.annotation.Schedules
		org.springframework.scheduling.annotation.EnableScheduling
		org.springframework.scheduling.annotation.Async
		org.springframework.scheduling.annotation.Scheduled
		org.springframework.scheduling.annotation.EnableAsync
		org.springframework.context.annotation.Lazy
		org.springframework.context.annotation.ImportResource
		org.springframework.context.annotation.EnableLoadTimeWeaving
		org.springframework.context.annotation.PropertySource
		org.springframework.context.annotation.Import
		org.springframework.context.annotation.Scope
		org.springframework.context.annotation.Configuration
		org.springframework.context.annotation.Description
		org.springframework.context.annotation.Bean
		org.springframework.context.annotation.DependsOn
		org.springframework.context.annotation.Role
		org.springframework.context.annotation.PropertySources
		org.springframework.context.annotation.ComponentScans
		org.springframework.context.annotation.Profile
		org.springframework.context.annotation.Primary
		org.springframework.context.annotation.EnableAspectJAutoProxy
		org.springframework.context.annotation.Conditional
		org.springframework.context.annotation.ComponentScan
		org.springframework.context.annotation.ComponentScan$Filter
		org.springframework.context.annotation.EnableMBeanExport
		org.springframework.context.event.EventListener
		org.springframework.validation.annotation.Validated
		org.springframework.jmx.export.annotation.ManagedResource
		org.springframework.jmx.export.annotation.ManagedNotification
		org.springframework.jmx.export.annotation.ManagedOperationParameter
		org.springframework.jmx.export.annotation.ManagedOperation
		org.springframework.jmx.export.annotation.ManagedNotifications
		org.springframework.jmx.export.annotation.ManagedAttribute
		org.springframework.jmx.export.annotation.ManagedMetric
		org.springframework.jmx.export.annotation.ManagedOperationParameters
		org.springframework.cache.annotation.Caching
		org.springframework.cache.annotation.CacheEvict
		org.springframework.cache.annotation.EnableCaching
		org.springframework.cache.annotation.Cacheable
		org.springframework.cache.annotation.CachePut
		org.springframework.cache.annotation.CacheConfig
		org.springframework.format.annotation.NumberFormat
		org.springframework.format.annotation.DateTimeFormat
		org.springframework.stereotype.Controller
		org.springframework.stereotype.Repository
		org.springframework.stereotype.Service
		org.springframework.stereotype.Component




       2spring-web
		org.springframework.web.bind.annotation.CrossOrigin
		org.springframework.web.bind.annotation.RequestHeader
		org.springframework.web.bind.annotation.RequestParam
		org.springframework.web.bind.annotation.ResponseStatus
		org.springframework.web.bind.annotation.MatrixVariable
		org.springframework.web.bind.annotation.ModelAttribute
		org.springframework.web.bind.annotation.ExceptionHandler
		org.springframework.web.bind.annotation.CookieValue
		org.springframework.web.bind.annotation.PostMapping
		org.springframework.web.bind.annotation.RequestPart
		org.springframework.web.bind.annotation.RequestMapping
		org.springframework.web.bind.annotation.GetMapping
		org.springframework.web.bind.annotation.RequestBody
		org.springframework.web.bind.annotation.InitBinder
		org.springframework.web.bind.annotation.PatchMapping
		org.springframework.web.bind.annotation.Mapping
		org.springframework.web.bind.annotation.SessionAttribute
		org.springframework.web.bind.annotation.RestController
		org.springframework.web.bind.annotation.ControllerAdvice
		org.springframework.web.bind.annotation.SessionAttributes
		org.springframework.web.bind.annotation.ResponseBody
		org.springframework.web.bind.annotation.RestControllerAdvice
		org.springframework.web.bind.annotation.RequestAttribute
		org.springframework.web.bind.annotation.DeleteMapping
		org.springframework.web.bind.annotation.PutMapping
		org.springframework.web.bind.annotation.PathVariable
		org.springframework.web.context.annotation.SessionScope
		org.springframework.web.context.annotation.RequestScope
		org.springframework.web.context.annotation.ApplicationScope
	

       3spring-webmvc
		org.springframework.web.servlet.config.annotation.EnableWebMvc

	
	
	
	  /**
    * 从jar获取某包下所有类
    * 
    * @param jarPath
    *                        jar文件路径
    * @param childPackage
    *                        是否遍历子包
    * @return 类的完整名称
    */
   private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
       List<String> myClassName = new ArrayList<String>();
       String[] jarInfo = jarPath.split("!");
       String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
       String packagePath = jarInfo[1].substring(1);
       try (JarFile jarFile = new JarFile(jarFilePath);) {
           Enumeration<JarEntry> entrys = jarFile.entries();
           while (entrys.hasMoreElements()) {
               JarEntry jarEntry = entrys.nextElement();
               String entryName = jarEntry.getName();
               if (entryName.endsWith(".class")) {
                   if (childPackage) {
                       if (entryName.startsWith(packagePath)) {
                           entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                           myClassName.add(entryName);
                       }
                   } else {
                       int index = entryName.lastIndexOf("/");
                       String myPackagePath;
                       if (index != -1) {
                           myPackagePath = entryName.substring(0, index);
                       } else {
                           myPackagePath = entryName;
                       }
                       if (myPackagePath.equals(packagePath)) {
                           entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                           myClassName.add(entryName);
                       }
                   }
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }

       return myClassName;
   }

   /**
    * 从所有jar中搜索该包，并获取该包下所有类
    * 
    * @param urls
    *                        URL集合
    * @param packagePath
    *                        包路径
    * @param childPackage
    *                        是否遍历子包
    * @return 类的完整名称
    */
   private static List<String> getClassNameByJars(String[] urls, String packagePath, boolean childPackage) {
       List<String> myClassName = new ArrayList<String>();
       if (urls != null) {
           for (int i = 0; i < urls.length; i++) {
               String urlPath = urls[i];
               // 不必搜索classes文件夹
               if (urlPath.endsWith("classes/")) {
                   continue;
               }
               String jarPath = urlPath + "!" + packagePath;
               myClassName.addAll(getClassNameByJar(jarPath, childPackage));
           }
       }
       return myClassName;
   }
   public static void main(String[] args) {
       List<String> annotationsList = new ArrayList<>();
       String BasePath="D:/repository/maven/org/springframework";
       List<String> jars = new ArrayList<>();
   //    jars.add(BasePath+"/spring-context/4.3.8.RELEASE/spring-context-4.3.8.RELEASE.jar");
   //    jars.add(BasePath+"/spring-web/4.3.8.RELEASE/spring-web-4.3.8.RELEASE.jar");
       jars.add(BasePath+"/spring-webmvc/4.3.8.RELEASE/spring-webmvc-4.3.8.RELEASE.jar");
       String[] strArr = new String[jars.size()];
       jars.toArray(strArr);
       
       String packagePath=" org"; //此处有个空格

		List<String> className = getClassNameByJars(strArr, packagePath, true);

for (String cn : className) {
           try {
               Class<?> onwClass = Class.forName(cn);
               if(onwClass !=null){
                   
                   boolean annotationFlg = onwClass.isAnnotation();
                   if(annotationFlg){
                       annotationsList.add(cn);
                   }
               }
           } catch (ClassNotFoundException e) {
               continue;
           }catch(NoClassDefFoundError f){
               continue;
           }
       }
       
       for (String str : annotationsList){
           System.out.println(str);
       }
       
   }
	

}
