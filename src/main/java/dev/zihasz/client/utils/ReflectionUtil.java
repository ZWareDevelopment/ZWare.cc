package dev.zihasz.client.utils;

import org.reflections.Reflections;

import java.util.Set;

public class ReflectionUtil implements Util {

	public static Set<Class<?>> findClasses(String pkg, Class subType) {
		Reflections reflections = new Reflections(pkg);
		return reflections.getSubTypesOf(subType);
	}

}
