import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * <p>
 *
 * CGLib中提供的FastClass增强功能，
 * FastClass顾名思义是一个能让被增强类更快调用的Class，
 * 主要针对调用方法是变量的场景，用于替代反射调用
 *
 * </p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 9:43 2021/3/16
 */
public class FastClassTest {

    static class Student{
        public String hello(String name){
           return  "hello,"+name;
        }
    }

    public static void main(String[] args) throws Exception{
        Student student = new Student();
        Class<?> sClass = student.getClass();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        /**1、直接调用*/
        System.out.println(student.hello("直接调用"));
        stopWatch.stop();
        System.out.println("耗时："+stopWatch.getTotalTimeNanos()+"纳秒");

        /**2、使用jdk反射调用*/
        stopWatch.start();
        Method reflectMethod = sClass.getMethod("hello", String.class);
        Class<?>[] parameterTypes = reflectMethod.getParameterTypes();
        System.out.println(reflectMethod.invoke(student, "反射调用"));
        stopWatch.stop();
        System.out.println("耗时："+stopWatch.getTotalTimeNanos()+"纳秒");

        stopWatch.start();
        /**3、使用cglib增强类调用*/
        FastClass fastClass = FastClass.create(sClass);
        FastMethod fastMethod = fastClass.getMethod("hello", parameterTypes);
        System.out.println(fastMethod.invoke(student,new String[]{"增强调用"}));
        stopWatch.stop();
        System.out.println("耗时："+stopWatch.getTotalTimeNanos()+"纳秒");
    }
}


