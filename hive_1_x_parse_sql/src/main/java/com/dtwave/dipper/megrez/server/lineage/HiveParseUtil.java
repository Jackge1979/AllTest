package com.dtwave.dipper.megrez.server.lineage;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.MetaStoreUtils;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Schema;
import org.apache.hadoop.hive.ql.exec.FetchTask;
import org.apache.hadoop.hive.ql.parse.BaseSemanticAnalyzer;
import org.apache.hadoop.hive.ql.plan.TableDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author hulb
 * @date 2018/4/4 下午4:40
 */
public class HiveParseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiveParseUtil.class);
    /**
     * 获取schema信息
     *
     * @param sem
     * @param conf
     * @return
     */
    public static Schema getSchema(BaseSemanticAnalyzer sem, HiveConf conf) {
        Schema schema = null;

        // If we have a plan, prefer its logical result schema if it's
        // available; otherwise, try digging out a fetch task; failing that,
        // give up.
        if (sem == null) {
            // can't get any info without a plan
        } else if (sem.getResultSchema() != null) {
            List<FieldSchema> lst = sem.getResultSchema();
            schema = new Schema(lst, null);
        } else if (sem.getFetchTask() != null) {
            FetchTask ft = sem.getFetchTask();
            TableDesc td = ft.getTblDesc();
            // partitioned tables don't have tableDesc set on the FetchTask. Instead
            // they have a list of PartitionDesc objects, each with a table desc.
            // Let's
            // try to fetch the desc for the first partition and use it's
            // deserializer.
            if (td == null && ft.getWork() != null && ft.getWork().getPartDesc() != null) {
                if (ft.getWork().getPartDesc().size() > 0) {
                    td = ft.getWork().getPartDesc().get(0).getTableDesc();
                }
            }

            if (td == null) {
                LOGGER.info("No returning schema.");
            } else {
                String tableName = "result";
                List<FieldSchema> lst = null;
                try {
                    lst = MetaStoreUtils.getFieldsFromDeserializer(tableName, td.getDeserializer(conf));
                } catch (Exception e) {
                    LOGGER.warn("Error getting schema: "+e);
                }
                if (lst != null) {
                    schema = new Schema(lst, null);
                }
            }
        }
        if (schema == null) {
            schema = new Schema();
        }
        LOGGER.info("Returning Hive schema: " + schema);
        return schema;
    }
}
