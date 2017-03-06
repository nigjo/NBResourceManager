package de.nigjo.nbm.rm.core.intern;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.ServiceProvider;

import de.nigjo.nbm.rm.core.ResourceManager;
import de.nigjo.nbm.rm.core.ResourceRegistration;

/**
 * ResourceManager to search in XML-Layer path '/de.nigjo.nbm.rm/Resources/'.
 *
 * @author Jens Hofschröer
 */
@ServiceProvider(service = ResourceManager.class)
public class DefaultResourceManager implements ResourceManager
{
  @Override
  public Optional<String> getResourcePath(String resId)
  {
    FileObject configFolder = FileUtil.getConfigFile(ResourceRegistration.LAYER_PATH);
    if(configFolder == null)
    {
      return Optional.empty();
    }
    return Arrays.stream(configFolder.getChildren())
        .filter(f -> resId.equals(f.getAttribute("resourceId")))
        .map(f -> String.valueOf(f.getAttribute("resourcePath")))
        .findFirst();
  }

  @Override
  public Collection<String> getResourceIds()
  {
    FileObject configFolder = FileUtil.getConfigFile(ResourceRegistration.LAYER_PATH);
    if(configFolder == null)
    {
      return Collections.emptySet();
    }
    return Arrays.stream(configFolder.getChildren())
        .map(f -> f.getAttribute("resourceId"))
        .filter(f -> f instanceof String)
        .map(f -> (String)f)
        .collect(Collectors.toSet());
  }

}
