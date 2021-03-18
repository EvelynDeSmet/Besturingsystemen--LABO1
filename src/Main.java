
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("-----------------FCFS----------------");
        fcfs();
        /* System.out.println("-----------------SJF-----------------");
        sjf();
        System.out.println("-----------------SRT-----------------");
        srt();
        System.out.println("-----------------PSN-----------------");
        psn();
        System.out.println("-----------------PSP-----------------");
        psp();
        System.out.println("-----------------RR------------------");
        rr(); */
    }
    
    public static List<List> leesIn(){
        List<Integer> processlist = new ArrayList<Integer>();
        List<List> list = new ArrayList<List>();
        try {
            //creating a constructor of file class and parsing an XML file
            File file = new File("/Users/kiwi/Downloads/CPUScheduler-master/processen10000.xml");
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("process");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                //System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    processlist.add(Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getTextContent()));
                    processlist.add(Integer.parseInt(eElement.getElementsByTagName("servicetime").item(0).getTextContent()));
                    processlist.add(Integer.parseInt(eElement.getElementsByTagName("arrivaltime").item(0).getTextContent()));
                    list.add(processlist);
                    processlist = new ArrayList<Integer>();
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }

    
    public static void fcfs()
    {
        CPUScheduler fcfs = new FirstComeFirstServe();
        List<List> data = leesIn();
        
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            fcfs.add(new Row(pid, arrivaltime, servicetime));
        }
        fcfs.process();
        display(fcfs);
        
    }
    
    public static void sjf()
    {
        CPUScheduler sjf = new ShortestJobFirst();
        List<List> data = leesIn();
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            sjf.add(new Row(pid, arrivaltime, servicetime));
        }
        sjf.process();
        display(sjf);
    }
    
    public static void srt()
    {
        CPUScheduler srt = new ShortestRemainingTime();
        List<List> data = leesIn();
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            srt.add(new Row(pid, arrivaltime, servicetime));
        }
        srt.process();
        display(srt);
    }
    
    public static void psn()
    {
        CPUScheduler psn = new PriorityNonPreemptive();
        List<List> data = leesIn();
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            psn.add(new Row(pid, arrivaltime, servicetime));
        }
        psn.process();
        display(psn);
    }
    
    public static void psp()
    {
        CPUScheduler psp = new PriorityPreemptive();
        List<List> data = leesIn();
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            psp.add(new Row(pid, arrivaltime, servicetime));
        }
        psp.process();
        display(psp);
    }
    
    public static void rr()
    {
        CPUScheduler rr = new RoundRobin();
        rr.setTimeQuantum(2);
        for (List map : data) {
            String pid = map.get(0).toString();
            int arrivaltime = (Integer)map.get(2);
            int servicetime = (Integer)map.get(1);
            rr.add(new Row(pid, arrivaltime, servicetime));
        }
        rr.process();
        display(rr);
    }
    
    public static void display(CPUScheduler object)
    {
        System.out.println("Process\tAT\tBT\tWT\tTAT");

        for (Row row : object.getRows())
        {
            System.out.println(row.getProcessName() + "\t" + row.getArrivalTime() + "\t" + row.getBurstTime() + "\t" + row.getWaitingTime() + "\t" + row.getTurnaroundTime());
        }
        
        System.out.println();
        
        for (int i = 0; i < object.getTimeline().size(); i++)
        {
            List<Event> timeline = object.getTimeline();
            System.out.print(timeline.get(i).getStartTime() + "(" + timeline.get(i).getProcessName() + ")");
            
            if (i == object.getTimeline().size() - 1)
            {
                System.out.print(timeline.get(i).getFinishTime());
            }
        }
        
        System.out.println("\n\nAverage WT: " + object.getAverageWaitingTime() + "\nAverage TAT: " + object.getAverageTurnAroundTime());
    }
}
