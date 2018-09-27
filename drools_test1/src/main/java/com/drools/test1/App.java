package com.drools.test1;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

/**
 * Created by lcc on 2018/9/27.
 */
public class App {
    public static void main(String[] args) throws Exception {

        /**
         *
         * 由于规则是通过Drools Workbench定义在远程服务器上的，因此应用程序需要配置该规则对应jar包的GAV信息，配置了LASTEST版本，
         * 就是要去获取服务器上最新 版本的jar那程序是怎么知道去Drools Workbench在哪儿呢？就是通过在pom.xml中定义的<repository>
         *
         * 这个三个参数分别是：groupId,artifactId,版本号(这里版本号是写死的，如果我们升级还要重新修改程序，建议写成LATEST，这样每次都是最新的版本，
         * 只需要修改页面规则版本就好了)
         */
        ReleaseIdImpl releaseId = new ReleaseIdImpl("com.myspace","lcc_car","1.0.0");
        KieServices ks = KieServices.Factory.get();
        KieContainer container = ks.newKieContainer(releaseId);


        /**
         * 启动一个定时器定期去Drools Workbench上检查规则包是否有最新版本更新发布，如果有，则该定时器会负责将最新规则包拉取到本地，
         * 并使最新规则生效（后台线程运行）
         */
        KieScanner scanner = ks.newKieScanner(container);
        scanner.start(1000);


        /**
         * 执行规则，按照规则定义，18岁以下禁止申请，规则中会把valid设置成false,通过这个代码块，将规则执行后的结果打印出来
         * 申请人：张三，年龄：17是否可以申请驾照false
         * 通过执行结果可以看到，规则被执行了，申请人不满足申请条件，被拒绝了
         */
        StatelessKieSession session = container.newStatelessKieSession();
        for(int i = 0;i < 100;i++) {
            FactType factType = factType(container.getKieBase());
            Object applicant = makeApplicant(factType);
            session.execute(applicant);
            System.out.println("申请人："+factType.get(applicant, "name")+"，年龄："+factType.get(applicant, "age")+"是否可以申请驾照"+factType.get(applicant, "valid"));
            Thread.sleep(20000);//休眠20秒，等待更新规则查看输出结果
        }
    }


    /**
     * 通过类似反射机制，对Applicant对象的各个属性进行赋值代码示例中，传递的是一个年龄是17，valid=true的申请人信息
     *
     * @param factType
     * @return
     * @throws Exception
     */
    private static Object makeApplicant(FactType factType) throws Exception{
        Object applicant = factType.newInstance();
        factType.set(applicant, "name", "张三");
        factType.set(applicant, "age", 17);
        return applicant;
    }

    /**
     * package com.myspace.lcc_car;
     *
     * declare Applicant
     *      name:String
     *      age:int
     *      valid:boolean
     * end
     *
     *
     * 在这个例子中，规则定义是接收Applicant类型的参数的，而Eclipse工程中并没有这个类的定义，这个时候可以通过类似反射的机制，
     * 来根据在Drools Workbench中定义的Applicant的包名和名称将其对应的类型对象生成出来
     *
     * @param base
     * @return
     */
    protected static FactType factType(KieBase base) {
        // 这里是页面上定义的实体类，包名，实体名称
        FactType factType = base.getFactType("com.myspace.lcc_car", "Applicant");
        return factType;
    }
}

