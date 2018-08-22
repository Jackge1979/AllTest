package com.dtwave.dipper.dubhe.node.entity;

import com.dtwave.dipper.dubhe.common.constant.TaskType;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * Master下发给Node的任务信息
 * <p>
 * Created by baisong on 17/8/9.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {

    //任务所属租户ID
    private Long tenantId;

    private Integer wsId;

    //任务所属用户ID
    private Long modifyUserId;

    //任务ID,由instanceId和scheduleId组成
    private String taskId;

    /** 真实的任务ID*/
    private Long realTaskId;

    //任务名
    private String taskName;

    //修改者
    private String modifyUserName;

    //下载的最大数目
    private Integer downloadSize;

    //类型
    private TaskType taskType;

    //源码, 经过Base64编码
    private String source;

    //运行参数, 经过Base64编码
    private String parameter;

    //对Hive, 表示数据库名
    private String schemaName;

    //是否空跑
    private boolean isEmptyRun;

    //Yarn地址, 暂时可选
    private String yarnAddress;

    //prestoDB的地址
    private String prestoServer;

    /**文件系统的FSKey*/
    private FSKey fsKey;

    //队列名
    private String yarnQueue = "default";

    /**
     * 资源信息, 可选. resourceName->resourcePath
     * resourceName写在代码里,根据resourcePath去文件服务器中间件下载
     */
    private Map<String, String> resourceMap;

    /**
     * 系统参数, 经过Base64编码
     * 例如 executor-memory、driver-memory、driver-cores、num-executors
     */
    private String systemParameter;
    /**
     * 限制访问库空间 0:不限制1:限制
     */
    private Integer limitSchema;

    /**
     * 是否开启权限验证 true:验证权限 false:不验证权限
     */
    private boolean isAuthority;

    /**
     * 以下三个参数临时用于支持 Mysql、GreenPlum计算引擎
     */
    private  String address;

    private String  userName;

    private String  password;
    /**
     * 对hive高可用模式，后面添加的参数
     */
    private String engineParam;

    /**
     * hive是否高可用
     */
    private boolean isAddressHa;
}
