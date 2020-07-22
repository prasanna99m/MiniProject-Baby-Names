
/**
 * @author (Prasanna Kumar) 
 * @version (16/07/2020)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class Part1 {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int totalboynames=0;
        int totalgirlnames=0;
        int totalnames=0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalnames=totalnames+1;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                totalboynames=totalboynames+1;
            }
            else {
                totalGirls += numBorn;
                totalgirlnames=totalgirlnames+1;
            }
        }
        System.out.println("Total births = " + totalBirths);
        System.out.println("Female girls = " + totalGirls);
        System.out.println("Male boys = " + totalBoys);
        System.out.println("Total names = "+totalnames);
        System.out.println("Total girl names = "+totalgirlnames);
        System.out.println("Total boy names = "+ totalboynames);
    }

    public void testTotalBirths () {
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    public long getRank(int year,String name,String gender){
        String filename= "us_babynames_by_year/"+"yob"+year+".csv";
        FileResource fr= new FileResource(filename);
        long rank;
        for(CSVRecord rec : fr.getCSVParser(false)){
            String namecsv=rec.get(0);
            String gendercsv=rec.get(1);
            
            if(namecsv.equals(name)&&gendercsv.equals(gender)){
                rank=rec.getRecordNumber();
                if(gender.equals("M")){
                    long nogirls=nogirls(filename);
                    return (rank-nogirls);
                }
                
                return rank;
            }
            
        }
        return -1;
    }
    public void testGetRank(){
        int year=1971;
        String name="Frank";
        String gender="M";
        long rank=getRank(year,name,gender);
        System.out.println(rank);
    }
    public long nogirls(String filename){
        long totalgirls=0;
        FileResource fr=new FileResource(filename);
        for(CSVRecord rec : fr.getCSVParser(false)){
            if(rec.get(1).equals("F")){
                totalgirls=totalgirls+1;
            }
        }
        return totalgirls;
    }
    public String getName(int year,long rank,String gender){
        String filename= "us_babynames_by_year/"+"yob"+year+".csv";
        FileResource fr=new FileResource(filename);
        for(CSVRecord rec : fr.getCSVParser(false)){
            
            if(gender=="F"){
                if(rec.getRecordNumber()==rank){
                    return rec.get(0);
                }
            }
            if(gender=="M"){
                long nogirls=nogirls(filename);
                long newrank=rank+nogirls;
                if(rec.getRecordNumber()==newrank){
                    return rec.get(0);
                }
                
            }
        }
        return "NO NAME";
    }
    public void testgetName(){
        String name=getName(1982,450,"M");
        System.out.println(name);
    }
    public void whatIsNameInYear(String name,int year,int newyear,String gender){
        long rank=getRank(year,name,gender);
        String newname=getName(newyear,rank,gender);
        System.out.println(name+" born in "+year+" would be "+newname+" if she was born in "+newyear);
    }
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Owen",1974,2014,"M");
    } 
    public int yearOfHighestRank(String name,String gender){
        DirectoryResource dr=new DirectoryResource();
        long rank;
        String year="";
        int yearint;
        long highestrank=1000000000;
        String filename="";
        for(File f : dr.selectedFiles()){
            String fname=f.getName();
            year=fname.substring(3,7);
            yearint=Integer.parseInt(year);
            rank=getRank(yearint,name,gender);
            if(rank<highestrank && rank>0){
                highestrank=rank;
                filename=f.getName();
            }
        }
        if(highestrank == 1000000000){return -1;}
        String newyear=filename.substring(3,7);
        int newyearint=Integer.parseInt(newyear);
        return newyearint;
    }
    public void testYearOfHighestRank(){
        int year=yearOfHighestRank("Mich","M");
        System.out.println("Year of highest rank "+year);
    }
    public double getAverageRank(String name,String gender){
        DirectoryResource dr=new DirectoryResource();
        long rank;
        String year="";
        int yearint;
        double sum=0;
        double count=0;
        for(File f :dr.selectedFiles()){
            String fname=f.getName();
            year=fname.substring(3,7);
            yearint=Integer.parseInt(year);
            rank=getRank(yearint,name,gender);
            if(rank>0){
                sum=sum+rank;
                count=count+1;
            }
        }
        if(sum==0 && count==0){return -1;}
        double avgrank=sum/count;
        return avgrank;
    }
    public void testGetAverageRank(){
        double avgrank=getAverageRank("Robert","M");
        System.out.println("The average rank "+avgrank);
    }
    public int getTotalBirthsRankedHigher(int year,String name,String gender){
        String filename= "us_babynames_by_year/"+"yob"+year+".csv";
        int totalBirths=0;
        long rank=getRank(year,name,gender);
        FileResource fr=new FileResource(filename);
        for(CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            String gendercsv=rec.get(1);
            String namecsv=rec.get(0);
            long rankcsv=getRank(year,namecsv,gendercsv);
            
            if(gender.equals(gendercsv) && rank > rankcsv ) {
                totalBirths=totalBirths+numBorn;
            }
        }
        return totalBirths;
        
    }
    public void testGetTotalBirthsRankedHigher(){
        int totalBirths=getTotalBirthsRankedHigher(1990,"Emily","F");
        System.out.println(totalBirths);
    }
}
