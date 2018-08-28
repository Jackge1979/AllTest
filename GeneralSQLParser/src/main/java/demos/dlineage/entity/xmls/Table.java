package demos.dlineage.entity.xmls;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
@XStreamAlias("table")
public class Table {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String type;
    @XStreamAsAttribute
    private String coordinate;

    @XStreamImplicit(itemFieldName = "column")
    private List<Column> column;
}
