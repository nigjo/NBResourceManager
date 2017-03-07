package de.nigjo.nbm.rm.debugui;

import javax.swing.ImageIcon;

import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 * Eine neue Klasse von hof. Erstellt Mar 7, 2017, 10:18:04 AM.
 *
 * @todo Hier fehlt die Beschreibung der Klasse.
 *
 * @author hof
 */
public class ResourceEntry
{
  private final FileObject layerEntry;

  public ResourceEntry(FileObject layerEntry)
  {
    this.layerEntry = layerEntry;
  }

  public ImageIcon getNameIcon()
  {
    Object attribute = layerEntry.getAttribute("resourcePath");
    if(attribute instanceof String)
    {
      return ImageUtilities.loadImageIcon((String)attribute, true);
    }
    return null;
  }

  public String getDisplayName()
  {
    Object attribute = layerEntry.getAttribute("displayName");
    if(attribute instanceof String)
    {
      return (String)attribute;
    }
    return layerEntry.getNameExt();
  }

  public String getResouceId()
  {
    return (String)layerEntry.getAttribute("resourceId");
  }

}
