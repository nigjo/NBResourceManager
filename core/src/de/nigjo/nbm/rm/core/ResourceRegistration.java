package de.nigjo.nbm.rm.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registered a resource to the system.
 *
 * @author Jens Hofschröer
 */
@Retention(RetentionPolicy.SOURCE)
@Target(
{
  ElementType.PACKAGE, ElementType.TYPE, ElementType.FIELD
})
public @interface ResourceRegistration
{
  public static final String LAYER_PATH = "de.nigjo.nbm.rm/Resources/";

  String resource();

  String key() default "";

  String displayName() default "";

}
