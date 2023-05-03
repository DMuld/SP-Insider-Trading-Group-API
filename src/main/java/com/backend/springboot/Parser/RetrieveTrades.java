package com.backend.springboot.Parser;

import com.backend.springboot.database.Trade;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.*;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.lang.*;
import java.net.URL;

public class RetrieveTrades {

    static String ScheduleB = "S\u0000\u0000\u0000\u0000\u0000\u0000\u0000 B: T\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
    static Pattern pattern = Pattern.compile("\\]");
    static Matcher matcher;
    static Pattern monthPattern = Pattern.compile("\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\b");
    static int idCounter = 30;

    public static void main(String[] args) throws Exception {

        /*
        This first section read from the txt file and get all doc ID's that are of filing type C and T as those
        filing types contain relevant financial disclosures
         */
        File file = new File("2023FD.txt");
        String url = "https://disclosures-clerk.house.gov/public_disc/financial-pdfs/2023/";
        Scanner scanner = new Scanner(file);
        scanner.nextLine();


        while (scanner.hasNext()) {
            String fileName = "";
            String prefix = scanner.next();
            String lastName;
            if (prefix.equals("Hon.") || prefix.equals("Mr.") || prefix.equals("Ms.") || prefix.equals("Mrs.s")) {
                lastName = scanner.next();
            } else {
                lastName = prefix;
            }

            String firstName = scanner.next();
            String secondName = scanner.next();
            String suffix = "";
            //System.out.print(suffix + " ");
            String filingType = secondName;
            if (secondName.length() > 1) {
                firstName = firstName + " " + secondName;
                filingType = scanner.next();
                if (secondName.equals("Jr") || secondName.equals("III") || secondName.equals("II") || secondName.equals("MD")) {
                    suffix = secondName;
                    //        System.out.print(" ");
                }
            }
            while (filingType.length() > 1) {
                //    firstName += " " + filingType;
                filingType = scanner.next();
            }

            //System.out.print(firstName + " ");
            //System.out.print(filingType + " ");
            String stateDST = scanner.next();
            //System.out.print(stateDST + " ");
            scanner.next();
            String date = scanner.next();
            //System.out.print(date + " ");
            String docID = scanner.next();
            //System.out.print(docID + " ");
            URL docURL = new URL(url + docID + ".pdf");
            //System.out.println(docURL);


            if (filingType.equals("C") || filingType.equals("T")) {
                System.out.printf("%s %s. Date: %s, %s Document ID: %s, link: %s\n", firstName, lastName, date, filingType, docID, docURL);
                ReadableByteChannel rbc = Channels.newChannel(docURL.openStream());
                fileName = lastName + docID + ".pdf";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fileReaderA(fileName, docID, date, lastName);
            }
        }
    }

    static void fileReaderA(String fileName, String docID, String dateReported, String lastName) throws IOException {

        System.out.println("***********ASSETS**********");
        if (docID.charAt(0) == '8'){
            System.out.println("INVALID FORM");
            return;
        }
        File pdfFile = new File(fileName);
        PDDocument pdf = PDDocument.load(pdfFile);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String readPDF = pdfStripper.getText(pdf);
        //System.out.print(readPDF);
        pdf.close();
        Scanner pdfScanner = new Scanner(readPDF);

        for (int i = 0; i < 11; i++) {
            pdfScanner.nextLine();
            //System.out.println(scanner.nextLine());
        }

        for (int i = 0; i < 3; i++) {
            if (pdfScanner.nextLine().equals("None disclosed.")) {
                System.out.println("NONE DISCLOSED");
                return;
            }
        }

        String AccountName = pdfScanner.nextLine();
        while (!AccountName.equals("None disclosed.")) {

            String backup = " ";
            matcher = pattern.matcher(AccountName);
            while (!(matcher.find())) {
                backup = AccountName;
                AccountName = pdfScanner.nextLine();
                if (AccountName.equals(ScheduleB) || backup.equals(ScheduleB)) {
                    System.out.println("********TRANSACTIONS********");
                    fileReaderB(pdfScanner, lastName , dateReported);
                    System.out.println("*************END***************");
                    return;
                }
                matcher = pattern.matcher(AccountName);
                if (AccountName.equals("None disclosed.")) return;
            }
            if (AccountName.charAt(0) == '['){
                backup = backup + AccountName;
                AccountName = backup;
            }
            //System.out.println(AccountName);
            String assetValue = "";
            String stockName = "";
            String vision = "";

            String type = getType(AccountName);
            String assetName = formatAssetName(AccountName);

            for (int i = 0; i < AccountName.length(); i++) {
                if (AccountName.charAt(i) == '$') {
                    for (int j = i; j < AccountName.length(); j++) {
                        assetValue += AccountName.charAt(j);
                    }
                }
            }
            if (backup.charAt(0) == '$') {
                assetValue = backup;
            }
            else{
                assetValue += pdfScanner.nextLine();
                if (assetValue.charAt(assetValue.length() - 1) == ']' || assetValue.charAt(assetValue.length() - 1) == '⇒')
                    assetValue = "THROW CASE";
                else{
                    vision = pdfScanner.nextLine();
                    if (vision.charAt(0) == '$')
                        assetValue += vision;
                }
            }
            assetValue = formatAssetValue(assetValue);
            stockName = getStockAbbreviation(assetName);
            if (type.equals("[ST]") || type.equals("[PS]") || type.equals("SA")) System.out.printf("Asset: %s. %s. Value: %s\n", assetName, stockName, assetValue);
            AccountName = pdfScanner.nextLine();
        }
    }

    static void fileReaderB(Scanner pdfScanner, String lastName, String reportDate) throws IOException {
        String AccountName = pdfScanner.nextLine();
        if (AccountName.equals("None disclosed.")){
            System.out.println("NONE DISCLOSED");
            return;
        }
        Pattern newPattern = Pattern.compile("\\d+");
        Pattern dollar = Pattern.compile("\\$");
        String backup = "";
        String ticker = "";
        String date= "";
        String sale = "";
        String amount = "";
        String type = "";

        while (!AccountName.equals("None disclosed.")) {
            AccountName = pdfScanner.nextLine();
            matcher = pattern.matcher(AccountName);
            while (!(matcher.find())) {
                backup = AccountName;
                AccountName = pdfScanner.nextLine();
                matcher = pattern.matcher(AccountName);
                if (AccountName.equals("None disclosed.")) return;
            }

            if (AccountName.charAt(0) == '[' || AccountName.charAt(0) == '('){
                backup = backup + " " + AccountName;
                AccountName = backup;
            }
            Matcher newMatcher = dollar.matcher(AccountName);
            if (newMatcher.find() && !AccountName.equals("iShares iBoxx $ Investment Grade Corporate Bond ETF (LQD) [ST]")){
                Scanner accountScanner = new Scanner(AccountName);
                AccountName = accountScanner.next();
                matcher = pattern.matcher(AccountName);
                while (!(matcher.find())) {
                    AccountName += " " + accountScanner.next();
                    matcher = pattern.matcher(AccountName);
                }
                date = accountScanner.next();
                newMatcher = newPattern.matcher(date);
                if (date.equals("$250,000")){
                    date = "08/05/2022";
                    sale = "Purchase";
                    amount = "$100,001 - $250,000";
                }
                else if(date.equals("$100,000")){
                    date = "04/04/2022";
                    sale = "Purchase";
                    amount = "$50,001 - $100,000";
                }
                else {
                    if (!newMatcher.find()) date = accountScanner.next();
                    sale = accountScanner.next();
                    if (sale.equals("P")) sale = "Purchase";
                    if (sale.equals("S")) sale = "Sell";
                    if (accountScanner.hasNext()){
                        amount = accountScanner.nextLine();
                        if (amount.equals("")) amount = accountScanner.nextLine();
                        if (amount.equals("(partial)")) amount = accountScanner.nextLine();
                    }
                    else{
                        amount = pdfScanner.nextLine();
                        if (amount.equals("")) amount = pdfScanner.nextLine();
                        if (amount.equals("(partial)")) amount = pdfScanner.nextLine();
                    }
                    if (amount.charAt(amount.length() - 1) == '-') amount += " " + pdfScanner.nextLine();
                }
            }
            else {
                date = pdfScanner.next();
                newMatcher = newPattern.matcher(date);
                if (!newMatcher.find()) date = pdfScanner.next();
                sale = pdfScanner.next();
                if (sale.equals("P")) sale = "Purchase";
                if (sale.equals("S")) sale = "Sell";
                amount = pdfScanner.nextLine();
                if (amount.equals("")) amount = pdfScanner.nextLine();
                if (amount.equals("(partial)")) amount = pdfScanner.nextLine();
                if (amount.charAt(amount.length() - 1) == '-') amount += " " + pdfScanner.nextLine();
            }
            ticker = getStockAbbreviation(AccountName);
            type = getType(AccountName);
            AccountName = formatAssetName(AccountName);
            int size = sizeFloor(amount);
            if (type.equals("[ST]") || type.equals("[SA]")) {
                float price = getStockPrice(ticker, date);
                if (price != -1) {
                    System.out.printf("Account Name: %s. Date: %s. %s. Amount: %s. -> %s. Stock Price: %.2f\n", AccountName, date, sale, amount, size, price);
                    dbConnector( lastName + idCounter, ticker, reportDate, date, sale, size, price);
                    idCounter++;
                }
            }
        }
    }

    static String formatAssetValue(String assetValues){

        if (assetValues.equals("THROW CASE")) return assetValues;
        int dollarSigns = 0;
        String newAssetValue = "";
        for (int i = 0; i < assetValues.length(); i++){
            if (assetValues.charAt(i) == '$') dollarSigns += 1;
            if (dollarSigns == 2 && assetValues.charAt(i) == ' ') return newAssetValue;
            if (dollarSigns >= 1) newAssetValue += assetValues.charAt(i);
        }
        if (dollarSigns == 2) return newAssetValue;
        return "$1 - $1000";
    }

    static String getType(String name){
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '[') return name.substring(i, name.length());
        }
        return name;
    }

    static String formatAssetName(String name){
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '[') return name.substring(0, i - 1);
        }
        return name;
    }

    static String getStockAbbreviation(String name){

        if (name.equals("")) return "NOT A STOCK";

        int par = 0;
        String abbr = "";
        for (int i = 0; i < name.length(); i++){

            if (name.charAt(i) == ')') par++;
            if (name.charAt(i) == '.' && par ==1) abbr += '-';
            else if (par == 1) abbr += name.charAt(i);
            if (name.charAt(i) == '(') par ++;
        }
        if (par == 0) return "PRIVATE STOCK";
        return abbr;
    }

    static void dbConnector(String id, String ticker, String published, String traded, String type, int size, float price) throws IOException {

        Trade trade = new Trade();
        trade.setTICKER(ticker);
        trade.setPUBLISHED(published);
        trade.setTRADED(traded);
        trade.setFILEDAFTER(subtractDates(published, traded));
        trade.setTYPE(type);
        trade.setVOLUME(size);
        trade.setPRICE(price);
    }

    static int sizeFloor(String size){
        int dollar = 0;
        String amount = "";
        for (int i =0; i < size.length(); i++){
            if (dollar == 2 && size.charAt(i) != ',') {
                amount += size.charAt(i);
            }
            if (size.charAt(i) == '$') dollar++;

        }
        return Integer.parseInt(amount);
    }

    static int subtractDates(String pub, String traded){

        int month1 = 0;
        int month2 = 0;
        if (traded.charAt(0) == '1'){
            month2 = 10;
        }
        if (traded.charAt(0) == '1'){
            month1 = 10;
        }
        for(int i = 0; i < 10; i++){
            if (traded.charAt(1) == i) month2 += i;
            if (pub.charAt(1) == i) month1 += i;
        }
        int date1 = 0;
        int date2 = 0;
        for(int i = 0; i < 3; i++){
            if (traded.charAt(3) == i){
                date2 = 10;
            }
            if (traded.charAt(3) == i){
                date1 = 10;
            }
        }
        for(int i = 0; i < 10; i++){
            if (traded.charAt(4) == i) date2 += i;
            if (pub.charAt(4) == i) date1 += i;
        }
        int year1 = 0;
        int year2 = 0;
        if (traded.charAt(10) == '3') year2 = 0;
        if (pub.charAt(10) == '3') year1 = 356;

        year1 += (month1 * 30) + date1;
        year2 += (month2 * 30) + date2;

        return Math.abs(year2 - year1);
    }

    static float getStockPrice(String ticker, String date) throws IOException{

        if (!date.toLowerCase().equals(date.toUpperCase())) return -1;
        URL url = new URL("https://finance.yahoo.com/quote/" + ticker + "/history?p=" + ticker + "/");
        System.out.println(url);
        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        int zero = (int) date.charAt(0) - 48;
        int ones = (int) date.charAt(1) - 48;
        int newMonth = zero * 10 + ones;
        String day = "" +  date.charAt(3);
        if (date.length() == 10) day += date.charAt(4);
        String year = "" + (2020 + (int) date.charAt(date.length() - 1) - 48);
        String formattedDate = month[newMonth -1]+ day + "," +  year;
        String spacedDate = month[newMonth -1]+ " "+ day + ", " +  year;
        //System.out.println(formattedDate);
        Pattern pattern = Pattern.compile(formattedDate);
        Scanner webScanner = new Scanner(url.openStream());
        StringBuffer stringBuffer = new StringBuffer();


        webScanner.nextLine();
        webScanner.nextLine();
        webScanner.nextLine();
        webScanner.nextLine();
        webScanner.nextLine();

        String output = "";


        stringBuffer.append(webScanner.nextLine());
        output = stringBuffer.toString();
        matcher = pattern.matcher(output);

        boolean check = matcher.find();
       //System.out.println(check);
        if (!check){
            pattern = Pattern.compile(spacedDate);
            matcher = pattern.matcher(output);
            check = matcher.find();
            //System.out.println(check);
        }

        if (!check){
            pattern = Pattern.compile("Dec 13, 2022");
            matcher = pattern.matcher(output);
            check = matcher.find();
            //System.out.println(check);
        }


        if(!check){
            pattern = Pattern.compile("Dec13,2022");
            matcher = pattern.matcher(output);
            check = matcher.find();
            //System.out.println(check);
        }
        if(!check){
            pattern = Pattern.compile("Dec15,2022");
            matcher = pattern.matcher(output);
            check = matcher.find();
            //System.out.println(check);
        }
        if (!check){
            pattern = Pattern.compile("Dec 15, 2022");
            matcher = pattern.matcher(output);
            check = matcher.find();
            //System.out.println(check);
        }
        if (!check) return -1;
        //System.out.println(matcher.start());
        String put = "";
        boolean dot = true;
        int j = 60;
        while (dot){
            if (output.charAt(j + matcher.start()) == '1' || output.charAt(j+ matcher.start()) == '2'|| output.charAt(j+ matcher.start()) == '3' ||output.charAt(j+ matcher.start()) == '4' ||output.charAt(j+ matcher.start()) == '5'||output.charAt(j+ matcher.start()) == '6' ||output.charAt(j+ matcher.start()) == '7'||output.charAt(j+ matcher.start()) == '8'||output.charAt(j+ matcher.start()) == '9')
                put += output.charAt(j+ matcher.start());
            if (output.charAt(j + matcher.start()) == '.'){
                put += output.charAt(j + matcher.start());
                put += output.charAt(j + matcher.start() + 1);
                put += output.charAt(j + matcher.start() + 2);
                dot = false;
            }
            j++;
        }

        //System.out.print(put);
        float price = Float.parseFloat(put);
        //System.out.println(put);
        return price;
    }

}

