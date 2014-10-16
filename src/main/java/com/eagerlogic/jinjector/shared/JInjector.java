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
    
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Provider<?>> providers = new HashMap<>();
    
    public <T> T get(Class<T> type) {
        return getProvider(type).get();
    }
    
    public <T> Provider<T> getProvider(final Class<T> type) {
        return new Provider<T>() {

            @Override
            public T get() {
                T res = (T) instances.get(type);
                if (res == null) {
                    Provider<?> provider = providers.get(type);
                    if (provider == null) {
                        return null;
                    }
                    res = (T) provider.get();
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
        instances.put(type, instance);
    }
    
    public <T> void register(Class<T> type, Provider<T> provider) {
        if (type == null) {
            throw new NullPointerException("The 'type' parameter can not be null.");
        }
        if (provider == null) {
            throw new NullPointerException("The 'provider' parameter can not be null.");
        }
        providers.put(type, provider);
    }
    
}
