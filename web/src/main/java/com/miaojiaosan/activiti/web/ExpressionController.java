package com.miaojiaosan.activiti.web;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 李宇飞
 * create by 2020-07-01 17:10
 */
@RestController
@RequestMapping("/express")
public class ExpressionController {

	@GetMapping()
	private Object eval(String express) {
		ExpressionParser parser = new SpelExpressionParser();
		return "";
	}

	public static void main(String[] args) throws NoSuchMethodException {
		ExpressionFactory factory = new ExpressionFactoryImpl();

// package de.odysseus.el.util provides a ready-to-use subclass of ELContext
		SimpleContext context = new SimpleContext();

// map function math:max(int, int) to java.lang.Math.max(int, int)
		context.setFunction("math", "max", Math.class.getMethod("max", int.class, int.class));

// map variable foo to 0
		context.setVariable("foo", factory.createValueExpression(0, int.class));


// parse our expression
		ValueExpression e = factory.createValueExpression(context, "${math:max(foo,bar)}", int.class);
// set value for top-level property "bar" to 1
		factory.createValueExpression(context, "${bar}", int.class).setValue(context, 1);

// get value for our expression
		System.out.println(e.getValue(context)); // --> 1
	}


}
