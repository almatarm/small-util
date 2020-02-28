/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.http;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;


/**
 *
 * @author MUFEED
 */
public class BatchDownloader implements Runnable {
    protected String[] wFiles;
    protected String[] lFiles;
    protected int nThread;
    protected String post;
    protected boolean overwrite;
    protected ExecutorService pool;    

    protected JDialog dialog;
    protected JProgressBar[] progressBar;
    protected JLabel[] labels;

    protected JLabel totalLabel;
    protected JProgressBar totalProgressBar;

    protected boolean[] inUse;
    protected int done = 0;
    protected int bufferSize = 1024 *16;

    public BatchDownloader(String[] wFiles, String[] lFiles, int nThread) {
        this(wFiles, lFiles, nThread, null);
    }

    public BatchDownloader(String[] wFiles, String[] lFiles, int nThread, String post) {
        this.wFiles = wFiles;
        this.lFiles = lFiles;
        this.nThread = nThread;
        this.post = post;

        pool = Executors.newFixedThreadPool(nThread);
        inUse = new boolean[nThread];

        dialog = new JDialog();
        int count = wFiles.length;
        progressBar = new JProgressBar[nThread];
        labels      = new JLabel[nThread];

        JPanel pane = new JPanel();
        GridLayout layout = new GridLayout(2 * nThread + 2, 1);
        layout.setHgap(20);
        layout.setVgap(10);

        pane.setLayout(layout);

        totalLabel = new JLabel();
        totalProgressBar = new JProgressBar();
        totalProgressBar.setMaximum(count);
        done = -1; updateTotalDownloaded();

        pane.add(totalLabel);
        pane.add(totalProgressBar);

        for (int i = 0; i < nThread; i++) {
            labels[i] = new JLabel("Read to download");
            pane.add( labels[i] );

            progressBar[i] = new JProgressBar();
            progressBar[i].setValue(0);
            progressBar[i].setStringPainted(true);
            pane.add(progressBar[i]);
        }

        JPanel mainPanel = (new JPanel(new BorderLayout()));
        mainPanel.add(pane);
        dialog.setContentPane(mainPanel);
        dialog.setPreferredSize(new Dimension(600 , (nThread + 1) * 70));
        dialog.pack();
        dialog.setResizable(false);

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                pool.shutdown();
            }

        });
    }

    public void run() {
        dialog.setVisible(true);

        for (int i = 0; i < wFiles.length; i++) {
            pool.execute(new DownloadHandler(wFiles[i], lFiles[i], post));
        }
    }

    // -1 all busy; come agian later
    private synchronized int getFreeProgressBarIdx() {
        for (int i = 0; i < inUse.length; i++) {
            if(!inUse[i])
                return i;
        }
        return -1;
    }

    private synchronized void updateInUseStatus(final int idx, boolean status) {
        inUse[idx] = status;
    }

    //add one to done downloaded files.
    private synchronized void updateTotalDownloaded() {
        done++;
        totalLabel.setText("Total Download Status: " + done + "/" + wFiles.length);
        totalProgressBar.setValue(done);
    }

    private class DownloadHandler implements Runnable {
        private String webFile;
        private String localFile;
        private String post;

        public DownloadHandler(String webFile, String localFile) {
            this.webFile = fixEncoding(webFile);
            this.localFile = localFile;
            post = null;
        }

        public DownloadHandler(String webFile, String localFile, String post) {
            this.webFile = fixEncoding(webFile);
            this.localFile = localFile;
            this.post = post;
        }

        public void run() {
            try {

                if(webFile == null || localFile == null)
                    return;
                int tmp;
                while ((tmp = getFreeProgressBarIdx()) == -1)
                    Thread.sleep(5000);
                final int pbIdx = tmp;

                updateInUseStatus(pbIdx, true);

                labels[pbIdx].setText("Downloading: " + webFile);


                if (!overwrite && (new File(localFile)).exists()) {
                    updateTotalDownloaded();
                    progressBar[pbIdx].setValue(progressBar[pbIdx].getMaximum());
                    updateInUseStatus(pbIdx, false);
                    return;
                }

                File localDir = new File(localFile.substring(0, localFile.lastIndexOf(File.separator)));
                if(!localDir.exists())
                    localDir.mkdirs();

                //Start download
                URL url = new URL(webFile);
                URLConnection conn = null;
                OutputStreamWriter wr = null;
                if(post != null) {
                    conn = url.openConnection();
                    conn.setDoOutput(true);
                    wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(post);
                    wr.flush();
                }

                BufferedInputStream in;
                try {
                    if(post != null) {
                        in = new BufferedInputStream(conn.getInputStream());
                    } else {
                        in = new BufferedInputStream(url.openStream());
                    }
                } catch (Exception fnfe) {
                    //updateTotalDownloaded();
                    progressBar[pbIdx].setValue(progressBar[pbIdx].getMaximum());
                    updateInUseStatus(pbIdx, false);
                    return;
                }

                final int fileSize;
                if(post == null) {
                    fileSize = url.openConnection().getContentLength();
                } else {
                    fileSize = conn.getContentLength();
                }

                FileOutputStream fos = new java.io.FileOutputStream(localFile + ".part");
                BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                if(fileSize > 0) {
                    //progressBar[pbIdx].setMaximum(fileSize);
                    progressBar[pbIdx].setMaximum(100);
                    progressBar[pbIdx].setStringPainted(true);
                    progressBar[pbIdx].setString("%0" );
                } else {
                    progressBar[pbIdx].setStringPainted(true);
                    progressBar[pbIdx].setString("0 KB" );
                }

                byte[] data = new byte[bufferSize];
                int x = 0;
                long prog = 0;
                long t1 = (new Date()).getTime();
                long prevTotal = 0;
                double speed = 0;
                while ((x = in.read(data, 0, bufferSize)) != -1) {
                    bout.write(data, 0, x);
                    prog += x;
                    final long total = prog;

                    long t2 = (new Date()).getTime();
                    if(t2 - t1 > 2500) { // > 1 sec
                        speed = ((total - prevTotal)/1024)/((t2 - t1)/1000);
                        t1 = t2;
                        prevTotal = total;
                    }

                    final int spd = (int) speed;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           double kb = Math.floor(total/1024);
                           if(fileSize > 0) {
                               //System.out.println("T:" + total + ", S:" + fileSize
                               //        + ", %: " + (total*100/fileSize));
                               progressBar[pbIdx].setValue((int) (total*100/fileSize));
                               progressBar[pbIdx].setString("%" + (total*100/fileSize) + " - " +
                                    (kb <= 1024?kb + " KB":
                                    Math.floor(kb/1024*100)/100 + " MB") + " - " + 
                                    (spd <= 1024?spd + " KB/s":
                                    Math.floor(spd/1024*100)/100 + " MB/s"));
                            } else {
                               progressBar[pbIdx].setString((kb <= 1024?kb + " KB":
                                    Math.floor(kb/1024*100)/100 + " MB") + " - " +
                                    (spd <= 1024?spd + " KB/s":
                                    Math.floor(spd/1024*100)/100 + " MB/s"));
                            }
                        }
                    });
                }
                bout.close();
                in.close();
                if(post != null) {
                    wr.close();
                }

                if(fileSize < 0)
                    progressBar[pbIdx].setString(progressBar[pbIdx].getString() + " - Done");
                    //Rename the file
                    (new File(localFile + ".part")).renameTo(new File(localFile));


                updateInUseStatus(pbIdx, false);
                updateTotalDownloaded();
            } catch (Exception ex) {
                Logger.getLogger(BatchDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    private String fixEncoding(String in) {
        if (in == null) return in;
        return in.replaceAll(" ", "%20");
//        try {
//            return URLEncoder.encode(in, "UTF-8").replace("http%3A%2F%2F", "http://");
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(BatchDownloader.class.getName()).log(Level.SEVERE, null, ex);
//            return in;
//        }
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        String[] wFiles = {
            //"http://v16.lscache5.c.youtube.com/videoplayback?sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Cratebypass%2Cclient&key=yt1&itag=18&ipbits=8&title=Lecture%252017%2520%257C%2520The%2520Fourier%2520Transforms%2520and%2520its%2520Applications&sver=3&signature=A5AF62DC9E7C49184307517C45C1A132E78C82F6.7829313AED1667729C6E7FD6F3260A2F294614ED&ratebypass=yes&client=youtube-download&expire=1284523200&ip=24.0.0.0&id=4106c43a318e6392",
            "http://shiavoice.com/save-H3hOg.html",
            "http://www.astropix.com/IMAGES/G_SUN/ANNULART.JPG",
            "http://www.astropix.com/IMAGES/G_SUN/PROM1T.JPG",
            "http://www.astropix.com/IMAGES/G_SUN/AURORA1T.JPG",
            "http://www.astropix.com/IMAGES/G_SUN/SUNSPOTT.JPG",
            "http://www.astropix.com/IMAGES/G_SUN/VTRANS1T.JPG",
            //"http://www.mahdimedia.net/downrajb2010-9026-0.html",
        };
        String[] lFiles = {"124.mp3", "01.jpg", "02.jpg", "03.jpg", "04.jpg", "05.jpg", "06.jpg", "01.mp3"};

        for(int i = 0; i < lFiles.length; i++)
            lFiles[i] = "/home/almatarm/tmp/" + lFiles[i];
        int nThread = 1;
//        BatchDownloader bd = new BatchDownloader(wFiles, lFiles, nThread);
        BatchDownloader bd = new BatchDownloader(wFiles, lFiles, nThread,
                URLEncoder.encode("submit", "UTF-8") + "=" + URLEncoder.encode("تحميل", "UTF-8"));
        bd.setOverwrite(true);
        bd.run();

    }

}
