package de.nigjo.nbm.rm.core.intern;

import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 * Processor to the {@code @ResourceRegistration} Annotation.
 *
 * @author Jens Hofschröer
 */
@ServiceProvider(service = Processor.class)
@SupportedAnnotationTypes("de.nigjo.nbm.rm.core.ResourceRegistration")
public class RegistrationProcessor extends LayerGeneratingProcessor
{
  @Override
  protected boolean handleProcess(
      Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
      throws LayerGenerationException
  {
    //TODO: handle resouces
    return true;
  }

}
