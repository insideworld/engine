package insideworld.engine.quarkus.extension;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class EntityRecorder {

    public RuntimeValue<?> createEntity() {
        return null;
//        final ClassCreator build = ClassCreator.builder().superClass(AbstractEntity.class).interfaces(Entity.class)
//            .className("OneMore").classOutput(new ClassOutput() {
//                @Override
//                public void write(String s, byte[] bytes) {
//                    final String s1 = new String(bytes);
//                    System.out.println(s);
//
//                }
//            }).build();
//        BeanGenerator beanGenerator = new BeanGenerator();

//        beanGenerator.addProperty("message", String.class);

//        beanGenerator.setSuperclass(AbstractEntity.);
//        Object myBean = beanGenerator.create();
//        Method setter = myBean.getClass().getMethod("setName", String.class);
//        setter.invoke(myBean, "some string value set by a cglib");
//
//        Method getter = myBean.getClass().getMethod("getName");
//        assertEquals("some string value set by a cglib", getter.invoke(myBean));

    }


}
