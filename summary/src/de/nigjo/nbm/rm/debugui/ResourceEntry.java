package de.nigjo.nbm.rm.debugui;

import javax.swing.ImageIcon;

import de.nigjo.nbm.rm.core.ResourceManager;

/**
 * DataWrapper for a resource registration entry.
 *
 * @author hof
 */
public class ResourceEntry
{
  private final String resourceId;

  public ResourceEntry(String resourceId)
  {
    this.resourceId = resourceId;
  }

  public ImageIcon getNameIcon()
  {
    return ResourceManager.getResouceImageIcon(resourceId);
  }

  public String getResouceId()
  {
    return resourceId;
  }

  public String getResoucePath()
  {
    return ResourceManager.getDefault()
        .getResourcePath(resourceId)
        .orElse("<unknown>");
  }

  public String getDisplayName()
  {
    return getResouceId(); //To change body of generated methods, choose Tools | Templates.
  }

  
}
