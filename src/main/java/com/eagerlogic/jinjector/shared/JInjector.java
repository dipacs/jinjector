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

	/**
	 * Creates a named injector. Named injectors can accessed by it's registered names.
	 * If an injector is already defined whith the given name than this method throws an exception.
	 *
	 * @param name The name of the injector which can be used to later retrieve it.
	 *
	 * @return The new injector which is associated with the given name.
	 */
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

	/**
	 * Returns a named injector which is associated with the given name. If no injector can be found than this
	 * method is throws an exception.
	 *
	 * @param name The name of the injector.
	 *
	 * @return The injector which is associated with the given name.
	 */
	public static JInjector getInjector(String name) {
		JInjector res = namedInjectors.get(name);
		if (res == null) {
			throw new IllegalArgumentException("Can't find injector with name: " + name);
		}
		return res;
	}

	/**
	 * Creates a package injector associated with the given package name.
	 * If an injector is already defined for the given package than this method throws an exception.
	 *
	 * @param packageName The name of the package.
	 *
	 * @return The new injector.
	 */
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

	/**
	 * Returns the injector which is associated with the caller class's package, or one of it's parent package.
	 * If no injector can be found than this method throws an exception.
	 *
	 * @return The injector which is associated with the caller class's package, or one of it's parent package.
	 */
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

	/**
	 * Returns the instance which is associated with the given type.
	 *
	 * @param <T> The type of the instance which you want to get.
	 * @param type The type of the instance which you want to get.
	 *
	 * @return The instance which is associated with the given type.
	 */
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

	/**
	 * Returns the provider which is associated with the given type.
	 *
	 * @param <T> The type of the instance which you want to get.
	 * @param type The type of the instance which you want to get.
	 *
	 * @return The provider which is associated with the given type.
	 */
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

	/**
	 * Registers an instance with a given type. Use this method to create a singleton binding.
	 *
	 * @param <T> The type of the registered instance.
	 * @param type The type of the registered instance. This can be used to retrieve the instance later.
	 * @param instance The instance which will registered for the given type.
	 */
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

	/**
	 * Registers an provider with a given type which will be called every time when an instance of the given type is requested.
	 * Use this method to create a dynamic bind.
	 *
	 * @param <T> The type of the registered provider.
	 * @param type The type of the registered provider. This can be used to retrieve the provider later.
	 * @param provider The provider which will registered for the given type.
	 */
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
