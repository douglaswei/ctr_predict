package www.douglas;

import java.io.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Math;
import java.util.Collections;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;

import java.util.Collections;

/**
 * Created by wgz on 14-11-3.
 */

class IdCounter {
    public static int count = 0;
}

enum BaseFeatId {
    id(),
    hour(),
    C1(),
    banner_pos(),
    site_id(),
    site_domain(),
    site_category(),
    app_id(),
    app_domain(),
    app_category(),
    device_id(),
    device_ip(),
    device_os(),
    device_make(),
    device_model(),
    device_type(),
    device_conn_type(),
    device_geo_country(),
    C17(),
    C18(),
    C19(),
    C20(),
    C21(),
    C22(),
    C23(),
    C24();
    public int idx;

    private BaseFeatId() {
        this.idx = IdCounter.count;
        ++IdCounter.count;
    }
}

class FeatComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        return ((FeatureNode) arg0).getIndex() - ((FeatureNode) arg1).getIndex();
    }

}

public class FeatExtract implements Runnable{

    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return (Math.abs(hash) % 0x1000000);
    }

    // get id idfeat, return label
    int getIdFeat(String line, List<Feature> idFeats, boolean withLabel) {
        StringTokenizer st = new StringTokenizer(line, ",");
        String token = null;
        String featName = null;
        int idx = 0;
        int label = 0;
        List<String> tokens = new ArrayList<String>();
        if (withLabel) {
            label = Integer.parseInt(st.nextToken());
        }
        while (st.hasMoreTokens()) {
            ++idx;
            token = st.nextToken();
            tokens.add(token);
            featName = new String("B_").concat(String.valueOf(idx)).concat(token);
            idFeats.add(new FeatureNode(FNVHash1(featName), 1.0));
        }
        featName = new String("TimeOfDay").concat(
                    tokens.get(BaseFeatId.hour.idx ).substring(6, 7));
        idFeats.add(new FeatureNode(FNVHash1(featName), 1));
        return label;
    }


    void init(String fileName) throws IOException {
        fin = new BufferedReader(new FileReader(new File(fileName)));
        try {
            String line = fin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void end() {
        try {
            if (fin != null) {
                fin.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = null;
        DateFormat sdf = new java.text.SimpleDateFormat("yy hh:mm:ss");
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##0.00");
        List<Feature> feats = new ArrayList<Feature>();
        while (true) {
            try {
                synchronized (fin) {
                    line = fin.readLine();
                    ++lineNum;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            feats.clear();
            int label =  getIdFeat(line, feats, true);
            Collections.sort(feats, new FeatComparator());
            int feaid = 0;
            synchronized (System.out) {
                System.out.printf("%d ", label);
                for (Feature feat : feats) {
                    if (feaid != feat.getIndex()) {
                        System.out.printf("%d:%s ", feat.getIndex(), df.format(feat.getValue()));
                    }
                    feaid = feat.getIndex();
                }
                System.out.print("\n");
                if (lineNum % 10000 == 0) {
                    System.err.printf("[%s]%s:%d\n",
                            sdf.format(new Date()),
                            Thread.currentThread().getName(),
                            lineNum);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FeatExtract featExtract = new FeatExtract();
        featExtract.init(args[0]);
        Thread[] ths = new Thread[10];
        for (int idx = 0; idx < ths.length; ++idx) {
            Thread th = new Thread(featExtract, String.valueOf(idx));
            th.start();
            ths[idx] = th;
        }
        for (Thread th : ths) {
            th.join();
        }
        featExtract.end();
    }

    BufferedReader fin = null;
    int lineNum = 0;
}
