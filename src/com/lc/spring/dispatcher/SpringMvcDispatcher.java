package com.lc.spring.dispatcher;

import com.lc.spring.annotion.Autowired;
import com.lc.spring.annotion.Controller;
import com.lc.spring.annotion.RequestMapping;
import com.lc.spring.annotion.Service;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchengli on 2016/10/29.
 */
@WebService
public class SpringMvcDispatcher extends HttpServlet {
    private List<String> basePackageList = new ArrayList<>();
    private Map<String, Object> instanceMap = new HashMap<>();
    private Map<String, Object> handerMap = new HashMap<>();


    /**
     * 初始化方法
     * @throws ServletException
     */
    public void init() throws ServletException {
        try {
            scanPackage("com.lc");
            instance();
            springIoc();
            handMap();


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 扫包
     *
     * @param basePackagename
     * @throws IOException
     */

    private void scanPackage(String basePackagename) throws IOException {

        URL url = this.getClass().getClassLoader().getResource("/" + replacePath(basePackagename));
        String fileName = url.getFile();
        String[] files = new File(fileName).list();
        for (String filePath : files) {

            File file = new File(fileName + filePath);
            if (file.isDirectory()) {
                scanPackage(basePackagename + "." + file.getName());
            } else {
                System.out.println("packageName is " + basePackagename + "." + file.getName());
                basePackageList.add(basePackagename + "." + file.getName());
            }
        }


    }


    /**
     * 初始化所有基础包的类
     */
    private void instance() {

        basePackageList.stream().filter(e -> e != null)
                .map(e -> {
                    try {
                        return Class.forName(e.replace(".class", ""));
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                }).filter(o ->
                        o.isAnnotationPresent(Controller.class) || o.isAnnotationPresent(Service.class)
        ).forEach(m -> {
            if (m.isAnnotationPresent(Controller.class)) {
                Controller controller = m.getAnnotation(Controller.class);
                try {
                    instanceMap.put(controller.value(), m.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (m.isAnnotationPresent(Service.class)) {
                Service service = m.getAnnotation(Service.class);
                try {
                    instanceMap.put(service.value(), m.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * IOC反转注入所有可调用实例
     *
     * @throws IllegalAccessException
     */
    private void springIoc() throws IllegalAccessException {
        if (instanceMap.size() == 0)
            return;
        for (Map.Entry<String, Object> entity : instanceMap.entrySet()) {

            Field[] fields = entity.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredName = autowired.value();
                    field.setAccessible(true);
                    field.set(entity.getValue(), instanceMap.get(autowiredName));
                }
            }
        }

    }



    /**
     * 扫描所有instance组装所有的mapping
     */
    public void handMap() {

        if (instanceMap == null || instanceMap.size() == 0)
            return;
        //            Class clazz =
        instanceMap.entrySet().stream().filter(entry -> entry.getValue().getClass().getAnnotation(Controller.class) != null).forEach(entry -> {
            Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
            String ctValue = controller.value();
            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String requestMappingV = requestMapping.value();
                    handerMap.put("/" + ctValue + "/" + requestMappingV, method);
                } else {
                    continue;
                }
            }
        });

    }

    private String replacePath(String path) {

        return path.replaceAll("\\.", "/");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String uri = req.getRequestURI();
            String url = req.getRequestURI();
            System.out.println(uri);
            String context = req.getContextPath();
            String path = url.replace(context, "");
            Method method = (Method) handerMap.get(path);
            Object controller = instanceMap.get(path.split("/")[1]);

            method.invoke(controller, new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.doPost(req, resp);
    }


}
