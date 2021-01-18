/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.io.importer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */
public class DtmsImportParser {

    private static ArrayList<String> parse_csv_line(String line) {
        ArrayList<String> output = new ArrayList<String>();
        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (String t : tokens) {
            output.add(t.replace("\"", ""));
        }
        return output;
    }

    public static HashMap<Integer, ImportCase> parse_dtmsfiles_to_case_map(File databasefolder) {
        HashMap<Integer, ImportCase> cases = new HashMap<>();
        HashMap<String, Integer> caseID_connector = new HashMap<>();
        File file = new File(databasefolder.getAbsolutePath() + "\\DataStorage.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                ArrayList<String> image_line = parse_csv_line(line);
                String imageID = image_line.get(0);
                Integer caseID = Integer.parseInt(image_line.get(1));
                caseID_connector.put(imageID, caseID);
                if (!cases.containsKey(caseID)) {
                    Integer clinicID = 0;
                    if (imageID.startsWith("W")) {
                        clinicID = 1;
                    }
                    cases.put(Integer.parseInt(image_line.get(1)), new ImportCase(image_line.get(2), Integer.parseInt(image_line.get(1)), image_line.get(5), image_line.get(3), clinicID, 0));
                }
                String filetype = image_line.get(9);
                switch (filetype) {
                    case "2D":
                        ImportTwoDim twoDimage = new ImportTwoDim(image_line.get(0), image_line.get(6), image_line.get(7), image_line.get(8), "nod", image_line.get(4), "", image_line.get(11), image_line.get(10));
                        cases.get(caseID).add_two_Dim(twoDimage);
                        break;
                    case "3D":
                        ImportThreeDim threeDimage = new ImportThreeDim(image_line.get(0), image_line.get(4), image_line.get(6), "", image_line.get(7), image_line.get(8), image_line.get(11), image_line.get(10), -1, "", "");
                        cases.get(caseID).add_three_Dim(threeDimage);
                        break;
                    case "4D":
                        ImportFourDim fourDimage = new ImportFourDim(image_line.get(0), image_line.get(4), image_line.get(6), "", image_line.get(7), image_line.get(8), image_line.get(11), image_line.get(10), -1, "", "");
                        cases.get(caseID).add_four_Dim(fourDimage);
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DtmsImportParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DtmsImportParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> dim = Arrays.asList("2D", "3D", "4D");
        for (String cur : dim) {
            File curfile = new File(databasefolder.getAbsolutePath() + "\\" + cur + "PropertyStorage.csv");
            try (BufferedReader br = new BufferedReader(new FileReader(curfile))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    ArrayList<String> image_line = parse_csv_line(line);
                    String imageID = image_line.get(0);
                    Integer caseID = caseID_connector.get(imageID);
                    if (caseID != null) {
                        switch (cur) {
                            case "2D":
                                HashMap<String, ImportTwoDim> twoDimages = cases.get(caseID).get_twodim_images();
                                ImportTwoDim twoDimage = twoDimages.get(imageID);
                                if (twoDimage != null) {
                                    if (!image_line.get(4).isEmpty()) {
                                        twoDimage.set_cellgraph_path(image_line.get(4));
                                    }
                                    twoDimages.put(imageID, twoDimage);
                                    cases.get(caseID).update_twodims(twoDimages);
                                }
                                break;
                            case "3D":
                                HashMap<String, ImportThreeDim> threeDimages = cases.get(caseID).get_threedim_images();
                                ImportThreeDim threeDimage = threeDimages.get(imageID);
                                if (threeDimage != null) {
                                    if (!image_line.get(4).isEmpty()) {
                                        threeDimage.set_tiles(Integer.parseInt(image_line.get(4)));
                                    }
                                    if (!image_line.get(5).isEmpty()) {
                                        threeDimage.set_imarisfile(image_line.get(5));
                                    }
                                    if (!image_line.get(6).isEmpty()) {
                                        threeDimage.set_overviewfile(image_line.get(6));
                                    }
                                    threeDimages.put(imageID, threeDimage);
                                    cases.get(caseID).update_threedims(threeDimages);
                                }
                                break;
                            case "4D":
                                HashMap<String, ImportFourDim> fourDimages = cases.get(caseID).get_fourdim_images();
                                ImportFourDim fourDimage = fourDimages.get(imageID);
                                if (fourDimage != null) {
                                    if (!image_line.get(4).isEmpty()) {
                                        fourDimage.set_tiles(Integer.parseInt(image_line.get(4)));
                                    }
                                    if (!image_line.get(5).isEmpty()) {
                                        fourDimage.set_imarisfile(image_line.get(5));
                                    }
                                    if (!image_line.get(6).isEmpty()) {
                                        fourDimage.set_overviewfile(image_line.get(6));
                                    }
                                    fourDimages.put(imageID, fourDimage);
                                    cases.get(caseID).update_fourdims(fourDimages);
                                }
                                break;
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DtmsImportParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DtmsImportParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cases;
    }

    public static HashMap<String, String> parse_comments(String comment) {
        HashMap<String, String> info = new HashMap<>();
        info.put("fixedcomment", comment);
        if (comment.contains("Bewertung:")) {
            Integer index1 = comment.indexOf("Bewertung:") + 10;
            Integer index2 = comment.indexOf(",", comment.indexOf("Bewertung:"));
            String replacer = ",";
            if (index2 < 0) {
                index2 = comment.length();
                replacer = "";
            }
            String rating = comment.substring(index1, index2);
            replacer = "Bewertung:" + rating + replacer;
            info.put("Bewertung", rating.replace(" ", ""));
            String replaced = comment.replace(replacer, "");
            if(replaced.startsWith(" ")){
                replaced = replaced.substring(1, replaced.length());
            }
            if(replaced.endsWith(", ")){
                replaced = replaced.substring(0, replaced.length()-2);
            }
            info.put("fixedcomment", replaced);
        }
        return info;
    }

    public static HashMap<String, String> parse_filepath(String filepath) {
        HashMap<String, String> info = new HashMap<>();
        return info;
    }
}
