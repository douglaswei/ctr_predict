package www.douglas;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.*;

/**
 * Created by wgz on 14/11/4.
 */


public class FeatureExtractor {
    public FeatureExtractor() {
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length >= 2) {
            String trainFilelName = args[0];
            String trainFeatureFileName = args[1];
            int threadsNum = 1;
            if (args.length > 2) {
                threadsNum = Integer.parseInt(args[2]);
            }
            FeatureExtractor featureExtractor = new FeatureExtractor();
            featureExtractor.ExtractFeaturesFrom(
                    trainFilelName, trainFeatureFileName, threadsNum);
        } else {
            return;
        }
    }

    void ExtractFeaturesFrom(
            String fileName,
            String outFileName,
            int threadNum) throws InterruptedException {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        boolean isTrainData = true;
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "featureFactory.xml");
        FeatureNameFactory featureNameFactory =
                (FeatureNameFactory) context.getBean("featureNameFactory");
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            bufferedWriter = new BufferedWriter(new FileWriter(outFileName));

            // first line is column description
            String line = bufferedReader.readLine();
            FieldDescription.getInstance().init(line);
            if (FieldDescription.getInstance().getValue("click") < 0) {
                isTrainData = false;
            }

            Thread[] threads = new Thread[threadNum];
            for (int index = 0; index < threads.length; ++index) {
                Extractor extractor = new Extractor();
                extractor.setFeatureNameFactory(featureNameFactory);
                extractor.setBufferedReader(bufferedReader);
                extractor.setBufferedWriter(bufferedWriter);
                extractor.setTrainData(isTrainData);
                threads[index] = new Thread(extractor);
                threads[index].start();
            }

            for (Thread thread : threads) {
                if (thread != null) {
                    thread.join();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return;
    }


}

class Extractor implements Runnable {

    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    boolean isTrainData = true;
    FeatureNameFactory featureNameFactory = null;

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    public boolean isTrainData() {
        return isTrainData;
    }

    public void setTrainData(boolean isTrainData) {
        this.isTrainData = isTrainData;
    }

    public FeatureNameFactory getFeatureNameFactory() {
        return featureNameFactory;
    }

    public void setFeatureNameFactory(FeatureNameFactory featureNameFactory) {
        this.featureNameFactory = featureNameFactory;
    }

    @Override
    public void run() {
        String line = null;
        while (true) {
            synchronized (bufferedReader) {
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (line == null) {
                break;
            }
            try {
                extractFeaturestoWriter(line, ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void extractFeaturestoWriter(String line,
                                 String seprator) throws IOException {
        List<String> inputFields = StringProc.getFields(line, seprator);
        List<String> featureNames = featureNameFactory.extractFeatureNames(inputFields);
        List<Integer> featureIds = new ArrayList<Integer>();

        synchronized (bufferedWriter) {
            String label = "1";
            if (isTrainData) {
                int labelIndex = FieldDescription.getInstance().getValue("click");
                label = inputFields.get(labelIndex);
                if (label.equals("0")) {
                    label = "-1";
                }
            }
            bufferedWriter.write(label);

            for (String featureName : featureNames) {
                bufferedWriter.write(" ");
                bufferedWriter.write(featureName);
            }

            bufferedWriter.newLine();
        }
    }
}
