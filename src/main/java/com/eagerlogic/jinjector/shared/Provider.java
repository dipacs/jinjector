/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eagerlogic.jinjector.shared;

/**
 * This interface is responsible to provide an instance of a class. You can think it as a factory.
 *
 * @author dipacs
 * @param <T> The type of the provided instance.
 */
public interface Provider<T> {

	/**
	 * Returns an instance (new or cached) of T.
	 *
	 * @return
	 */
    public T get();

}
