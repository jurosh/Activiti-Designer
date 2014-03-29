/**
 * 
 */
package org.activiti.designer.eclipse.common;

/**
 * Enum that lists the frequently used images available in this plugin.
 * 
 * @author Tiese Barrell
 * @version 2
 * @since 5.5
 * 
 * 
 */
public enum PluginImage {

  ACTIVITI_LOGO_16x16("icons/logo/activiti.logo.gradients.16x16.png"), //$NON-NLS-1$ 
  ACTIVITI_LOGO_32x32("icons/logo/activiti.logo.gradients.32x32.png"), //$NON-NLS-1$ 
  ACTIVITI_LOGO_48x48("icons/logo/activiti.logo.gradients.48x48.png"), //$NON-NLS-1$ 
  ACTIVITI_LOGO_64x64("icons/logo/activiti.logo.gradients.64x64.png"), //$NON-NLS-1$  
  ACTIVITI_LOGO_128x128("icons/logo/activiti.logo.gradients.128x128.png"), //$NON-NLS-1$
  
  ACTIVITI_VALIDATION_ERROR_32("icons/validation/error_32.png"),
  ACTIVITI_VALIDATION_WARN_32("icons/validation/warning_32.png"),
  ACTIVITI_VALIDATION_INFO_32("icons/validation/info_32.png"),
  ACTIVITI_VALIDATION_IMPR_32("icons/validation/update_32.png");
  
  private final String imagePath;

  private PluginImage(final String imagePath) {
    this.imagePath = imagePath;
  }

  public String getImagePath() {
    return imagePath;
  }

}
