# DTMS

This is a short introduction to Digital_tissue_management_suite(DTMS) developed by Patrik Wurzel.

DTMS is a content management system(CMS) for biological imagedata. It holds functions for the structural storage of 2D, 3D and 4D images as well as additional files of Imaris and Impro3000. 

Any information about underlying filepaths like the basic csv files, the general applied serverlogic can be found in the settings. Additionaly the settings window allows applying new short cuts. 

For any crashreport move the mouse over the status circle and follow the given instructions.

Adding and managing new images works via the add image menu item. To add an image a minimum information of CaseID, FileType and the Filepath has to be given, otherwise the assignment will be rejected.

After adding the image the given file will be postponed inside the selected imageserver path.

For adding new additional files like ims or gml use the properties view of the main view. For that purpose search for the connected 2D or 3D file, press the properties button and use the imaris file button inside the upcoming window node. Imaris and Impro3000 files can't be added without a given main file.

If you want to edit the information use the rightclick menu. If you have changed a part declared as a part of the imageserverlogic the image will be moved to the correct folder. If you change the connected image file, the old file will be  stored inside the current folder.

Be aware of deleting entries inside DTMS, the connected image file will be deleted either.

For more information, contact the developer.
