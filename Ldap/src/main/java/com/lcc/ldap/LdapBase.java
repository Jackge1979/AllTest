package com.lcc.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by lcc on 2018/11/2.
 */
public class LdapBase {

    public static LdapContext connetLDAP() throws NamingException {
        // 连接Ldap需要的信息
        String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
        String ldapUrl = "ldap://121.196.237.221:389";// url
        String ldapAccount = "cn=Manager,dc=ymm56,dc=com"; // 用户名
        String ldapPwd = "ldap123";//密码
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
        // LDAP server
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapAccount);
        env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
        env.put("java.naming.referral", "follow");
        LdapContext ctxTDS = new InitialLdapContext(env, null);
        return ctxTDS;
    }


    //查询
    public void Search(String searchBase,String uid,String filter) throws Exception {
        LdapContext ctx = connetLDAP();

        // 限制要查询的字段内容
        String[] attrPersonArray = { "uid", "userPassword", "displayName", "cn", "sn", "mail", "description" };
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        // 设置将被返回的Attribute
        searchControls.setReturningAttributes(attrPersonArray);
        // 三个参数分别为：
        // 上下文；
        // 要搜索的属性，如果为空或 null，则返回目标上下文中的所有对象；
        // 控制搜索的搜索控件，如果为 null，则使用默认的搜索控件
        NamingEnumeration<SearchResult> answer = ctx.search(searchBase, filter.toString(), searchControls);
        // 输出查到的数据
        while (answer.hasMore()) {
            SearchResult result = answer.next();
            NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();
            while (attrs.hasMore()) {
                Attribute attr = attrs.next();
                System.out.println(attr.getID() + "=" + attr.get());
            }
            System.out.println("============");
        }
    }


    // 添加
    public void add( String userDN, Attributes attrs) throws Exception {
        LdapContext ctx = connetLDAP();
        ctx.createSubcontext(userDN, attrs);

    }


    //修改
    public boolean modify(String userDN, Attributes attrs) throws Exception {
        boolean result = true;
        LdapContext ctx = connetLDAP();
        ctx.modifyAttributes(userDN, DirContext.REPLACE_ATTRIBUTE, attrs);
        return result;

    }


    //删除
    public void del(String userDN) throws Exception {
        LdapContext ctx = connetLDAP();
        ctx.destroySubcontext(userDN);

    }

}
