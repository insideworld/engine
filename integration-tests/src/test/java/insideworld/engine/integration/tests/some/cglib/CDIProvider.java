//package insideworld.engine.integration.tests.some.cglib;
//
//import insideworld.engine.database.AbstractCrudStorage;
//import insideworld.engine.entities.storages.Storage;
//import io.quarkus.deployment.annotations.BuildStep;
//import io.quarkus.runtime.Startup;
//import io.quarkus.runtime.StartupEvent;
//import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.event.Observes;
//import javax.enterprise.inject.spi.AfterBeanDiscovery;
//import javax.enterprise.inject.spi.Bean;
//import javax.enterprise.inject.spi.BeanManager;
//import javax.enterprise.inject.spi.CDI;
//import javax.enterprise.inject.spi.Producer;
//import javax.enterprise.inject.spi.ProducerFactory;
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//
//@Startup(1000)
//@ApplicationScoped
//public class CDIProvider {
//
//    void onStart(@Observes StartupEvent event) {
//        // place the logic here
//
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(AbstractCrudStorage.class);
//        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
//            if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
//                return "Hello Tom!";
//            } else {
//                return proxy.invokeSuper(obj, args);
//            }
//        });
//        final Object o1 = enhancer.create();
//        final Object o = CDI.current().select(o1.getClass()).get();
//
////        o.forEntity();
//        System.out.println(o);
//
//    }
////
////    @BuildStep
////    SyntheticBeanBuildItem syntheticBean() {
////        return SyntheticBeanBuildItem.configure(String.class)
////            .qualifiers(new MyQualifierLiteral())
////            .creator(mc -> mc.returnValue(mc.load("foo")))
////            .done();
////    }
//
//    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
//        System.out.println("qwe");
//    }
//
//}
