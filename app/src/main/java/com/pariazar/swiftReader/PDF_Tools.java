package com.pariazar.swiftReader;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.util.ArrayList;

public class PDF_Tools {

    public static String getTextOfPage(String address_book, int page_number){
        StringBuilder res  = new StringBuilder();
        try {
            String parsedText="";
            PdfReader reader = new PdfReader(address_book);
            int n = reader.getNumberOfPages();
            // for (int i = 0; i <n ; i++) {
            parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, page_number+1).trim()+"\n"; //Extracting the content from the different pages
            //}
            res.append(parsedText);
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return res.toString();
    }

    public static ArrayList<String> searchWordInPages(String address_book, String word){
        ArrayList<String> result = new ArrayList<>();
        try {
            String parsedText="";
            PdfReader reader = new PdfReader(address_book);
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
                if(checkWordInString(parsedText,word)){
                    result.add(String.valueOf(i+1));
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    public static boolean checkWordInString(String mystr, String word){
        if(mystr.contains(word)){
            return true;
        }
        else{
            return false;
        }
    }

    public static int estimateTimeToReadMin(int noPage){
        int maxCountWordInPage = 500;
        int minCountWordInPage = 250;
        int halfNOPage = noPage / 2 ;
        int totalWord = (maxCountWordInPage*halfNOPage)+(minCountWordInPage*halfNOPage);
        //int totalWord = noPage * 500;
        int minutes = totalWord / 200;
        return minutes;
    }


}
