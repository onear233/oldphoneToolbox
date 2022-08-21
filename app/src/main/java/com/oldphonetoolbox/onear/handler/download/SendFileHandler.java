package com.oldphonetoolbox.onear.handler.download;

import com.oldphonetoolbox.onear.MainActivity;
import com.oldphonetoolbox.onear.handler.OPTBHandlerAbstract;
import com.oldphonetoolbox.onear.toolactivity.download.DownloadTransportThread;

public class SendFileHandler extends OPTBHandlerAbstract {
    @Override
    protected void executeCode(byte[] data, MainActivity activity) {
        new Thread(new DownloadTransportThread(activity,new String(data))).start();
    }
}