package com.appleyk.rpc.client.sanner;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Set;

/**
 * <p>自定义rpc service bean definition 扫描器（主要放开接口不能被作为bd的条件）</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @date created on  下午7:57 2021/5/19
 */
public class RpcServiceBeanPathScanner extends ClassPathBeanDefinitionScanner {

    public RpcServiceBeanPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public void addIncludeFilter(TypeFilter includeFilter) {
        super.addIncludeFilter(includeFilter);
    }

    /**
     * 重写父类方法，让接口类型的bean通过候选组件"扫描"
     */
    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        boolean bInterface = metadataReader.getClassMetadata().isInterface();
        System.out.printf("{%s}是否是接口：%b\n",metadataReader.getClassMetadata().getClassName(),bInterface);
        return bInterface;
    }

    /**
     * 重写父类方法，让接口类型的bean通过候选组件"扫描"
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean bInterface = beanDefinition.getMetadata().isInterface();
        System.out.printf("{%s}是否是接口：%b\n",beanDefinition.getMetadata().getClassName(),bInterface);
        return bInterface;
    }
}
