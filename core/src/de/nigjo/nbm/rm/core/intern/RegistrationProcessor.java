package de.nigjo.nbm.rm.core.intern;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

import de.nigjo.nbm.rm.core.ResourceRegistration;

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
  public SourceVersion getSupportedSourceVersion()
  {
    return SourceVersion.latestSupported();
  }

  @Override
  protected boolean handleProcess(
      Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
      throws LayerGenerationException
  {
    if(!roundEnv.processingOver() && !roundEnv.errorRaised())
    {
      annotations.forEach(
          a -> roundEnv.getElementsAnnotatedWith(a)
              .forEach(this::handleAnnotation));
    }
    return true;
  }

  private void handleAnnotation(Element e)
  {
    //message(Diagnostic.Kind.NOTE, e, "processing %s.", e.toString());

    ResourceRegistration[] registrations =
        e.getAnnotationsByType(ResourceRegistration.class);
    for(ResourceRegistration registration : registrations)
    {
      if(e instanceof PackageElement)
      {
        handlePackageRegistration((PackageElement)e, registration);
      }
      else if(e instanceof ExecutableElement)
      {
        handleMethodRegistration((ExecutableElement)e, registration);
      }
      else if(e instanceof VariableElement)
      {
        handleFieldRegistration((VariableElement)e, registration);
      }
    }
  }

  private void handlePackageRegistration(
      PackageElement e, ResourceRegistration registration)
  {
    warning(e, "package annotations not supported, yet");
  }

  private void handleMethodRegistration(
      ExecutableElement executableElement, ResourceRegistration registration)
  {
    String resParameter = registration.resource();
    if(!resParameter.isEmpty())
    {
      error(executableElement, "'resource' can not be used with methods");
      return;
    }
    if(registration.key().isEmpty())
    {
      error(executableElement, "missing 'key'");
      return;
    }

    if(!"java.lang.String".equals(executableElement.getReturnType().toString()))
    {
      error(executableElement, "method must return a String");
      return;
    }
    if(!new HashSet<>(executableElement.getModifiers()).removeAll(
        Arrays.asList(Modifier.PUBLIC, Modifier.STATIC)))
    {
      error(executableElement, "method must be public static");
      return;
    }

    String resourcePath =
        (executableElement.getEnclosingElement().toString()
            + '/' + executableElement.getSimpleName().toString())
            .replace('.', '/')
        + ".png";

    LayerBuilder.File fileEntry = layer(executableElement).file(
        String.format("%s/%s",
            ResourceRegistration.LAYER_PATH,
            resourcePath.replace('/', '-')));

    fileEntry.stringvalue("resourceId", registration.key());
    fileEntry.methodvalue("resourcePath",
        executableElement.getEnclosingElement().toString(),
        executableElement.getSimpleName().toString());

    String displayName = registration.displayName();
    if(!displayName.isEmpty())
    {
      if(displayName.charAt(0) == '#')
      {
        try
        {
          fileEntry.bundlevalue("displayName", displayName);
        }
        catch(LayerGenerationException ex)
        {
          error(executableElement, ex.getLocalizedMessage());
          return;
        }
      }
      else
      {
        fileEntry.stringvalue("displayName", displayName);
      }
    }

    fileEntry.write();
  }

  private void handleFieldRegistration(
      VariableElement variableElement, ResourceRegistration registration)
  {
    String resParameter = registration.resource();
    if(resParameter.isEmpty())
    {
      error(variableElement, "missing 'resource' parameter");
      return;
    }
    if(!registration.key().isEmpty())
    {
      error(variableElement, "'key' can not be used with fields");
      return;
    }

    String resourcePath;
    if(resParameter.indexOf('/') < 0)
    {
      PackageElement parent =
          processingEnv.getElementUtils().getPackageOf(variableElement);
      String packagePath = parent.getQualifiedName().toString().replace('.', '/');
      resourcePath = packagePath + '/' + resParameter;
    }
    else
    {
      resourcePath = resParameter;
      if(resParameter.indexOf('/') == 0)
      {
        warning(variableElement, "'resource' should not start with a slash.");
      }
    }

    if(!"java.lang.String".equals(variableElement.asType().toString()))
    {
      error(variableElement, "field must be of type String");
      return;
    }

    boolean success = true;
    HashSet<Modifier> modifiers = new HashSet<>(variableElement.getModifiers());
    success &= modifiers.remove(Modifier.FINAL);
    success &= modifiers.remove(Modifier.STATIC);
    if(!success)
    {
      error(variableElement, "field must be static final");
      return;
    }

    String key;
    Object constantValue = variableElement.getConstantValue();
    if(constantValue instanceof String)
    {
      key = (String)constantValue;
      if(key.isEmpty())
      {
        error(variableElement, "field must not be empty");
        return;
      }
      if(key.indexOf('.') < 0)
      {
        warning(variableElement, "'key' should contain dots for namespaces");
      }
    }
    else
    {
      error(variableElement, "field must have a constant value");
      return;
    }

    LayerBuilder.File fileEntry = layer(variableElement).file(
        String.format("%s/%s",
            ResourceRegistration.LAYER_PATH,
            resourcePath.replace('/', '-')));
    fileEntry.stringvalue("resourceId", key);
    fileEntry.stringvalue("resourcePath", resourcePath);

    fileEntry.write();
  }

  private void warning(Element executableElement,
      String message, Object... args)
  {
    message(Diagnostic.Kind.WARNING, executableElement, message, args);
  }

  private void error(Element executableElement,
      String message, Object... args)
  {
    message(Diagnostic.Kind.ERROR, executableElement, message, args);
  }

  private void message(Diagnostic.Kind kind, Element executableElement,
      String message, Object... args)
  {
    String fullMessage;
    if(args.length == 0)
    {
      fullMessage = message;
    }
    else
    {
      fullMessage = String.format(message, args);
    }

    executableElement.getAnnotationMirrors().stream()
        .filter(mirror -> "de.nigjo.nbm.rm.core.ResourceRegistration"
            .equals(mirror.getAnnotationType().toString()))
        .findFirst()
        .ifPresent(mirror ->
            processingEnv.getMessager().printMessage(kind,
                fullMessage, executableElement, mirror));

  }

}
