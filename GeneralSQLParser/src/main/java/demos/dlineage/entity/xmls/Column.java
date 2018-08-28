package demos.dlineage.entity.xmls;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
@XStreamAlias("column")
public class Column {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String coordinate;


}
