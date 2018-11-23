package com.lcc.ldap;


import org.junit.Test;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.Arrays;

/**
 * Created by lcc on 2018/11/2.
 */
public class LdapBaseTest {
    @Test
    public void connetLDAP() throws Exception {

        System.out.println(LdapBase.connetLDAP());
    }


    @Test
    public void search1() throws Exception {
        String searchBase = "ou=people,dc=ymm56,dc=com";
        // 设置过滤条件
        String uid = "admin_lcc";
        String filter = "(&(objectClass=top)(objectClass=posixAccount)(uid=" + uid + "))";

        LdapBase ldapBase = new LdapBase();

        ldapBase.Search(searchBase,uid,filter);

    }

    @Test
    public void add() throws Exception {
        LdapBase ldapBase = new LdapBase();


        Attributes attrs = new BasicAttributes(true);
        Attribute objclass = new BasicAttribute("objectclass");
        // 添加ObjectClass
        String[] attrObjectClassPerson = { "inetOrgPerson", "organizationalPerson", "person", "top" };
        Arrays.sort(attrObjectClassPerson);
        for (String ocp : attrObjectClassPerson) {
            objclass.add(ocp);
        }
        attrs.put(objclass);
        String uid = "zhangsan";

        String userDN = "uid=" + uid + "," + "ou=people,dc=ymm56,dc=com";

        // 密码处理
        // attrs.put("uid", uid);
        attrs.put("cn", uid);
        attrs.put("sn", uid);
        attrs.put("mail", "abc@163.com");
        attrs.put("description", "张三");
        attrs.put("userPassword", "Password".getBytes("UTF-8"));

        ldapBase.add(userDN,attrs);
    }


    @Test
    public void update1() throws Exception {
        LdapBase ldapBase = new LdapBase();

        String uid = "zhangsan";
        String userDN = "uid=" + uid + "," + "ou=people,dc=ymm56,dc=com";
        Attributes attrs = new BasicAttributes(true);
        attrs.put("mail", "zhangsan@163.com");

        ldapBase.modify(userDN,attrs);
    }



    @Test
    public void del1() throws Exception {
        LdapBase ldapBase = new LdapBase();

        String uid = "zhangsan";
        String userDN = "uid=" + uid + "," + "ou=people,dc=ymm56,dc=com";

        ldapBase.del(userDN);
    }





}