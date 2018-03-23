package br.com.cabtecgti.faces.bean;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;

/**
 * A port of Faces ViewScoped to CDI.
 * 
 * @author Cabtec GTI
 */
@Target(value = { METHOD, TYPE, FIELD })
@Retention(value = RUNTIME)
@NormalScope(passivating=true)
@Inherited
public @interface ViewScoped {

}
