package demos.dlineage.entity.xmls;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
@XStreamAlias("target") // 对应 target
public class Target {

    // 对应 coordinate 属性  对应在sql的第几行第几个字符到第几个字符
    @XStreamAsAttribute
    private String coordinate;
    // 对应 column 属性
    @XStreamAsAttribute
    private String column;
    // 对应 id 属性
    @XStreamAsAttribute
    private String id;
    // 对应 parentId 属性
    @XStreamAsAttribute
    private String parentId;
    // 对应 parentName 属性
    @XStreamAsAttribute
    private String parentName;

}
