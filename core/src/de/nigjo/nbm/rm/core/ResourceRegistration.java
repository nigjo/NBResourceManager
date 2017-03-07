package de.nigjo.nbm.rm.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers a resource to the system. All resources are registerd under the path
 * {@code de.nigjo.nbm.rm/Resources/} in the SystemFilesystem.
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

  /**
   * The registered key for this resource. The key must be unique to the runtime system.
   * If annotating a Field, this parameter must be ommited. For all other cases this
   * parameter is mandatory.
   *
   * @return a key for the registered resource.
   */
  String key() default "";

  /**
   * Defines the module local path to the resource. If this is only a file name the path
   * ist treated as relative to the current element.
   *
   * @return returns an absolute path to the resource or a filename to be relative to the
   * current element
   */
  String resource() default "";

  /**
   * A displayName for this resource, used for debug perpose only.
   *
   * @return A display name for this resource. if the first character is a "#", the return
   * value is treated as a resource bundle key.
   */
  String displayName() default "";

}
