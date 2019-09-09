package de.leonlatsch.oliviabackend.queue;

import com.pusher.rest.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueManager {

    private static final Logger log = LoggerFactory.getLogger(QueueManager.class);

    private static QueueManager queueManager;
    private Pusher pusher;

    private static final String APP_ID = "858252";
    private static final String KEY = "4a193b226e1b7b214d62";
    private static final String SECRET = "dc432f7ef62103ba1a5a";
    private static final String CLUSTER = "eu";

    private QueueManager() {
        pusher = new Pusher(APP_ID, KEY, SECRET);
        pusher.setCluster(CLUSTER);
        pusher.setEncrypted(true);
        log.info("Initialized pusher");
    }

    public void sendMessage(String channel, String message, Object data) {
        pusher.trigger(channel, message, data);
    }

    public static QueueManager getInstance() {
        if (queueManager == null) {
            queueManager = new QueueManager();
        }
        return queueManager;
    }

    public static void init() {
        queueManager = new QueueManager();
    }
}
