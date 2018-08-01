package com.antlr.label;

import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.compiler.STParser.expr_return;



public class MyVisitor extends CalExprBaseVisitor<Integer>{

    Map<String,Integer> map=new HashMap<String,Integer>();

    @Override
    public Integer visitParens(CalExprParser.ParensContext ctx) {
        return super.visit(ctx.expr());
    }

    @Override
    public Integer visitBlank(CalExprParser.BlankContext ctx) {
        return super.visitBlank(ctx);
    }

    @Override
    public Integer visitAddSub(CalExprParser.AddSubContext ctx) {

        Integer left=visit(ctx.expr(0));        //获取左边表达式最终值
        Integer right=visit(ctx.expr(1));       //获取右边表达式最终值

        if(ctx.op.getType()==CalExprLexer.ADD) return left+right;   //如果是加法
        else return left-right;                                     //如果是减法
    }

    @Override
    public Integer visitMulDiv(CalExprParser.MulDivContext ctx) {
        Integer left=visit(ctx.expr(0));        //获取左边表达式最终值
        Integer right=visit(ctx.expr(1));       //获取右边表达式最终值

        if(ctx.op.getType()==CalExprLexer.DIV) return left/right;   //如果是除法
        else return left*right;                                     //如果是乘法
    }

    @Override
    public Integer visitId(CalExprParser.IdContext ctx) {
        String key=ctx.ID().getText();

        if(map.containsKey(key)){   //如果变量被赋值
            return map.get(key);
        }
        return 0;
    }

    @Override
    public Integer visitInt(CalExprParser.IntContext ctx) {
        return Integer.parseInt(ctx.INT().getText());
    }

    @Override
    public Integer visitPrintExpr(CalExprParser.PrintExprContext ctx) {
        Integer value=visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    @Override
    public Integer visitAssign(CalExprParser.AssignContext ctx) {

        String key=ctx.ID().getText();
        Integer value=visit(ctx.expr());
        map.put(key, value);
        return value;                   // 返回 value ：a=b=6 则 a==6
    }

}
