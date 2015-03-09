/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eagerlogic.jinjector.shared;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dipacs
 */
public class JInjector {

	private static final Map<String, JInjector> namedInjectors = new HashMap<>();
	private static final Map<String, JInjector> packageInjectors = new HashMap<>();

	public static JInjector createNamed(String name) {
		if (name == null) {
			throw new NullPointerException("The 'name' parameter can not be null.");
		}
		if (namedInjectors.containsKey(name)) {
			throw new IllegalArgumentException("A named injector with this name already exists.");
		}

		JInjector injector = new JInjector();
		namedInjectors.put(name, injector);
		return injector;
	}

	public static JInjector getInjector(String name) {
		JInjector res = namedInjectors.get(name);
		if (res == null) {
			throw new IllegalArgumentException("Can't find injector with name: " + name);
		}
		return res;
	}

	public static JInjector createPackageInjector(String packageName) {
		if (packageName == null) {
			throw new NullPointerException("The 'name' parameter can not be null.");
		}
		if (packageInjectors.containsKey(packageName)) {
			throw new IllegalArgumentException("A named injector with this name already exists.");
		}

		JInjector injector = new JInjector();
		packageInjectors.put(packageName, injector);
		return injector;
	}

	public static JInjector getPackageInjector() {
		StackTraceElement[] st = Thread.currentThread().getStackTrace();
		StackTraceElement prevElement = st[2];
		String className = prevElement.getClassName();
		String pkgName = className.substring(0, className.lastIndexOf("."));
		int dotIndex;
		while ((dotIndex = className.lastIndexOf(".")) > -1) {
			className = className.substring(0, dotIndex);
			JInjector res = packageInjectors.get(className);
			if (res != null) {
				return res;
			}
		}
		throw new RuntimeException("Can't find class for the current package: " + pkgName);
	}

    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <T> T get(Class<T> type) {
        T res = (T) instances.get(type);
        if (res == null) {
            Provider<T> provider = (Provider<T>) providers.get(type);
            if (provider == null) {
                throw new IllegalArgumentException("Can't find bind for type: " + type.getName());
            }
            res = provider.get();
        }
        return res;
    }

    public <T> Provider<T> getProvider(final Class<T> type) {
        return new Provider<T>() {

            @Override
            public T get() {
                T res = (T) instances.get(type);
                if (res == null) {
                    Provider<T> provider = (Provider<T>) providers.get(type);
                    if (provider == null) {
                        throw new IllegalArgumentException("Can't find bind for type: " + type.getName());
                    }
                    res = provider.get();
                }
                return res;
            }
        };
    }

    public <T> void register(Class<T> type, T instance) {
        if (type == null) {
            throw new NullPointerException("The 'type' parameter can not be null.");
        }
        if (instance == null) {
            throw new NullPointerException("The 'instance' parameter can not be null.");
        }
        providers.remove(type);
        instances.put(type, instance);
    }

    public <T> void register(Class<T> type, Provider<T> provider) {
        if (type == null) {
            throw new NullPointerException("The 'type' parameter can not be null.");
        }
        if (provider == null) {
            throw new NullPointerException("The 'provider' parameter can not be null.");
        }
        instances.remove(type);
        providers.put(type, provider);
    }

}
