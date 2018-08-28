package demos.dlineage.entity.xmls;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
@XStreamAlias("DlineageEntity")//对应dlineage元素
public class DlineageEntity {

    // 对应relation
    @XStreamImplicit(itemFieldName = "relation")
    private List<Relation> relation;

    // 对应table
    @XStreamImplicit(itemFieldName = "table")
    private List<Table> table;

}
